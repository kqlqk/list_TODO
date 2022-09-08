package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents implementation for {@link me.kqlqk.todo_list.service.RefreshTokenService}
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Value("${jwt.refresh.expired}")
    private Long validityInMillis;
    @Value("${jwt.refresh.secret}")
    private String secret;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenService accessTokenService;
    private final UserService userService;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   AccessTokenService accessTokenService,
                                   UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenService = accessTokenService;
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }


    @Override
    public boolean existsById(long id) {
        return refreshTokenRepository.existsById(id);
    }

    /**
     * @return {@link me.kqlqk.todo_list.models.RefreshToken} or null, if {@link me.kqlqk.todo_list.models.RefreshToken} not found
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} not found
     */
    @Override
    public RefreshToken getByUser(User user) {
        if (!userService.isValid(user)) {
            throw new UserNotFoundException("User with id = " + user.getId() + " not found");
        }

        return refreshTokenRepository.findByUserId(user.getId());
    }

    /**
     * @return {@link me.kqlqk.todo_list.models.RefreshToken} or null, if {@link me.kqlqk.todo_list.models.RefreshToken} not found
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if param token is null
     */
    @Override
    public RefreshToken getByStringToken(String token) {
        if (token == null || token.equals("")) {
            throw new TokenNotFoundException("Token cannot be null");
        }
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} not found
     */
    @Override
    public String createAndGetToken(User user) {
        if (!userService.isValid(user)) {
            throw new UserNotFoundException("User with id = " + user.getId() + " not found");
        }

        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        String refreshTokenString = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setExpiresIn(validity.getTime());
        refreshToken.setUser(user);
        user.setRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshToken);

        logger.info("Was created refresh token for " + user.getEmail());

        return refreshTokenString;
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if param token is null,
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException if param token cannot be parsed
     */
    @Override
    public String getEmail(String token) {
        if (token == null || token.equals("")) {
            throw new TokenNotFoundException("Refresh token cannot be null");
        }

        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenNotValidException("Refresh token cannot be parsed");
        }
    }

    @Override
    public long getValidity() {
        return validityInMillis;
    }

    @Override
    public boolean isValid(String refreshTokenString) {
        if (refreshTokenString == null || refreshTokenString.equals("")) {
            return false;
        }
        RefreshToken refreshToken = getByStringToken(refreshTokenString);

        try {
            String email = getEmail(refreshToken.getToken());
            if (!userService.isValid(userService.getByEmail(email))) {
                return false;
            }

            return new Date(refreshToken.getExpiresIn()).after(new Date());
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if {@link me.kqlqk.todo_list.models.RefreshToken} not found,
     * @throws IllegalArgumentException if param HttpServletRequest is null
     */
    @Override
    public String resolveToken(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest cannot be null");
        }

        String bearerWithToken = request.getHeader("Authorization_refresh");

        if (bearerWithToken == null) {
            throw new TokenNotFoundException("Authorization_refresh header not found");
        }
        if (!bearerWithToken.startsWith("Bearer_")) {
            throw new TokenNotFoundException("Refresh token should starts with Bearer_");
        }

        return bearerWithToken.substring(7);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} not found
     */
    @Override
    public String updateRefreshToken(User user) {
        if (!userService.isValid(user)) {
            throw new UserNotFoundException("User not found, if you haven't account, please, sign up");
        }

        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        RefreshToken refreshToken = getByUser(user);
        refreshToken.setExpiresIn(validity.getTime());
        refreshToken.setToken(token);
        user.setRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshToken);

        logger.info("Was updated refresh token for " + user.getEmail());

        return token;
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} not found,
     * @throws IllegalArgumentException if param HttpServletRequest OR HttpServletResponse are null
     */
    @Override
    public Map<String, String> updateAccessAndRefreshTokens(User user,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            boolean setCookie,
                                                            boolean rememberMe) {
        if (!userService.isValid(user)) {
            throw new UserNotFoundException("User not found");
        }
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest cannot be null");
        }
        if (response == null) {
            throw new IllegalArgumentException("HttpServletResponse cannot be null");
        }

        updateRefreshToken(user);

        String newAccessToken = accessTokenService.createAndGetToken(user.getEmail());
        String newRefreshToken = getByUser(user).getToken();

        if (setCookie && rememberMe) {
            UtilCookie.createOrUpdateOrDeleteCookie("at", newAccessToken, (int) (getValidity() / 1000), request, response);
            UtilCookie.createOrUpdateOrDeleteCookie("rt", newRefreshToken, (int) (getValidity() / 1000), request, response);
        }
        if (setCookie && !rememberMe) {
            UtilCookie.createOrUpdateOrDeleteCookie("at", newAccessToken, -1, request, response);
            UtilCookie.createOrUpdateOrDeleteCookie("rt", newRefreshToken, -1, request, response);
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", newAccessToken);
        tokens.put("refresh_token", newRefreshToken);

        logger.info("Was updated access nad refresh tokens for " + user.getEmail());

        return tokens;
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} not found,
     */
    @Override
    public Map<String, String> updateAccessAndRefreshTokens(User user) {
        if (!userService.isValid(user)) {
            throw new UserNotFoundException("User not found");
        }
        updateRefreshToken(user);

        String newAccessToken = accessTokenService.createAndGetToken(user.getEmail());
        String newRefreshToken = getByUser(user).getToken();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", newAccessToken);
        tokens.put("refresh_token", newRefreshToken);

        logger.info("Was updated access nad refresh tokens for " + user.getEmail());

        return tokens;
    }
}

package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletRequestNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletResponseNotFoundException;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilCookie;
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

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refresh.expired}")
    private Long validityInMillis;
    @Value("${jwt.refresh.secret}")
    private String secret;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenService accessTokenService;
    private final UserService userService;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, AccessTokenService accessTokenService, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenService = accessTokenService;
        this.userService = userService;
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    @Override
    public boolean existsById(long id) {
        return refreshTokenRepository.existsById(id);
    }

    @Override
    public RefreshToken getByUser(User user) {
        if(!userService.isValid(user)){
            throw new UserNotFoundException("User with id = " +  user.getId() + " not found");
        }

        return refreshTokenRepository.getByUserId(user.getId());
    }

    @Override
    public void create(User user) {
        if(!userService.isValid(user)){
            throw new UserNotFoundException("User with id = " +  user.getId() + " not found");
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
    }

    @Override
    public String getEmail(String token) {
        if(token == null){
            throw new TokenNotFoundException("Refresh token cannot be null");
        }

        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        }
        catch (JwtException | IllegalArgumentException e){
            throw new TokenNotValidException("Refresh token cannot be parsed");
        }
    }

    @Override
    public long getValidity(){
        return validityInMillis;
    }

    @Override
    public boolean isValid(RefreshToken refreshToken) {
        if (refreshToken == null){
            return false;
        }

        return new Date(refreshToken.getExpiresIn()).after(new Date());
    }

    @Override
    public String resolveToken(HttpServletRequest request){
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }

        String bearerWithToken = request.getHeader("Authorization_refresh");

        if(bearerWithToken == null){
            throw new TokenNotFoundException("Authorization_refresh header not found");
        }
        if(!bearerWithToken.startsWith("Bearer_")){
            throw new TokenNotFoundException("Refresh token should starts with Bearer_");
        }

        return bearerWithToken.substring(7);
    }

    @Override
    public void updateRefreshToken(User user) {
        if(!userService.isValid(user)){
            throw new UserNotFoundException("User not found, if you hasn't account try to sign up");
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
        refreshToken.setToken(token);
        refreshToken.setExpiresIn(validity.getTime());

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Map<String, String> updateAccessAndRefreshTokens(RefreshToken refreshToken, User user, HttpServletRequest request, HttpServletResponse response, boolean setCookie) {
        if(!isValid(refreshToken)){
            throw new TokenNotValidException("Refresh token is not valid, try to log in one more time");
        }
        if(!userService.isValid(user)){
            throw new UserNotFoundException("User not found");
        }
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }
        if(response == null){
            throw new HttpServletResponseNotFoundException("HttpServletResponse cannot be null");
        }

        updateRefreshToken(user);

        String newAccessToken = accessTokenService.createToken(user.getEmail());
        String newRefreshToken = getByUser(user).getToken();

        if(setCookie) {
            UtilCookie.createOrUpdateCookie("at", newAccessToken, (int) (getValidity() / 1000), request, response);
            UtilCookie.createOrUpdateCookie("rt", newRefreshToken, (int) (getValidity() / 1000), request, response);
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", newAccessToken);
        tokens.put("refresh_token", newRefreshToken);

        return tokens;
    }
}

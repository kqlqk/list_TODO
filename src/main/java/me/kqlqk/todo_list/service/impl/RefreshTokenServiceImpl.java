package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenIsNotValidException;
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

    //JPA-repository methods
    @Override
    public boolean existsById(long id) {
        return refreshTokenRepository.existsById(id);
    }

    @Override
    public boolean existsByToken(String token) {
        if(token == null){
            throw new TokenNotFoundException("Token cannot be a null");
        }
        return refreshTokenRepository.existsByToken(token);
    }

    @Override
    public RefreshToken getByUser(User user) {
        if(user == null){
            throw new UserNotFoundException("User cannot be null");
        }
        if(!userService.existsById(user.getId())){
            throw new UserNotFoundException(user + " not found");
        }
        return refreshTokenRepository.getByUser(user);
    }

    //RefreshTokenService methods
    @Override
    public void create(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        String token = Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();

        if(!userService.existsById(user.getId())){
            throw new UserNotFoundException(user + " not found");
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiresIn(validity.getTime());
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String getEmail(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        }
        catch (JwtException e){
            return null;
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
    public String resolveStringToken(HttpServletRequest request){
        String bearerWithToken = request.getHeader("Authorization_refresh");

        if(bearerWithToken != null && bearerWithToken.startsWith("Bearer_")){
            return bearerWithToken.substring(7);
        }

        return null;
    }

    @Override
    public void update(User user) {
        if(!userService.existsById(user.getId())){
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
            throw new TokenIsNotValidException("refresh token is not valid, try to log in one more time");
        }
        if(!userService.existsById(user.getId())){
            throw new UserNotFoundException(user + " not found");
        }

        update(user);

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

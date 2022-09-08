package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.*;
import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

/**
 * Represents implementation for {@link me.kqlqk.todo_list.service.AccessTokenService}
 */
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final UserService userService;

    @Value("${jwt.access.secret}")
    private String secret;

    @Value("${jwt.access.expired}")
    private long validityInMillis;

    @Autowired
    public AccessTokenServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }


    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if param email is null,
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException if {@link me.kqlqk.todo_list.models.User} isn't exist by email
     */
    @Override
    public String createAndGetToken(String email) {
        if(email == null || email.equals("")){
            throw new TokenNotFoundException("Email cannot be null");
        }
        if (userService.getByEmail(email) == null) {
            throw new UserNotFoundException("User with email = " + email + " not found");
        }

        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        logger.info("Was created access token for " + email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if param token is null,
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException if access token cannot be parsed
     */
    @Override
    public String getEmail(String token) {
        if (token == null || token.equals("")) {
            throw new TokenNotFoundException("Access token cannot be null");
        }

        try {
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new TokenNotValidException("Access token cannot be parsed");
        }
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException if token not found in header,
     * @throws IllegalArgumentException if param HttpServletRequest is null
     */
    @Override
    public String resolveToken(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest cannot be null");
        }

        String bearerWithToken = request.getHeader("Authorization_access");

        if (bearerWithToken == null) {
            throw new TokenNotFoundException("Authorization_access header not found");
        }
        if (!bearerWithToken.startsWith("Bearer_")) {
            throw new TokenNotFoundException("Access token should starts with Bearer_");
        }

        return bearerWithToken.substring(7);
    }

    @Override
    public boolean isValid(String token) {
        if (token == null) {
            return false;
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);

            String email = getEmail(token);
            if (!userService.isValid(userService.getByEmail(email))) {
                return false;
            }

            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public long getValidity() {
        return validityInMillis;
    }
}

package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.*;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletRequestNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    private final UserService userService;
    @Value("${jwt.access.secret}")
    private String secret;

    @Value("${jwt.access.expired}")
    private long validityInMillis;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AccessTokenServiceImpl(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String email){
        if(userService.getByEmail(email) == null){
            throw new UserNotFoundException("User with email = " + email + " not found");
        }

        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        if(token == null){
            throw new TokenNotValidException("Token cannot be null");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token){
        if(token == null){
            throw new TokenNotFoundException("Access token cannot be null");
        }

        try{
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        }
        catch (JwtException | IllegalArgumentException e){
            throw new TokenNotValidException("Access token cannot be parsed");
        }
    }

    public String resolveToken(HttpServletRequest request){
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }

        String bearerWithToken = request.getHeader("Authorization_access");

        if(bearerWithToken == null){
            throw new TokenNotFoundException("Authorization_access header not found");
        }
        if(!bearerWithToken.startsWith("Bearer_")){
            throw new TokenNotFoundException("Access token should starts with Bearer_");
        }

        return bearerWithToken.substring(7);
    }

    public boolean validateToken(String token){
        if(token == null){
            return false;
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public long getValidity() {
        return validityInMillis;
    }
}

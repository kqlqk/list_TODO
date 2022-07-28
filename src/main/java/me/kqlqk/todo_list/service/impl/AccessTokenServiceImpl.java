package me.kqlqk.todo_list.service.impl;

import io.jsonwebtoken.*;
import me.kqlqk.todo_list.service.AccessTokenService;
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
    @Value("${jwt.access.secret}")
    private String secret;

    @Value("${jwt.access.expired}")
    private long validityInMillis;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AccessTokenServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String email){
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        }
        catch (JwtException e){
            return null;
        }
    }

    public long getValidity() {
        return validityInMillis;
    }

    public String resolveToken(HttpServletRequest request){
        String bearerWithToken = request.getHeader("Authorization_access");

        if(bearerWithToken != null && bearerWithToken.startsWith("Bearer_")){
            return bearerWithToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token){
        if(token == null){
            return false;
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        }
        catch (JwtException e){
            return false;
        }
    }
}

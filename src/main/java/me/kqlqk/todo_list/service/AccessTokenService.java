package me.kqlqk.todo_list.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public interface AccessTokenService {
    String createToken(String email);
    Authentication getAuthentication(String token);
    String getEmail(String token);
    long getValidity();
    String resolveToken(HttpServletRequest request);
    boolean validateToken(String token);
}
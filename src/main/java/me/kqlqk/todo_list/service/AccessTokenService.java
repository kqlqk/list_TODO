package me.kqlqk.todo_list.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents service-layer for access token
 */
@Component
public interface AccessTokenService {

    /**
     * Creates access token for user
     *
     * @return string token
     */
    String createAndGetToken(String email);

    String getEmail(String token);

    long getValidity();

    /**
     * Gets token from HttpServletRequest
     *
     * @return string token
     */
    String resolveToken(HttpServletRequest request);

    boolean isValid(String token);
}
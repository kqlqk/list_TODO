package me.kqlqk.todo_list.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Represents service-layer for Authentication
 */
@Component
public interface AuthenticationService {

    /**
     * Set authentication to SecurityContextHolder
     *
     * @param loginObj email or login
     */
    void setAuthentication(String loginObj);

    /**
     * Set authentication to SecurityContextHolder
     */
    void setAuthentication(Authentication authentication);

    Authentication getUsernamePasswordAuthenticationToken(UserDetails userDetails, String rawPassword);

    /**
     * @param loginObj email or login
     */
    Authentication getUsernamePasswordAuthenticationTokenWithoutCredentials(String loginObj);

    /**
     * @return Authentication from SecurityContextHolder
     */
    Authentication getAuthenticationFromContext();

}

package me.kqlqk.todo_list.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {
    void setAuthentication(String loginObj);
    void setAuthentication(Authentication authentication);
    Authentication getUsernamePasswordAuthenticationToken(UserDetails userDetails, String rawPassword);
    Authentication getUsernamePasswordAuthenticationTokenWithoutCredentials(String loginObj);
    Authentication getAuthenticationFromContext();

}

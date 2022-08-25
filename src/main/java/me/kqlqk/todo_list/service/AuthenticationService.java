package me.kqlqk.todo_list.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {
    void setAuthentication(UserDetails userDetails);

    void setAuthentication(Authentication authentication);
    Authentication getAuthentication(UserDetails userDetails);

    Authentication getAuthenticationFromContext();

}

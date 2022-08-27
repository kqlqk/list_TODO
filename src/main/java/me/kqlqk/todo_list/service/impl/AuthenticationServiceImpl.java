package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void setAuthentication(String loginObj) {
        if(loginObj == null || loginObj.equals("")){
            throw new NullPointerException("LoginObj cannot be null");
        }

        SecurityContextHolder.getContext().setAuthentication(
                getUsernamePasswordAuthenticationTokenWithoutCredentials(loginObj));
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        if(authentication == null){
            throw new NullPointerException("Auth cannot be null");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Authentication getUsernamePasswordAuthenticationToken(UserDetails userDetails, String rawPassword) {
        if(userDetails == null){
            throw new NullPointerException("UserDetails cannot be null");
        }
        if(rawPassword == null || rawPassword.equals("")){
            throw new NullPointerException("If you want to get authentication without password," +
                    " please use getUsernamePasswordAuthenticationTokenWithoutCredentials(UserDetails userDetails)");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
    }

    @Override
    public Authentication getUsernamePasswordAuthenticationTokenWithoutCredentials(String loginObj) {
        if(loginObj == null || loginObj.equals("")){
            throw new NullPointerException("LoginObj cannot be null");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetailsService.loadUserByUsername(loginObj),
                null,
                userDetailsService.loadUserByUsername(loginObj).getAuthorities());
    }

    @Override
    public Authentication getAuthenticationFromContext() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        }
        catch (NullPointerException e){
            return null;
        }
    }

}

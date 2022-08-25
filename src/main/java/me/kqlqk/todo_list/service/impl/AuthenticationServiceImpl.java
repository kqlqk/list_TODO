package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public void setAuthentication(UserDetails userDetails) {
        if(userDetails == null){
            throw new NullPointerException("UserDetails cannot be null");
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(userDetails));
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        if(authentication == null){
            throw new NullPointerException("Auth cannot be null");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Authentication getAuthentication(UserDetails userDetails) {
        if(userDetails == null){
            throw new NullPointerException("UserDetails cannot be null");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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

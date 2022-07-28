package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginObj) {
        if(loginObj == null){
            throw new UserNotFoundException("LoginObj cannot be a null");
        }
        User user = userRepository.getByEmail(loginObj) == null ?
                userRepository.getByLogin(loginObj) :
                userRepository.getByEmail(loginObj);

        if(user == null){
            throw new UserNotFoundException("User with loginObj = " + loginObj + " not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}

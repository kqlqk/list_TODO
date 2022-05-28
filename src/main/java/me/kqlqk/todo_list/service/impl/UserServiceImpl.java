package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    //JPA-repository methods
    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    //UserService methods
    @Override
    public User getByEmailOrLogin(String loginObj) {
        User user = getByEmail(loginObj);
        return user != null ? user : getByLogin(loginObj);
    }


    @Override
    @Transactional
    public void addNew(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findById(1L).isPresent() ? roleRepository.findById(1L).get() : null);
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        return getByEmail(getCurrentEmail());
    }

    @Override
    public String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public boolean tryAutoLogin(String loginObj, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginObj);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        Authentication auth = authenticationManager.authenticate(token);

        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        }

        return false;
    }
}

package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder) {
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
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.getById(1L));
        userRepository.save(user);
        logger.info("was created " + user);
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("was updated " + user);
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
    public boolean canAutoLogin(String loginObj, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginObj);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        Authentication auth = authenticationManager.authenticate(token);

        return auth.isAuthenticated();
    }

    @Override
    public void autoLogin(String loginObj, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginObj);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        Authentication auth = authenticationManager.authenticate(token);

        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("was set auth");
        }
    }
}

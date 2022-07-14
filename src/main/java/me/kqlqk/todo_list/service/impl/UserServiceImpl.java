package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions.dao.user.UserAlreadyExistException;
import me.kqlqk.todo_list.exceptions.dao.user.UserNotFoundException;
import me.kqlqk.todo_list.exceptions.dao.user.status.UserStatus;
import me.kqlqk.todo_list.exceptions.service_exceptions.AuthenticationNotAuthenticatedException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${temp.password.oauth2}")
    private String tempPassword;

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
        return userRepository.getByEmail(email.toLowerCase());
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login){
        return userRepository.existsByLogin(login);
    }

    //UserService methods
    @Override
    @Transactional
    public void add(User user) {
        if(existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exist, Email already exist", UserStatus.EMAIL_ALREADY_EXIST);
        }
        if(existsByLogin(user.getLogin())) {
            throw new UserAlreadyExistException("User already exist, Login already exist", UserStatus.LOGIN_ALREADY_EXIST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.getById(1L));
        userRepository.save(user);
    }


    @Override
    public List<User> getAll() {
       return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User getByLoginObj(String loginObj) {
        User user = getByEmail(loginObj);
        if(user == null) {
            user = getByLogin(loginObj);
            if(user == null) {
                throw new UserNotFoundException("User not found, Login object is " + (loginObj.equals("") ? "null" : loginObj) );
            }
        }

        return user;
    }

    @Override
    public OAuth2User getOAuth2UserFromSecurityContextHolder() {
        OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(oAuth2User == null) {
            throw new UserNotFoundException("OAuth2User not found, SecurityContextHolder principals " +
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        SecurityContextHolder.clearContext();
        return oAuth2User;
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        return getByEmail(getCurrentEmail());
    }

    @Override
    public String getCurrentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    @Override
    public void setAuth(String loginObj, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginObj);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        Authentication auth = authenticationManager.authenticate(token);

        if(!auth.isAuthenticated()) {
            throw new AuthenticationNotAuthenticatedException("Authentication not authenticated, auth is " + auth);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public User convertOAuth2UserToUserAndSave(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(oAuth2User.getAttribute("email").toString().toLowerCase());
        user.setLogin(oAuth2User.getAttribute("name"));
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setOAuth2(true);
        user.setRole(roleRepository.getById(1L));
        userRepository.save(user);

        return user;
    }

    @Override
    public boolean isUserUsedOAuth2Login() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser";
    }
}

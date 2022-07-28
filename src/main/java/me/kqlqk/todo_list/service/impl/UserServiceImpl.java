package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PasswordEncoder passwordEncoder;

    @Value("${temp.password.oauth2}")
    private String tempPassword;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    //JPA-repository methods
    @Override
    public User getByEmail(String email) {
        if(email == null){
            throw new UserNotFoundException("Email cannot be null");
        }
        return userRepository.getByEmail(email.toLowerCase());
    }

    @Override
    public User getByLogin(String login) {
        if(login == null){
            throw new UserNotFoundException("Login cannot be null");
        }
        return userRepository.getByLogin(login);
    }

    @Override
    public User getByRefreshToken(RefreshToken refreshToken) {
        if(refreshToken == null){
            throw new TokenNotFoundException("Refresh token cannot be null");
        }
        return userRepository.getByRefreshToken(refreshToken);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByLogin(String login){
        return userRepository.existsByLogin(login);
    }

    //UserService methods
    @Override
    @Transactional
    public void add(User user) {
        if(getByEmail(user.getEmail()) != null){
            throw new UserAlreadyExistsException(user + " already exists");
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
        if(loginObj == null){
            throw new UserNotFoundException("loginObj cannot be a null");
        }

        return getByEmail(loginObj.toLowerCase()) == null ? getByLogin(loginObj) : getByEmail(loginObj.toLowerCase());
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
        if(getByEmail(user.getEmail()) == null){
            throw new UserNotFoundException(user + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        return getByEmail(getCurrentEmail());
    }

    @Override
    public String getCurrentEmail() {
        try{
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        catch (NullPointerException e){
            return null;
        }
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

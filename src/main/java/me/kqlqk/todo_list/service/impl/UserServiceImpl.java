package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.AuthenticationService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public User getByEmail(String email) {
        if(email == null){
            throw new UserNotFoundException("Email cannot be null");
        }

        return userRepository.findByEmail(email.toLowerCase());
    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getByLogin(String login) {
        if(login == null){
            throw new UserNotFoundException("Login cannot be null");
        }
        return userRepository.findByLogin(login);
    }

    @Override
    public User getByRefreshToken(RefreshToken refreshToken) {
        if(refreshToken == null){
            throw new TokenNotFoundException("Refresh token cannot be null");
        }
        return userRepository.findByRefreshToken(refreshToken);
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
    public void add(User user) {
        if(getByEmail(user.getEmail()) != null){
            throw new UserAlreadyExistsException(user + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findById(1L));
        userRepository.save(user);

        logger.info("Was created new user " + user.getEmail());
    }

    @Override
    public List<User> getAll() {
       return userRepository.findAll();
    }

    @Override
    public User getByLoginObj(String loginObj) {
        if(loginObj == null){
            throw new UserNotFoundException("loginObj cannot be a null");
        }

        return getByEmail(loginObj.toLowerCase()) == null ? getByLogin(loginObj) : getByEmail(loginObj.toLowerCase());
    }

    @Override
    public void update(User user) {
        if(!userRepository.existsById(user.getId())){
            throw new UserNotFoundException("User not found");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("Was updated user " + user.getEmail());
    }

    @Override
    public User getCurrentUser() {
        return getByEmail(getCurrentEmail());
    }

    @Override
    public String getCurrentEmail() {
        return authenticationService.getAuthenticationFromContext().getName();
    }

    @Override
    public boolean isValid(User user){
        if(user == null || !existsById(user.getId()) || user.getEmail() == null || user.getLogin() == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(long userId){
        return isValid(getById(userId));
    }
}

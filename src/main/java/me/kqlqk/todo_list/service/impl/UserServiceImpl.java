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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getByEmail(String email) {
        if(email == null){
            throw new UserNotFoundException("Email cannot be null");
        }

        return userRepository.getByEmail(email.toLowerCase());
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
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

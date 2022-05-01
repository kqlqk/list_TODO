package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    void addNew(User user);
    User findByEmail(String email);
    boolean tryAutoLoginAfterRegistration(String email, String password);

}

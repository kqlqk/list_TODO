package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    void addNew(User user);
    User getByEmail(String email);
    String getCurrentEmail();
    User getCurrentUser();
    boolean tryAutoLoginAfterRegistration(String email, String password);
}

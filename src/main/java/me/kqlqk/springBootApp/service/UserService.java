package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    User getByEmail(String email);

    void addNew(User user);
    String getCurrentEmail();
    User getCurrentUser();
    boolean tryAutoLoginAfterRegistration(String email, String password);
}

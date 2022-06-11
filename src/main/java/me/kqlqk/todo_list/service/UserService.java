package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    User getByEmail(String email);
    User getByLogin(String login);

    void add(User user);
    void autoLogin(String loginObj, String password);
    boolean canAutoLogin(String loginObj, String password);
    User getByEmailOrLogin(String loginObj);
    String getCurrentEmail();
    User getCurrentUser();
    void update(User user);
}

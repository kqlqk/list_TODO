package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    User getByEmail(String email);
    User getByLogin(String login);

    void addNew(User user);
    User getByEmailOrLogin(String loginObj);
    String getCurrentEmail();
    User getCurrentUser();
    boolean tryAutoLogin(String loginObj, String password);
}

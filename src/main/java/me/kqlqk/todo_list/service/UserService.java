package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService{
    User getByEmail(String email);
    User getById(long id);
    User getByLogin(String login);
    User getByRefreshToken(RefreshToken refreshToken);
    boolean existsByEmail(String email);
    boolean existsById(long id);
    boolean existsByLogin(String login);

    void add(User user);
    List<User> getAll();
    User getByLoginObj(String loginObj);
    String getCurrentEmail();
    User getCurrentUser();
    void update(User user);
    boolean isValid(User user);
    boolean isValid(long userId);
}

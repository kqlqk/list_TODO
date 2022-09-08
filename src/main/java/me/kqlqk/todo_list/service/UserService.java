package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents service-layer for {@link me.kqlqk.todo_list.models.User}
 */
@Component
public interface UserService {
    User getByEmail(String email);

    User getById(long id);

    User getByLogin(String login);

    User getByRefreshToken(RefreshToken refreshToken);

    boolean existsByEmail(String email);

    boolean existsById(long id);

    boolean existsByLogin(String login);

    /**
     * Adds new {@link me.kqlqk.todo_list.models.User} to db
     */
    void add(User user);

    List<User> getAll();

    /**
     * @param loginObj email or login
     */
    User getByLoginObj(String loginObj);

    String getCurrentEmail();

    User getCurrentUser();

    /**
     * Updates {@link me.kqlqk.todo_list.models.User} in db
     */
    void update(User user);

    boolean isValid(User user);

    boolean isValid(long userId);
}

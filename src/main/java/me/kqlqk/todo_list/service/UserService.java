package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService{
    User getByEmail(String email);
    User getByLogin(String login);
    User getByRefreshToken(RefreshToken refreshToken);
    boolean existsByEmail(String email);
    boolean existsById(long id);
    boolean existsByLogin(String login);

    void add(User user);
    User convertOAuth2UserToUserAndSave(OAuth2User oAuth2User);
    List<User> getAll();
    User getByLoginObj(String loginObj);
    String getCurrentEmail();
    User getCurrentUser();
    OAuth2User getOAuth2UserFromSecurityContextHolder();
    boolean isUserUsedOAuth2Login();
    void update(User user);
}

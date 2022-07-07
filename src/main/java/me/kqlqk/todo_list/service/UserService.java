package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public interface UserService{
    User getByEmail(String email);
    User getByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);

    void add(User user);
    User convertOAuth2UserToUserAndSave(OAuth2User oAuth2User);
    User getByLoginObj(String loginObj);
    String getCurrentEmail();
    User getCurrentUser();
    OAuth2User getOAuth2UserFromSecurityContextHolder();
    boolean isUserUsedOAuth2Login();
    void setAuth(String loginObj, String password);
    void update(User user);
}

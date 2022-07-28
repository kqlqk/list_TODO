package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public interface RefreshTokenService {
    boolean existsById(long id);
    boolean existsByToken(String token);
    RefreshToken getByUser(User user);

    void create(User user);
    String getEmail(String token);
    long getValidity();
    boolean isValid(RefreshToken refreshToken);
    String resolveStringToken(HttpServletRequest request);
    void update(User user);
    Map<String, String> updateAccessAndRefreshTokens(RefreshToken refreshToken, User user, HttpServletRequest request, HttpServletResponse response, boolean setCookie);
}

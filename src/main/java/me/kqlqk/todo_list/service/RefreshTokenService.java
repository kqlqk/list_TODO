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
    RefreshToken getByUser(User user);

    void createAndAddToken(User user);
    String getEmail(String token);
    long getValidity();
    boolean isValid(RefreshToken refreshToken);
    String resolveToken(HttpServletRequest request);
    void updateRefreshToken(User user);
    Map<String, String> updateAccessAndRefreshTokens(User user, HttpServletRequest request, HttpServletResponse response, boolean setCookie);
}

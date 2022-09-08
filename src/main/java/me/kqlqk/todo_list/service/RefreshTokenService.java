package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Represents service-layer for {@link me.kqlqk.todo_list.models.RefreshToken}
 */
@Component
public interface RefreshTokenService {
    boolean existsById(long id);

    RefreshToken getByUser(User user);

    RefreshToken getByStringToken(String token);

    String getEmail(String token);

    long getValidity();

    boolean isValid(String refreshToken);

    /**
     * Adds new {@link me.kqlqk.todo_list.models.RefreshToken} to db
     *
     * @return string token
     */
    String createAndGetToken(User user);

    /**
     * Gets token from HttpServletRequest
     *
     * @return string token
     */
    String resolveToken(HttpServletRequest request);

    /**
     * Updates {@link me.kqlqk.todo_list.models.RefreshToken} in db
     *
     * @return string token
     */
    String updateRefreshToken(User user);

    /**
     * Updates {@link me.kqlqk.todo_list.models.RefreshToken} in db AND access token
     *
     * @return Map with access and refresh tokens
     */
    Map<String, String> updateAccessAndRefreshTokens(User user, HttpServletRequest request, HttpServletResponse response, boolean setCookie, boolean rememberMe);

    /**
     * Updates {@link me.kqlqk.todo_list.models.RefreshToken} in db AND access token
     *
     * @return Map with access and refresh tokens
     */
    Map<String, String> updateAccessAndRefreshTokens(User user);
}

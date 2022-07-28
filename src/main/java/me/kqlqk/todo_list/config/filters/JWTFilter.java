package me.kqlqk.todo_list.config.filters;

import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.exceptions_handling.GlobalErrorController;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenIsNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    private final List<String> uncheckedURI = Arrays.asList(
            "/login",
            "/registration",
            "/recovery",
            "/error",
            "/api/login",
            "/api/registration",
            "/api/error",
            "/api/recovery");

    @Autowired
    public JWTFilter(AccessTokenService accessTokenService, RefreshTokenService refreshTokenService, UserService userService) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean isRest = request.getRequestURI().contains("/api");

        for(String uri : uncheckedURI){
            if(request.getRequestURI().startsWith(uri) || request.getRequestURI().equals("/")){
                filterChain.doFilter(request, response);
                return;
            }
        }

        if ((!UtilCookie.isCookieExistsByName("at", request) || !UtilCookie.isCookieExistsByName("rt", request)) &&
                (accessTokenService.resolveToken(request) == null || refreshTokenService.resolveStringToken(request) == null)) {
            GlobalErrorController.setInfo(
                    new TokenNotFoundException("Necessary cookies / headers not found, try to log in one more time"), isRest);

            filterChain.doFilter(request,response);
            return;
        }

        String accessToken;
        String refreshTokenFromRequest;

        boolean setCookie;
        if (UtilCookie.getCookieByName("at", request) != null) {
            setCookie = true;
            accessToken = UtilCookie.getCookieByName("at", request).getValue();
            refreshTokenFromRequest = UtilCookie.getCookieByName("rt", request).getValue();

        }
        else {
            setCookie = false;
            accessToken = accessTokenService.resolveToken(request);
            refreshTokenFromRequest = refreshTokenService.resolveStringToken(request);
        }

        if (!accessTokenService.validateToken(accessToken)) {
            User user;
            try {
                user = userService.getCurrentUser();
            }
            catch (UserNotFoundException e) {
                try {
                    user = userService.getByEmail(refreshTokenService.getEmail(refreshTokenFromRequest));
                }
                catch (UserNotFoundException ex){
                    GlobalErrorController.setInfo(new TokenIsNotValidException("Access and refresh token aren't valid, try to log in one more time"), isRest);
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            RefreshToken refreshTokenFromDb = refreshTokenService.getByUser(user);

            if (refreshTokenFromRequest.equals(refreshTokenFromDb.getToken())) {
                Map<String, String> tokens = refreshTokenService.updateAccessAndRefreshTokens(refreshTokenFromDb, user, request, response, setCookie);
                accessToken = tokens.get("access_token");
                refreshTokenFromRequest = tokens.get("refresh_token");
                response.setHeader("Authorization_access", "Bearer_" + accessToken);
                response.setHeader("Authorization_refresh", "Bearer_" + refreshTokenFromRequest);
            }
            else {
                GlobalErrorController.setInfo(new TokenIsNotValidException("Access and refresh token aren't valid, try to log in one more time"), isRest);
                filterChain.doFilter(request, response);
                return;
            }
        }

        Authentication authentication = accessTokenService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}

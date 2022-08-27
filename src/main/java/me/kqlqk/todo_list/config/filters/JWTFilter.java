package me.kqlqk.todo_list.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kqlqk.todo_list.dto.ExceptionDTO;
import me.kqlqk.todo_list.exceptions_handling.RestGlobalExceptionHandler;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.AuthenticationService;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import me.kqlqk.todo_list.util.UtilCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RestGlobalExceptionHandler restExceptionHandler;
    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImpl userDetailsService;

    private final List<String> URIs = Arrays.asList(
            "/home",
            "/logout",
            "/admin",

            "/api/notes",
            "/api/admin");


    @Autowired
    public JWTFilter(AccessTokenService accessTokenService,
                     RefreshTokenService refreshTokenService,
                     UserService userService,
                     RestGlobalExceptionHandler restExceptionHandler, AuthenticationService authenticationService, UserDetailsServiceImpl userDetailsService) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.restExceptionHandler = restExceptionHandler;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean isRest = request.getRequestURI().contains("/api");
        ExceptionDTO exceptionDTO;

        int count = 0;
        for(String uri : URIs){
            if(!request.getRequestURI().startsWith(uri)){
                count++;
            }
            if(count == URIs.size()){
                filterChain.doFilter(request, response);
                return;
            }
        }


        String accessTokenString = null;
        String refreshTokenString = null;

        if (!UtilCookie.isCookieExistsByName("at", request) || !UtilCookie.isCookieExistsByName("rt", request)){
            try {
                accessTokenString = accessTokenService.resolveToken(request);
                refreshTokenString = refreshTokenService.resolveToken(request);
            }
            catch (TokenNotFoundException e){
                if(isRest) {
                    exceptionDTO = restExceptionHandler.handleNotFoundAndNotValidExceptions(e);
                    postException(response, exceptionDTO);
                }
                return;
            }
        }

        boolean setCookie;

        if (UtilCookie.getCookieByName("at", request) != null) {
            setCookie = true;
            accessTokenString = UtilCookie.getCookieByName("at", request).getValue();
            refreshTokenString = UtilCookie.getCookieByName("rt", request).getValue();
        }
        else {
            setCookie = false;
        }

        if (!accessTokenService.validateToken(accessTokenString)) {
            User user = null;
            try {
                user = userService.getCurrentUser();
            }
            catch (UserNotFoundException e) {
                try {
                    user = userService.getByEmail(refreshTokenService.getEmail(refreshTokenString));
                }
                catch (UserNotFoundException | TokenNotValidException ex){
                    if(isRest) {
                        exceptionDTO = new ExceptionDTO();
                        exceptionDTO.setInfo("Access and refresh tokens aren't valid, try to log in one more time");
                        postException(response, exceptionDTO);
                    }
                    return;
                }
            }

            RefreshToken refreshTokenFromDb = refreshTokenService.getByUser(user);

            if (refreshTokenString.equals(refreshTokenFromDb.getToken())) {
                Map<String, String> tokens = refreshTokenService.updateAccessAndRefreshTokens(user, request, response, setCookie);
                accessTokenString = tokens.get("access_token");
                refreshTokenString = tokens.get("refresh_token");
                response.setHeader("Authorization_access", "Bearer_" + accessTokenString);
                response.setHeader("Authorization_refresh", "Bearer_" + refreshTokenString);
            }
            else {
                if(isRest) {
                    exceptionDTO = new ExceptionDTO();
                    exceptionDTO.setInfo("Access and refresh tokens aren't valid, try to log in one more time");
                    postException(response, exceptionDTO);
                }
                return;
            }
        }

        String email = accessTokenService.getEmail(accessTokenString);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        authenticationService.setAuthentication(userDetails);

        filterChain.doFilter(request, response);
    }

    private void postException(HttpServletResponse response, ExceptionDTO exceptionDTO) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionDTO));
        response.getWriter().flush();
    }

}

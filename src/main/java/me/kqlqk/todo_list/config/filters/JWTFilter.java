package me.kqlqk.todo_list.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kqlqk.todo_list.dto.ExceptionDTO;
import me.kqlqk.todo_list.exceptions_handling.RestGlobalExceptionHandler;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.AuthenticationService;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.GlobalVariables;
import me.kqlqk.todo_list.util.UtilCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents functionality to check incoming request
 * for the content of necessary authorization headers or cookies
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RestGlobalExceptionHandler restExceptionHandler;

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
                     RestGlobalExceptionHandler restExceptionHandler,
                     AuthenticationService authenticationService) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.restExceptionHandler = restExceptionHandler;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        boolean isRest = request.getRequestURI().contains("/api");

        if (!shouldCheckRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> tokens;
        ExceptionDTO exceptionDTO;
        boolean setCookie;

        try {
            tokens = getTokensFromCookie(request);
            setCookie = true;
        } catch (TokenNotFoundException e) {
            try {
                tokens = getTokensFromHeaders(request);
                setCookie = false;
            } catch (TokenNotFoundException nestedEx) {
                exceptionDTO = restExceptionHandler.handleNotFoundAndNotValidExceptions(nestedEx);
                postException(exceptionDTO, isRest, response);
                return;
            }
        }

        String accessTokenString = tokens.get("access_token");
        String refreshTokenString = tokens.get("refresh_token");

        if (!accessTokenService.isValid(accessTokenString)) {
            if (!refreshTokenService.isValid(refreshTokenString)) {
                exceptionDTO = new ExceptionDTO();
                exceptionDTO.setInfo("Access and refresh tokens aren't valid, try to log in one more time");
                postException(exceptionDTO, isRest, response);
                return;
            }

            User user = userService.getByEmail(refreshTokenService.getEmail(refreshTokenString));
            tokens = refreshTokenService.updateAccessAndRefreshTokens(user, request, response, setCookie, GlobalVariables.REMEMBER_ME);
            refreshTokenString = tokens.get("refresh_token");
            response.setHeader("Authorization_access", "Bearer_" + tokens.get("access_token"));
            response.setHeader("Authorization_refresh", "Bearer_" + tokens.get("refresh_token"));
        }

        if (!refreshTokenService.isValid(refreshTokenString)) {
            exceptionDTO = new ExceptionDTO();
            exceptionDTO.setInfo("Refresh token aren't valid, try to log in one more time");
            postException(exceptionDTO, isRest, response);
            return;
        }

        String email = refreshTokenService.getEmail(tokens.get("refresh_token"));
        authenticationService.setAuthentication(email);
        filterChain.doFilter(request, response);
    }

    private boolean shouldCheckRequest(HttpServletRequest request) {
        int count = 0;

        for (String uri : URIs) {
            if (!request.getRequestURI().startsWith(uri)) {
                count++;
            }
            if (count == URIs.size()) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> getTokensFromCookie(HttpServletRequest request) {
        if (!UtilCookie.isCookieExistsByName("at", request) ||
                !UtilCookie.isCookieExistsByName("rt", request)) {
            throw new TokenNotFoundException("There's no cookie with tokens");
        }

        String accessToken = UtilCookie.getCookieByName("at", request).getValue();
        String refreshToken = UtilCookie.getCookieByName("rt", request).getValue();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    private Map<String, String> getTokensFromHeaders(HttpServletRequest request) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessTokenService.resolveToken(request));
        tokens.put("refresh_token", refreshTokenService.resolveToken(request));

        return tokens;
    }

    private void postException(ExceptionDTO exceptionDTO,
                               boolean isRest,
                               HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        if (isRest) {
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionDTO));
            response.getWriter().flush();
        } else {
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", "/login");
        }
    }


}

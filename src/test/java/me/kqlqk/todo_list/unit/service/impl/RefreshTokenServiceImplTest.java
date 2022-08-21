package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletRequestNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletResponseNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private User user;

    @Mock
    private UserService userService;

    @Mock
    private RefreshToken refreshToken;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    public void existsById_shouldCallRefreshTokenRepository(){
        refreshTokenServiceImpl.existsById(10L);

        verify(refreshTokenRepository, times(1)).existsById(10L);
    }

    @Test
    public void getByUser_shouldCallRefreshTokenRepository(){
        doReturn(true).when(userService).isValid(user);

        refreshTokenServiceImpl.getByUser(user);

        verify(refreshTokenRepository, times(1)).getByUserId(0L);
    }

    @Test
    public void getByUser_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.getByUser(user));
    }

    @Test
    public void create_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.create(user));
    }

    @Test
    public void getEmail_shouldThrowTokenNotFoundException(){
        assertThrows(TokenNotFoundException.class, () ->refreshTokenServiceImpl.getEmail(null));
    }

    @Test
    public void getEmail_shouldThrowTokenNotValidException(){
        assertThrows(TokenNotValidException.class, () ->refreshTokenServiceImpl.getEmail("anyEmail"));
    }

    @Test
    public void isValid_shouldCheckIsTokenValid(){
        assertThat(refreshTokenServiceImpl.isValid(null)).isFalse();

        doReturn(new Date().getTime() + 1000).when(refreshToken).getExpiresIn();

        assertThat(refreshTokenServiceImpl.isValid(refreshToken)).isTrue();
    }

    @Test
    public void resolveToken_shouldReturnToken(){
        doReturn("Bearer_anyToken").when(request).getHeader("Authorization_refresh");

        String token = refreshTokenServiceImpl.resolveToken(request);

        assertThat(token).isEqualTo("anyToken");
    }

    @Test
    public void resolveToken_shouldThrowHttpServletRequestNotFoundException(){
        assertThrows(HttpServletRequestNotFoundException.class, () -> refreshTokenServiceImpl.resolveToken(null));
    }

    @Test
    public void resolveToken_shouldThrowTokenNotFoundException(){
        RuntimeException e = assertThrows(TokenNotFoundException.class, () -> refreshTokenServiceImpl.resolveToken(request));
        assertThat(e.getMessage()).isEqualTo("Authorization_refresh header not found");
    }

    @Test
    public void resolveToken_shouldThrowTokenNotFoundExceptionWithAnotherException(){
        doReturn("anyToken").when(request).getHeader("Authorization_refresh");

        RuntimeException e = assertThrows(TokenNotFoundException.class, () -> refreshTokenServiceImpl.resolveToken(request));
        assertThat(e.getMessage()).isEqualTo("Refresh token should starts with Bearer_");
    }

    @Test
    public void updateRefreshToken_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.create(user));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowTokenNotValidException(){
        assertThrows(TokenNotValidException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(refreshToken, user, request, response, false));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowUserNotFoundException(){
        doReturn(new Date().getTime() + 1000).when(refreshToken).getExpiresIn();

        assertThrows(UserNotFoundException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(refreshToken, user, request, response, false));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowHttpServletRequestNotFoundException(){
        doReturn(new Date().getTime() + 1000).when(refreshToken).getExpiresIn();
        doReturn(true).when(userService).isValid(user);

        assertThrows(HttpServletRequestNotFoundException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(refreshToken, user, null, response, false));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowHttpServletResponseNotFoundException(){
        doReturn(new Date().getTime() + 1000).when(refreshToken).getExpiresIn();
        doReturn(true).when(userService).isValid(user);

        assertThrows(HttpServletResponseNotFoundException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(refreshToken, user, request, null, false));
    }
}

package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException;
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
    public void existsById_shouldCallsRefreshTokenRepository(){
        refreshTokenServiceImpl.existsById(10L);

        verify(refreshTokenRepository, times(1)).existsById(10L);
    }

    @Test
    public void getByUser_shouldCallsRefreshTokenRepository(){
        doReturn(true).when(userService).isValid(user);

        refreshTokenServiceImpl.getByUser(user);

        verify(refreshTokenRepository, times(1)).getByUserId(0L);
    }

    @Test
    public void getByUser_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.getByUser(user));
    }

    @Test
    public void create_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.createAndGetToken(user));
    }

    @Test
    public void getEmail_shouldThrowsTokenNotFoundException(){
        assertThrows(TokenNotFoundException.class, () ->refreshTokenServiceImpl.getEmail(null));
    }

    @Test
    public void getEmail_shouldThrowsTokenNotValidException(){
        assertThrows(TokenNotValidException.class, () ->refreshTokenServiceImpl.getEmail("anyEmail"));
    }

    @Test
    public void isValid_shouldChecksIsTokenValid(){
        assertThat(refreshTokenServiceImpl.isValid(null)).isFalse();
    }

    @Test
    public void resolveToken_shouldReturnsToken(){
        doReturn("Bearer_anyToken").when(request).getHeader("Authorization_refresh");

        String token = refreshTokenServiceImpl.resolveToken(request);

        assertThat(token).isEqualTo("anyToken");
    }

    @Test
    public void resolveToken_shouldThrowsHttpServletRequestNotFoundException(){
        assertThrows(IllegalArgumentException.class, () -> refreshTokenServiceImpl.resolveToken(null));
    }

    @Test
    public void resolveToken_shouldThrowsTokenNotFoundException(){
        RuntimeException e = assertThrows(TokenNotFoundException.class, () -> refreshTokenServiceImpl.resolveToken(request));
        assertThat(e.getMessage()).isEqualTo("Authorization_refresh header not found");
    }

    @Test
    public void resolveToken_shouldThrowsTokenNotFoundExceptionWithAnotherException(){
        doReturn("anyToken").when(request).getHeader("Authorization_refresh");

        RuntimeException e = assertThrows(TokenNotFoundException.class, () -> refreshTokenServiceImpl.resolveToken(request));
        assertThat(e.getMessage()).isEqualTo("Refresh token should starts with Bearer_");
    }

    @Test
    public void updateRefreshToken_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> refreshTokenServiceImpl.createAndGetToken(user));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(user, request, response, false, false));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowsHttpServletRequestNotFoundException(){
        doReturn(true).when(userService).isValid(user);

        assertThrows(IllegalArgumentException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(user, null, response, false, false));
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldThrowsHttpServletResponseNotFoundException(){
        doReturn(true).when(userService).isValid(user);

        assertThrows(IllegalArgumentException.class,
                () -> refreshTokenServiceImpl.updateAccessAndRefreshTokens(user, request, null, false, false));
    }
}

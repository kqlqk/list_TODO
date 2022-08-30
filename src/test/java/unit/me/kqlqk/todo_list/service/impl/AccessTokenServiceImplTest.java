package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.AccessTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceImplTest {
    @InjectMocks
    private AccessTokenServiceImpl accessTokenService;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @Mock
    private HttpServletRequest request;

    @Test
    public void createToken_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> accessTokenService.createToken("anyEmail"));
    }

    @Test
    public void getEmail_shouldThrowsTokenNotFoundException(){
        assertThrows(TokenNotFoundException.class, () ->accessTokenService.getEmail(null));
    }

    @Test
    public void getEmail_shouldThrowsTokenNotValidException(){
        assertThrows(TokenNotValidException.class, () ->accessTokenService.getEmail("anyEmail"));
    }

    @Test
    public void resolveToken_shouldReturnsToken(){
        doReturn("Bearer_anyToken").when(request).getHeader("Authorization_access");

        String token = accessTokenService.resolveToken(request);

        assertThat(token).isEqualTo("anyToken");
    }

    @Test
    public void resolveToken_shouldThrowsHttpServletRequestNotFoundException(){
        assertThrows(IllegalArgumentException.class, () -> accessTokenService.resolveToken(null));
    }

    @Test
    public void resolveToken_shouldThrowsTokenNotFoundException(){
       RuntimeException e = assertThrows(TokenNotFoundException.class, () -> accessTokenService.resolveToken(request));
       assertThat(e.getMessage()).isEqualTo("Authorization_access header not found");
    }

    @Test
    public void resolveToken_shouldThrowsTokenNotFoundExceptionWithAnotherException(){
        doReturn("anyToken").when(request).getHeader("Authorization_access");

        RuntimeException e = assertThrows(TokenNotFoundException.class, () -> accessTokenService.resolveToken(request));
        assertThat(e.getMessage()).isEqualTo("Access token should starts with Bearer_");
    }

    @Test
    public void validateToken_shouldValidatesToken(){
        assertThat(accessTokenService.isValid(null)).isFalse();
        assertThat(accessTokenService.isValid("anyToken")).isFalse();
    }
}

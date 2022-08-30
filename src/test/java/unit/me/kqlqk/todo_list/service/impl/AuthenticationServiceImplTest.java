package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserDetails userDetails;

    @Test
    public void setAuthentication_shouldThrowNPE() {
        assertThrows(IllegalArgumentException.class, () -> authenticationService.setAuthentication((String) null));
        assertThrows(IllegalArgumentException.class, () -> authenticationService.setAuthentication((Authentication) null));
    }


    @Test
    public void getUsernamePasswordAuthenticationToken_shouldThrowNPE() {
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.getUsernamePasswordAuthenticationToken(null, "any"));
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.getUsernamePasswordAuthenticationToken(userDetails, null));
    }

    @Test
    public void getUsernamePasswordAuthenticationTokenWithoutCredentials_shouldThrowNPE() {
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.getUsernamePasswordAuthenticationTokenWithoutCredentials(null));
    }

    @Test
    public void getAuthenticationFromContext_shouldReturnNull() {
        assertThat(authenticationService.getAuthenticationFromContext()).isNull();
    }
}
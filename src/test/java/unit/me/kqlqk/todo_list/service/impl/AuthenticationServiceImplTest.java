package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import unit.me.kqlqk.todo_list.service.UnitServiceParent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthenticationServiceImplTest extends UnitServiceParent {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void setAuthentication_shouldThrowNPE() {
        assertThrows(NullPointerException.class, () -> authenticationService.setAuthentication((UserDetails) null));
        assertThrows(NullPointerException.class, () -> authenticationService.setAuthentication((Authentication) null));
    }


    @Test
    public void getAuthentication_shouldThrowNPE() {
        assertThrows(NullPointerException.class, () -> authenticationService.getAuthentication((UserDetails) null));
    }

    @Test
    public void getAuthenticationFromContext_shouldReturnNull() {
        assertThat(authenticationService.getAuthenticationFromContext()).isNull();
    }
}
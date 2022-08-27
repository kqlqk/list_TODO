package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.service.IntegrationServiceParent;
import me.kqlqk.todo_list.service.impl.AuthenticationServiceImpl;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationServiceImplTest extends IntegrationServiceParent {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Test
    public void setAuthentication_shouldSetAuthToContext() {
        authenticationService.setAuthentication("user@mail.com");

        assertThat(authenticationService.getAuthenticationFromContext()).isNotNull();
        assertThat(authenticationService.getAuthenticationFromContext().getName()).isEqualTo("user@mail.com");
    }

    @Test
    public void setAuthentication_shouldSetAuthToContext2() {
        Authentication auth =
                authenticationService.getUsernamePasswordAuthenticationTokenWithoutCredentials("user@mail.com");

        authenticationService.setAuthentication(auth);

        assertThat(authenticationService.getAuthenticationFromContext()).isNotNull();
        assertThat(authenticationService.getAuthenticationFromContext().getName()).isEqualTo("user@mail.com");
    }

    @Test
    public void getUsernamePasswordAuthenticationToken_shouldReturnToken() {
        Authentication authentication =
                authenticationService.getUsernamePasswordAuthenticationToken(
                        userDetailsService.loadUserByUsername("user@mail.com"), "userPswd1");

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@mail.com");
        assertThat(authentication.getCredentials()).isEqualTo("userPswd1");
    }

    @Test
    public void getUsernamePasswordAuthenticationTokenWithoutCredentials_shouldReturnToken() {
        Authentication authentication =
                authenticationService.getUsernamePasswordAuthenticationTokenWithoutCredentials("user@mail.com");

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@mail.com");
        assertThat(authentication.getCredentials()).isNull();
    }

    @Test
    public void getAuthenticationFromContext() {
        authenticationService.setAuthentication("user@mail.com");

        Authentication authentication = authenticationService.getAuthenticationFromContext();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@mail.com");
    }
}
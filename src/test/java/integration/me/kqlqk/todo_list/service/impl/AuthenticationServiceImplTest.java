package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.service.IntegrationServiceParent;
import me.kqlqk.todo_list.service.impl.AuthenticationServiceImpl;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationServiceImplTest extends IntegrationServiceParent {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Test
    public void setAuthentication_shouldSetAuthToContext() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@mail.com");

        authenticationService.setAuthentication(userDetails);

        assertThat(authenticationService.getAuthenticationFromContext()).isNotNull();
        assertThat(authenticationService.getAuthenticationFromContext().getName()).isEqualTo("user@mail.com");
    }

    @Test
    public void setAuthentication_shouldSetAuthToContext2() {
        Authentication auth = authenticationService.getAuthentication(userDetailsService.loadUserByUsername("user@mail.com"));

        authenticationService.setAuthentication(auth);

        assertThat(authenticationService.getAuthenticationFromContext()).isNotNull();
        assertThat(authenticationService.getAuthenticationFromContext().getName()).isEqualTo("user@mail.com");
    }

    @Test
    public void getAuthentication() {
        Authentication authentication =
                authenticationService.getAuthentication(userDetailsService.loadUserByUsername("user@mail.com"));

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@mail.com");
    }

    @Test
    public void getAuthenticationFromContext() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@mail.com");
        authenticationService.setAuthentication(userDetails);

        Authentication authentication = authenticationService.getAuthenticationFromContext();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user@mail.com");
    }
}
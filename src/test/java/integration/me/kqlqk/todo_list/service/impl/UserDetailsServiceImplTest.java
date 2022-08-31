package integration.me.kqlqk.todo_list.service.impl;

import annotations.TestService;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestService
public class UserDetailsServiceImplTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Test
    public void loadUserByUsername_shouldReturnValidUserDetails() {
        assertThat(userDetailsService.loadUserByUsername("userLogin").toString()).isEqualTo(
                "org.springframework.security.core.userdetails.User" +
                        " [Username=user@mail.com, Password=[PROTECTED]," +
                        " Enabled=true," +
                        " AccountNonExpired=true," +
                        " credentialsNonExpired=true," +
                        " AccountNonLocked=true," +
                        " Granted Authorities=[ROLE_USER]]");
    }
}
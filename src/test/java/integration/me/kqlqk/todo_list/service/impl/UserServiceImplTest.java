package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.IntegrationServiceParent;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.AuthenticationService;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import me.kqlqk.todo_list.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceImplTest extends IntegrationServiceParent {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @Transactional
    public void getById_shouldReturnNull(){
        assertThat(userService.getById(99)).isNull();
    }

    @Test
    public void add_shouldAddUserToDb(){
        User user = new User();
        user.setEmail("random@mail.com");
        user.setLogin("randomLogin");
        user.setPassword("randomPswd1");

        userService.add(user);

        assertThat(user.getId()).isGreaterThan(0);
        assertThat(user.getPassword()).isNotEqualTo("randomPswd1");
    }

    @Test
    public void getByLoginObj_shouldReturnsUser(){
        assertThat(userService.getByLoginObj("userLogin").getEmail()).isEqualTo("user@mail.com");
        assertThat(userService.getByLoginObj("user@mail.com").getLogin()).isEqualTo("userLogin");
    }

    @Test
    public void getCurrentEmail_shouldGetEmailFromAuth(){
        authenticationService.setAuthentication("user@mail.com");

        assertThat(userService.getCurrentEmail()).isEqualTo("user@mail.com");
    }
}

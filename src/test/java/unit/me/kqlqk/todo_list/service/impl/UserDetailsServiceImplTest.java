package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private Role role;

    @Test
    public void loadUserByUsername_shouldReturnsValidSpringSecurityUser(){
        doReturn(user).when(userRepository).findByEmail("anyLoginObj");
        doReturn(role).when(user).getRole();
        doReturn("anyRole").when(role).getName();
        doReturn("anyEmail").when(user).getEmail();
        doReturn("anyPswd").when(user).getPassword();

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("anyLoginObj");

        assertThat(userDetails).isInstanceOf(org.springframework.security.core.userdetails.User.class);
        assertThat(userDetails.getUsername()).isEqualTo("anyEmail");
        assertThat(userDetails.getPassword()).isEqualTo("anyPswd");
    }

    @Test
    public void loadUserByUsername_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(null));
        assertThrows(UserNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("anyLoginObj"));
    }
}
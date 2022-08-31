package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.AuthenticationService;
import me.kqlqk.todo_list.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private User user;
    
    @Mock
    private RefreshToken refreshToken;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;


    @Test
    public void getByEmail_shouldSetsEmailInLowerCaseAndCallUserRepository(){
        String email = "anyEmail";

        userServiceImpl.getByEmail(email);

        verify(userRepository, times(1)).getByEmail(email.toLowerCase());
    }

    @Test
    public void getByEmail_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByEmail(null));
    }

    @Test
    public void getById_shouldCallsUserRepository(){
        doReturn(user).when(userRepository).getById(10L);

        userServiceImpl.getById(10L);

        verify(userRepository, times(1)).getById(10L);
    }

    @Test
    public void getByLogin_shouldCallsUserRepository(){
        String login = "anyLogin";

        userServiceImpl.getByLogin(login);

        verify(userRepository, times(1)).getByLogin(login);
    }

    @Test
    public void getByLogin_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByLogin(null));
    }

    @Test
    public void getByRefreshToken_shouldCallsUserRepository(){
        userServiceImpl.getByRefreshToken(refreshToken);

        verify(userRepository, times(1)).getByRefreshToken(refreshToken);
    }

    @Test
    public void getByRefreshToken_shouldThrowsTokenNotFoundException(){
        assertThrows(TokenNotFoundException.class, () -> userServiceImpl.getByRefreshToken(null));
    }

    @Test
    public void existsByEmail_shouldCallsUserRepository(){
        String email = "anyEmail";

        userServiceImpl.existsByEmail(email);

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void existsById_shouldCallsUserRepository(){
        userServiceImpl.existsById(10L);

        verify(userRepository, times(1)).existsById(10L);
    }

    @Test
    public void existsByLogin_shouldCallsUserRepository(){
        String login = "anyLogin";

        userServiceImpl.existsByLogin(login);

        verify(userRepository, times(1)).existsByLogin(login);
    }

    @Test
    public void add_shouldCallsUserRepository(){
        doReturn("anyEmail").when(user).getEmail();

        userServiceImpl.add(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void add_shouldThrowsUserAlreadyExistsException(){
        doReturn("anyEmail").when(user).getEmail();
        doReturn(user).when(userRepository).getByEmail("anyemail");

        assertThrows(UserAlreadyExistsException.class, () -> userServiceImpl.add(user));
    }

    @Test
    public void getByLoginObj_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByLoginObj(null));
    }

    @Test
    public void update_shouldCallsUserRepository(){
        doReturn("anyEmail").when(user).getEmail();
        doReturn(user).when(userRepository).getByEmail("anyemail");

        userServiceImpl.update(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void update_shouldThrowUserAlreadyExistsException(){
        doReturn("anyEmail").when(user).getEmail();

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.update(user));
    }

    @Test
    public void getCurrentEmail_shouldCallAuthenticationService(){
        doReturn(authentication).when(authenticationService).getAuthenticationFromContext();

        userServiceImpl.getCurrentEmail();

        verify(authenticationService, times(1)).getAuthenticationFromContext();
    }

    @Test
    public void isValid_shouldCheckIfValid(){
        assertThat(userServiceImpl.isValid(null)).isFalse();

        doReturn(true).when(userRepository).existsById(10L);
        doReturn(10L).when(user).getId();
        doReturn("anyEMail").when(user).getEmail();
        doReturn("anyLogin").when(user).getLogin();


        assertThat(userServiceImpl.isValid(user)).isTrue();

        doReturn(null).when(user).getEmail();
        assertThat(userServiceImpl.isValid(user)).isFalse();
    }

}
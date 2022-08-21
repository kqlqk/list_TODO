package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
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


    @Test
    public void getByEmail_shouldSetEmailInLowerCaseAndCallUserRepository(){
        String email = "anyEmail";

        userServiceImpl.getByEmail(email);

        verify(userRepository, times(1)).getByEmail(email.toLowerCase());
    }

    @Test
    public void getByEmail_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByEmail(null));
    }

    @Test
    public void getById_shouldCallUserRepository(){
        userServiceImpl.getById(10L);

        verify(userRepository, times(1)).getById(10L);
    }

    @Test
    public void getByLogin_shouldCallUserRepository(){
        String login = "anyLogin";

        userServiceImpl.getByLogin(login);

        verify(userRepository, times(1)).getByLogin(login);
    }

    @Test
    public void getByLogin_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByLogin(null));
    }

    @Test
    public void getByRefreshToken_shouldCallUserRepository(){
        userServiceImpl.getByRefreshToken(refreshToken);

        verify(userRepository, times(1)).getByRefreshToken(refreshToken);
    }

    @Test
    public void getByRefreshToken_shouldThrowTokenNotFoundException(){
        assertThrows(TokenNotFoundException.class, () -> userServiceImpl.getByRefreshToken(null));
    }

    @Test
    public void existsByEmail_shouldCallUserRepository(){
        String email = "anyEmail";

        userServiceImpl.existsByEmail(email);

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void existsById_shouldCallUserRepository(){
        userServiceImpl.existsById(10L);

        verify(userRepository, times(1)).existsById(10L);
    }

    @Test
    public void existsByLogin_shouldCallUserRepository(){
        String login = "anyLogin";

        userServiceImpl.existsByLogin(login);

        verify(userRepository, times(1)).existsByLogin(login);
    }

    @Test
    public void add_shouldCallUserRepository(){
        doReturn("anyEmail").when(user).getEmail();

        userServiceImpl.add(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void add_shouldThrowUserAlreadyExistsException(){
        doReturn("anyEmail").when(user).getEmail();
        doReturn(user).when(userRepository).getByEmail("anyemail");

        assertThrows(UserAlreadyExistsException.class, () -> userServiceImpl.add(user));
    }

    @Test
    public void getByLoginObj_shouldThrowUserNotFoundException(){
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByLoginObj(null));
    }

    @Test
    public void update_shouldCallUserRepository(){
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
    public void isValid_test(){
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
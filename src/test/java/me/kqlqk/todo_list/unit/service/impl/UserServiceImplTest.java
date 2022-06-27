package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.exceptions.UserAlreadyExistException;
import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import me.kqlqk.todo_list.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private static UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private static Role role;

    private static User user;


    @BeforeAll
    public static void setUp(){
        user = new User("testMail@todo.list", "testLogin", "testPSWD", new Role("USER"));
    }

    @Test
    public void add_shouldSaveUser() {
        System.out.println(user);
        userServiceImpl.add(user);

        assertNotNull(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void add_shouldThrowNewUserAlreadyExistException() {
        when(userRepository.existsByEmail("testMail@todo.list")).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userServiceImpl.add(user));

        assertEquals("User already exist", exception.getMessage());
    }


    @Test
    public void getByLoginObj_shouldReturnNull() {
        assertNull(userServiceImpl.getByEmail(null));
    }

    @Test
    public void update_shouldUpdateUser(){
        userServiceImpl.update(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void convertOAuth2UserToUserAndSave_shouldConvertOAuth2UserToUserAndSave(){
        OAuth2User oAuth2User = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                Map<String, Object> res = new HashMap<>();
                res.put("email", "test@todo.list");
                res.put("name", "John");
                return res;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };

        User localUser = userServiceImpl.convertOAuth2UserToUserAndSave(oAuth2User);

        assertEquals("test@todo.list", localUser.getEmail());
        assertEquals("John", localUser.getLogin());

        verify(userRepository, times(1)).save(localUser);
    }

}
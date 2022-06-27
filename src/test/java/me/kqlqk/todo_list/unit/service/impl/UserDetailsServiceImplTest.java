package me.kqlqk.todo_list.unit.service.impl;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
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
    public void loadUserByUsername_shouldLoadUserByUsername(){
        when(user.getEmail()).thenReturn("test@todo.list");
        when(user.getRole()).thenReturn(role);
        when(user.getPassword()).thenReturn("testPswd");
        when(role.getName()).thenReturn("USER");
        when(userRepository.getByEmail("test")).thenReturn(user);

        System.out.println(user);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("test");

        assertEquals(userDetails.getUsername(), user.getEmail());
        assertEquals(userDetails.getPassword(), user.getPassword());
    }
}
package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.RecoveryController;
import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class RecoveryControllerTest {

    @InjectMocks
    private RecoveryController recoveryController;

    @Mock
    private UserService userService;

    @Mock
    private UserDTO userDTO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private User user;

    @Mock
    private EmailSenderService emailSenderService;

    @Test
    public void sendEmail() {
        when(userDTO.getEmail()).thenReturn("testEmail");
        when(userService.getByEmail("testEmail")).thenReturn(user);

        recoveryController.sendEmail(userDTO, request);

        verify(userService, times(1)).getByEmail(userDTO.getEmail());
    }

}
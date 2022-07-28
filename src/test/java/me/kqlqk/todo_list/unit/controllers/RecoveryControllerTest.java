package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.RecoveryController;
import me.kqlqk.todo_list.dto.daoDTOs.UserDTO;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class RecoveryControllerTest {

    @InjectMocks
    private RecoveryController recoveryController;

    @Mock
    private UserService userService;

    @Mock
    private UserDTO userValid;

    @Mock
    private me.kqlqk.todo_list.models.User user;

    @Mock
    private EmailSenderService emailSenderService;

    @Test
    public void sendEmail() {
        when(userValid.getEmail()).thenReturn("testEmail");
        when(userService.getByEmail("testEmail")).thenReturn(user);

        //recoveryController.sendEmail(userValid);

        verify(userService, times(1)).getByEmail(userValid.getEmail());
    }

}
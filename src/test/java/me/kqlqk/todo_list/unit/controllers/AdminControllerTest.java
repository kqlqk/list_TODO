package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.AdminController;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith({MockitoExtension.class})
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;


    @Test
    public void getAdminMenu_shouldMakeAllCalls(){
        adminController.getAdminMenu(model);
        Mockito.verify(userService, Mockito.times(1)).getCurrentEmail();
    }
}
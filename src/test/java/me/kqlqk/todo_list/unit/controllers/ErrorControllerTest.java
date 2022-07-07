package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.ErrorController;
import me.kqlqk.todo_list.service.ErrorsHandlerService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ErrorControllerTest {

    @InjectMocks
    private ErrorController errorController;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserService userService;

    @Mock
    private ErrorsHandlerService errorsHandlerService;

    @Test
    public void handleErrors_shouldMakeAllCalls(){

        errorController.handleErrors(model, request);

        verify(userService, times(1)).getCurrentEmail();
        verify(errorsHandlerService, times(1)).getErrorCodeWithDetails(request);
    }
}

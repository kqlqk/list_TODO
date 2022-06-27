package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.ErrorController;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.ui.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

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
    private RequestDispatcher requestDispatcher;

    @Mock
    private Logger logger;

    @Test
    public void handleError_shouldMakeAllCalls(){
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(403);

        errorController.handleError(model, request);

        verify(userService, times(2)).getCurrentEmail();
        verify(userService, times(2)).getCurrentUser();
    }
}

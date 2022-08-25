package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletRequestNotFoundException;
import me.kqlqk.todo_list.service.impl.ErrorsHandlerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unit.me.kqlqk.todo_list.service.UnitServiceParent;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

public class ErrorsHandlerServiceImplTest extends UnitServiceParent {
    @InjectMocks
    private ErrorsHandlerServiceImpl errorsHandlerServiceImpl;

    @Mock
    private HttpServletRequest request;


    @Test
    public void getErrorCode_shouldReturnsErrorCode(){
        doReturn(404).when(request).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int errorCode = errorsHandlerServiceImpl.getErrorCode(request);

        assertThat(errorCode).isEqualTo(404);
    }

    @Test
    public void getErrorCode_shouldThrowsHttpServletRequestNotFoundException() {
        assertThrows(HttpServletRequestNotFoundException.class, () -> errorsHandlerServiceImpl.getErrorCode(null));
    }

    @Test
    public void getErrorCodeWithDetails_shouldReturnsErrorCodeWithDetails() {
        doReturn(404).when(request).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String errorDetails = errorsHandlerServiceImpl.getErrorCodeWithDetails(request);

        assertThat(errorDetails).isEqualTo("Error 404 Page not found");
    }

    @Test
    public void getErrorCodeWithDetails_shouldThrowsHttpServletRequestNotFoundException() {
        assertThrows(HttpServletRequestNotFoundException.class, () -> errorsHandlerServiceImpl.getErrorCodeWithDetails(null));
    }
}
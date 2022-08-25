package me.kqlqk.todo_list.exceptions_handling;

import me.kqlqk.todo_list.dto.ExceptionDTO;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.TokenNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotValidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "me.kqlqk.todo_list.controllers.rest")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestGlobalExceptionHandler {

    @ExceptionHandler({
            NoteNotFoundException.class,
            UserNotFoundException.class,
            NoteNotValidException.class,
            UserNotValidException.class,
            TokenNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleNotFoundAndNotValidExceptions(RuntimeException e){
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setInfo(e.getMessage());
        return exceptionDTO;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleUserAlreadyExistsEx(UserAlreadyExistsException e){
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setInfo(e.getMessage());

        return exceptionDTO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNonValidDataEx(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> errors.put("info", error.getDefaultMessage()));

        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleHttpMessageNotReadableEx(){
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setInfo("Required request body is missing");

        return exceptionDTO;
    }

}

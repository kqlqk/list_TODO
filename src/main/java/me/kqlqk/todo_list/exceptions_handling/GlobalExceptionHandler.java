package me.kqlqk.todo_list.exceptions_handling;

import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.token.TokenNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Represents exception handler for endpoints
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler({NoteNotFoundException.class, TokenNotFoundException.class})
    public String handleNoteNotFoundEx() {
        return "redirect:/home";
    }


}

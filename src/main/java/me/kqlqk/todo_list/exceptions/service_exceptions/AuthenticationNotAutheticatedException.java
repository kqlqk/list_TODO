package me.kqlqk.todo_list.exceptions.service_exceptions;

import me.kqlqk.todo_list.exceptions.ServiceException;

public class AuthenticationNotAutheticatedException extends ServiceException {

    public AuthenticationNotAutheticatedException() {
    }

    public AuthenticationNotAutheticatedException(String message) {
        super(message);
    }

    public AuthenticationNotAutheticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

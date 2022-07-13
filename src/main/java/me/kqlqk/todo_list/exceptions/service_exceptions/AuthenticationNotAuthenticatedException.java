package me.kqlqk.todo_list.exceptions.service_exceptions;

public class AuthenticationNotAuthenticatedException extends RuntimeException {

    public AuthenticationNotAuthenticatedException() {
    }

    public AuthenticationNotAuthenticatedException(String message) {
        super(message);
    }

    public AuthenticationNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

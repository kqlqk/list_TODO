package me.kqlqk.todo_list.exceptions_handling.exceptions.security;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}

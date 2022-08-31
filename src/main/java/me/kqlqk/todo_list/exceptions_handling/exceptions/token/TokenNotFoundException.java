package me.kqlqk.todo_list.exceptions_handling.exceptions.token;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}

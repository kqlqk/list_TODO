package me.kqlqk.todo_list.exceptions_handling.exceptions.security;

public class TokenIsNotValidException extends RuntimeException{
    public TokenIsNotValidException() {
    }

    public TokenIsNotValidException(String message) {
        super(message);
    }
}

package me.kqlqk.todo_list.exceptions_handling.exceptions.security;

public class TokenNotValidException extends RuntimeException{
    public TokenNotValidException(String message) {
        super(message);
    }
}

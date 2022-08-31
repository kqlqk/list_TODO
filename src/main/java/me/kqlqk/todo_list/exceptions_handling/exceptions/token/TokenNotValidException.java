package me.kqlqk.todo_list.exceptions_handling.exceptions.token;

public class TokenNotValidException extends RuntimeException{
    public TokenNotValidException(String message) {
        super(message);
    }
}

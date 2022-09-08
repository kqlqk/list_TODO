package me.kqlqk.todo_list.exceptions_handling.exceptions.token;

/**
 * Represents exception for access token OR {@link me.kqlqk.todo_list.models.RefreshToken}
 */
public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String message) {
        super(message);
    }
}

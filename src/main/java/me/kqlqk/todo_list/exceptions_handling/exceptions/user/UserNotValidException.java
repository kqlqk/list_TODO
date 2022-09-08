package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

/**
 * Represents exception for {@link me.kqlqk.todo_list.models.User}
 */
public class UserNotValidException extends RuntimeException {
    public UserNotValidException(String message) {
        super(message);
    }
}

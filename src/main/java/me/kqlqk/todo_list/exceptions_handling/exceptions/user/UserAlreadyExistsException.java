package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

/**
 * Represents exception for {@link me.kqlqk.todo_list.models.User}
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

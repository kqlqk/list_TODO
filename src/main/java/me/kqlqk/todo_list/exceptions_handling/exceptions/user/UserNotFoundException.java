package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

/**
 * Represents exception for {@link me.kqlqk.todo_list.models.User}
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

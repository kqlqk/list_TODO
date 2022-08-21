package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

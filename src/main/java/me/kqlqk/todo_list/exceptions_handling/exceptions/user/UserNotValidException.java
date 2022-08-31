package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

public class UserNotValidException extends RuntimeException{
    public UserNotValidException(String message) {
        super(message);
    }
}

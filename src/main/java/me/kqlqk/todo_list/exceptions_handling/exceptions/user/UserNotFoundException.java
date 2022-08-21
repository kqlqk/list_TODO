package me.kqlqk.todo_list.exceptions_handling.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}

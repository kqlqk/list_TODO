package me.kqlqk.todo_list.exceptions.dao.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
    }

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

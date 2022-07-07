package me.kqlqk.todo_list.exceptions.dao_exceptions.user_exceptions;

import me.kqlqk.todo_list.exceptions.dao_exceptions.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(){
    }

    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

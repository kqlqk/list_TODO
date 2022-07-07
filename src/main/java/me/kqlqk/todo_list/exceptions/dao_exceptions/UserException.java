package me.kqlqk.todo_list.exceptions.dao_exceptions;

import me.kqlqk.todo_list.exceptions.DAOException;

public class UserException extends DAOException {
    public UserException(){
    }

    public UserException(String message){
        super(message);
    }

    public UserException(String message, Throwable cause){
        super(message, cause);
    }
}

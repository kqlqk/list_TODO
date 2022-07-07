package me.kqlqk.todo_list.exceptions.dao_exceptions.user_exceptions;

import me.kqlqk.todo_list.exceptions.dao_exceptions.UserException;
import me.kqlqk.todo_list.exceptions.dao_exceptions.user_exceptions.status.UserStatus;

public class UserAlreadyExistException extends UserException {
    private UserStatus userStatus;


    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(String msg) {
        super(msg);
    }

    public UserAlreadyExistException(String msg, UserStatus userStatus){
        super(msg);
        this.userStatus = userStatus;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
}

package me.kqlqk.todo_list.exceptions;

import me.kqlqk.todo_list.exceptions.status.UserStatus;

public class UserAlreadyExistException extends RuntimeException{
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

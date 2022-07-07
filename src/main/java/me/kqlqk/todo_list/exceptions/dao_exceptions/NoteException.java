package me.kqlqk.todo_list.exceptions.dao_exceptions;

import me.kqlqk.todo_list.exceptions.DAOException;

public class NoteException extends DAOException {

    public NoteException(){
    }

    public NoteException(String message){
        super(message);
    }

    public NoteException(String message, Throwable cause){
        super(message, cause);
    }
}

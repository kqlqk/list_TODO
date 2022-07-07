package me.kqlqk.todo_list.exceptions.dao_exceptions.note_exceptions;

import me.kqlqk.todo_list.exceptions.dao_exceptions.NoteException;

public class NoteAlreadyExistException extends NoteException {

    public NoteAlreadyExistException(){
    }

    public NoteAlreadyExistException(String message){
        super(message);
    }

    public NoteAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }
}

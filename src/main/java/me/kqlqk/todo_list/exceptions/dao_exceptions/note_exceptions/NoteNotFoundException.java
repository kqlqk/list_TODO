package me.kqlqk.todo_list.exceptions.dao_exceptions.note_exceptions;

import me.kqlqk.todo_list.exceptions.dao_exceptions.NoteException;

public class NoteNotFoundException extends NoteException {

    public NoteNotFoundException(){
    }

    public NoteNotFoundException(String message){
        super(message);
    }

    public NoteNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

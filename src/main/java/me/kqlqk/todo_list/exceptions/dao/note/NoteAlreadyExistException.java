package me.kqlqk.todo_list.exceptions.dao.note;

public class NoteAlreadyExistException extends RuntimeException {

    public NoteAlreadyExistException(){
    }

    public NoteAlreadyExistException(String message){
        super(message);
    }

    public NoteAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }
}

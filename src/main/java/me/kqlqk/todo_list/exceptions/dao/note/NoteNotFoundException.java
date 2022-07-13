package me.kqlqk.todo_list.exceptions.dao.note;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(){
    }

    public NoteNotFoundException(String message){
        super(message);
    }

    public NoteNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

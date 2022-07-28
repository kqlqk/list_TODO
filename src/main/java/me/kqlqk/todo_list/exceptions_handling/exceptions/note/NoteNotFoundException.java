package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

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

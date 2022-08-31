package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message){
        super(message);
    }
}

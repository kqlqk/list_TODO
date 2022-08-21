package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

public class NoteNotValidException extends RuntimeException{
    public NoteNotValidException(String message) {
        super(message);
    }
}

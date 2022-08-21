package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

public class NoteAlreadyExistsException extends RuntimeException {
    public NoteAlreadyExistsException(String message) {
        super(message);
    }
}

package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

/**
 * Represents exception for {@link me.kqlqk.todo_list.models.Note}
 */
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}

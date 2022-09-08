package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

/**
 * Represents exception for {@link me.kqlqk.todo_list.models.Note}
 */
public class NoteAlreadyExistsException extends RuntimeException {
    public NoteAlreadyExistsException(String message) {
        super(message);
    }
}

package me.kqlqk.todo_list.exceptions_handling.exceptions.note;

public class NoteAlreadyExists extends RuntimeException {
    public NoteAlreadyExists() {
    }

    public NoteAlreadyExists(String message) {
        super(message);
    }
}

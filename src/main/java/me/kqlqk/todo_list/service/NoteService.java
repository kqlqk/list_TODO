package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents service-layer for {@link me.kqlqk.todo_list.models.Note}
 */
@Component
public interface NoteService {
    Note getById(long noteId);

    List<Note> getByUser(User user);

    boolean existsById(long id);

    boolean existsForUser(User user, long noteId);

    /**
     * Adds new {@link me.kqlqk.todo_list.models.Note} to db
     */
    void add(Note note);

    /**
     * Delete {@link me.kqlqk.todo_list.models.Note} from db
     */
    void delete(Note note);

    /**
     * Deletes {@link me.kqlqk.todo_list.models.Note} from db
     */
    void delete(long id);

    /**
     * Updates {@link me.kqlqk.todo_list.models.Note} in db
     */
    void update(Note note);

    boolean isValid(Note note);

    boolean isValid(long noteId);
}

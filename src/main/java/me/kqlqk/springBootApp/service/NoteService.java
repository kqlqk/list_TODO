package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NoteService{
    void add(Note note);
    void delete(Note note);
    void delete(long id);
    boolean existsById(long id);
    boolean existsForUser(User user, long noteId);
    Note getById(long id);
    List<Note> getByUser(User user);
    List<Note> getByUserId(long userId);
    void update(Note note);
}

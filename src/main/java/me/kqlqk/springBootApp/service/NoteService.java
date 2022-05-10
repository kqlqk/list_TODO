package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NoteService{
    Note getById(long id);
    List<Note> getByUser(User user);
    List<Note> getByUserId(long userId);
    boolean existsById(long id);


    void add(Note note);
    void delete(Note note);
    void delete(long id);
    boolean existsForUser(User user, long noteId);
    void update(Note note);
}

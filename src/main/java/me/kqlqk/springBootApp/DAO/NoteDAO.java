package me.kqlqk.springBootApp.DAO;

import me.kqlqk.springBootApp.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteDAO {
    List<Note> getNotes();
    Note getNote(int id);
    void saveNote(Note note);
    boolean existsById(int id);
    void update(Note note);
    void deleteNote(int id);

}

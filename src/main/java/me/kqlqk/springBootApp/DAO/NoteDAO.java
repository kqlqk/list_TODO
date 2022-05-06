package me.kqlqk.springBootApp.DAO;

import me.kqlqk.springBootApp.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteDAO extends JpaRepository<Note, Long> {
    Note getById(long id);

}

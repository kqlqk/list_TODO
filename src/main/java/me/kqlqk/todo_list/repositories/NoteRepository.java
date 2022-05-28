package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note getById(long id);
    List<Note> getByUser(User user);
    List<Note> getByUserId(long id);
    boolean existsById(long id);
}

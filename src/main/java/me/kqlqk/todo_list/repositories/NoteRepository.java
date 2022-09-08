package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Represents JPA repository for {@link me.kqlqk.todo_list.models.Note}
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findById(long id);

    List<Note> findByUser(User user);

    boolean existsById(long id);
}

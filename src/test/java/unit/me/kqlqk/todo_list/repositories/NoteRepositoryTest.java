package unit.me.kqlqk.todo_list.repositories;

import annotations.TestRepository;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestRepository
public class NoteRepositoryTest{
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void getById_shouldReturnValidNote(){
        Note note = noteRepository.getById(1);

        assertThat(note).isNotNull();
        assertThat(note.getTitle()).isEqualTo("anyTitle");
        assertThat(note.getFullTitle()).isEqualTo("anyTitle");
        assertThat(note.getUser().getId()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void getById_shouldThrowsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () -> noteRepository.getById(99).getTitle());
    }

    @Test
    public void getByUser_shouldReturnValidNote(){
        User user = userRepository.getById(1);

        List<Note> notes = noteRepository.getByUser(user);

        assertThat(notes).hasSize(2);
        assertThat(notes.get(0)).isInstanceOf(Note.class);
        assertThat(notes.get(1)).isInstanceOf(Note.class);
        assertThat(notes.get(0).getTitle()).isEqualTo("anyTitle");
        assertThat(notes.get(0).getFullTitle()).isEqualTo("anyTitle");
        assertThat(notes.get(0).getUser().getId()).isEqualTo(1);
    }

    @Test
    public void getByUser_shouldReturnEmptyList(){
        assertThat(noteRepository.getByUser(null)).isEmpty();
    }

    @Test
    public void existsById_shouldCheckIfExistsById(){
        assertThat(noteRepository.existsById(1)).isTrue();
        assertThat(noteRepository.existsById(99)).isFalse();
    }
}

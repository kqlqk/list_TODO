package unit.me.kqlqk.todo_list.repositories;

import annotations.TestRepository;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestRepository
public class NoteRepositoryTest{
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById_shouldReturnValidNote(){
        Note note = noteRepository.findById(1);

        assertThat(note).isNotNull();
        assertThat(note.getTitle()).isEqualTo("anyTitle");
        assertThat(note.getFullTitle()).isEqualTo("anyTitle");
        assertThat(note.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void findById_shouldReturnNull(){
        assertThat(noteRepository.findById(99)).isNull();
    }

    @Test
    public void findByUser_shouldReturnValidNote(){
        User user = userRepository.findById(1);

        List<Note> notes = noteRepository.findByUser(user);

        assertThat(notes).hasSize(2);
        assertThat(notes.get(0)).isInstanceOf(Note.class);
        assertThat(notes.get(1)).isInstanceOf(Note.class);
        assertThat(notes.get(0).getTitle()).isEqualTo("anyTitle");
        assertThat(notes.get(0).getFullTitle()).isEqualTo("anyTitle");
        assertThat(notes.get(0).getUser().getId()).isEqualTo(1);
    }

    @Test
    public void findByUser_shouldReturnEmptyList(){
        assertThat(noteRepository.findByUser(null)).isEmpty();
    }

    @Test
    public void existsById_shouldCheckIfExistsById(){
        assertThat(noteRepository.existsById(1)).isTrue();
        assertThat(noteRepository.existsById(99)).isFalse();
    }
}

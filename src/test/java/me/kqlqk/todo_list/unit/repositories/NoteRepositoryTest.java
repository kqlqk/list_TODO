package me.kqlqk.todo_list.unit.repositories;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/boot-up-h2-db.sql", "/add-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NoteRepositoryTest {
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

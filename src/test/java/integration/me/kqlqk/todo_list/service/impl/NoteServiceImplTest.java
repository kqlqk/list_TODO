package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.IntegrationServiceParent;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

public class NoteServiceImplTest extends IntegrationServiceParent {

    @Autowired
    private NoteServiceImpl noteService;

    @SpyBean
    private UserService userService;

    @Test
    @Transactional
    public void getById_shouldReturnNull(){
        assertThat(noteService.getById(99)).isNull();
    }

    @Test
    public void add_shouldAddNoteToDB(){
        User user = userService.getByEmail("user@mail.com");
        doReturn(user).when(userService).getCurrentUser();

        Note note = new Note();
        note.setFullTitle("Any title");
        note.setBody("Any body");

        noteService.add(note);

        assertThat(note.getId()).isGreaterThan(0);
        assertThat(note.getTitle()).isEqualTo("Any title");
    }

    @Test
    public void delete_shouldDeleteExistsNote(){
        assertThat(noteService.getById(1)).isNotNull();
        noteService.delete(noteService.getById(1));
        assertThat(noteService.getById(1)).isNull();

        assertThat(noteService.getById(2)).isNotNull();
        noteService.delete(2);
        assertThat(noteService.getById(2)).isNull();
    }

    @Test
    @Transactional
    public void existsForUser_shouldCheckIfExistsForUser(){
        assertThat(noteService.existsForUser(userService.getById(1), 1)).isTrue();
        assertThat(noteService.existsForUser(userService.getById(1), 3)).isFalse();
    }

    @Test
    public void update_shouldUpdateNote(){
        Note note = noteService.getById(1);
        note.setFullTitle("newAnyTitle");

        noteService.update(note);

        assertThat(noteService.getById(1).getTitle()).isEqualTo("newAnyTitle");
    }


}

package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotValidException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @Mock
    private Note note;

    @Mock
    private NoteRepository noteRepository;

    @Test
    public void getById_shouldCallsNoteRepository(){
        doReturn(note).when(noteRepository).findById(10L);
        noteServiceImpl.getById(10L);

        verify(noteRepository, times(1)).findById(10L);
    }


    @Test
    public void getByUser_shouldCallsNoteRepository(){
        doReturn(true).when(userService).isValid(user);

        noteServiceImpl.getByUser(user);

        verify(noteRepository, times(1)).findByUser(user);
    }

    @Test
    public void getByUser_shouldThrowsUserNotValidException(){
        assertThrows(UserNotValidException.class, () -> noteServiceImpl.getByUser(user));
        assertThrows(UserNotValidException.class, () -> noteServiceImpl.getByUser(null));
    }

    @Test
    public void existsByID_shouldCallsNoteRepository(){
        noteServiceImpl.existsById(10L);

        verify(noteRepository, times(1)).existsById(10L);
    }

    @Test
    public void add_titleShouldBeLessThan38Characters(){
        note = spy(new Note());

        String STRING_40_CHARACTERS = "1234567890123456789012345678901234567890";
        String STRING_37_CHARACTERS = "1234567890123456789012345678901234567";

        doReturn(10L).when(note).getId();
        doReturn(STRING_40_CHARACTERS).when(note).getFullTitle();
        doReturn(user).when(userService).getCurrentUser();

        noteServiceImpl.add(note);

        assertThat(note.getTitle()).isEqualTo(STRING_37_CHARACTERS);
    }

    @Test
    public void add_shouldThrowsNoteNotValidException(){
        assertThrows(NoteNotValidException.class, () -> noteServiceImpl.add(note));
    }

    @Test
    public void add_shouldThrowsNoteAlreadyExistsException(){
        doReturn("anyString").when(note).getFullTitle();
        doReturn(10L).when(note).getId();

        doReturn(true).when(noteRepository).existsById(10L);

        assertThrows(NoteAlreadyExistsException.class, () -> noteServiceImpl.add(note));
    }

    @Test
    public void delete_shouldThrowsNoteNotFoundException(){
        assertThrows(NoteNotValidException.class, () -> noteServiceImpl.delete(note));
        assertThrows(NoteNotValidException.class, () -> noteServiceImpl.delete(anyLong()));
    }

    @Test
    public void existsForUser_shouldThrowsUserNotFoundException(){
        assertThrows(UserNotValidException.class, () -> noteServiceImpl.existsForUser(user, 10L));
    }

    @Test
    public void existsForUser_shouldThrowsNoteNotFoundException(){
        doReturn(true).when(userService).isValid(user);

        assertThrows(NoteNotValidException.class, () -> noteServiceImpl.existsForUser(user, 0));
    }

    @Test
    public void update_titleShouldBeLessThan38Characters(){
        note = spy(new Note());
        note.setId(10L);
        note.setFullTitle("anyTitle");
        note.setUser(user);

        String STRING_40_CHARACTERS = "1234567890123456789012345678901234567890";
        String STRING_37_CHARACTERS = "1234567890123456789012345678901234567";

        doReturn(true).when(noteRepository).existsById(10L);
        doReturn(STRING_40_CHARACTERS).when(note).getFullTitle();

        noteServiceImpl.update(note);

        assertThat(note.getTitle()).isEqualTo(STRING_37_CHARACTERS);
    }

    @Test
    public void update_shouldThrowsNoteNotFoundException(){
        assertThrows(NoteNotValidException.class, () -> noteServiceImpl.update(note));
    }

}
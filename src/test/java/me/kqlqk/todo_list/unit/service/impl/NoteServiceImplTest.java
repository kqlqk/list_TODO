package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteServiceImpl;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserService userService;

    @Mock
    private static Note note;


    @Test
    public void add_shouldSaveNote() {
        when(note.getFullTitle()).thenReturn("test title");

        noteServiceImpl.add(note);

        verify(noteRepository, times(1)).save(note);
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void delete_shouldDeleteANote() {
        when(note.getId()).thenReturn(1L);
        when(noteRepository.existsById(1L)).thenReturn(true);

        noteServiceImpl.delete(note);
        verify(noteRepository, times(1)).existsById(1);
        verify(noteRepository, times(1)).delete(note);
    }

}
package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.HomeMainController;
import me.kqlqk.todo_list.dto.daoDTOs.NoteDTO;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class HomeMainControllerTest {

    @InjectMocks
    private HomeMainController homeMainController;

    @Mock
    private NoteService noteService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Note note;

    @Mock
    private NoteDTO noteDTO;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private User user;

    @Test
    public void showNote_shouldMakeAllCalls() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(noteService.existsById(0)).thenReturn(true);
        when(noteService.existsForUser(userService.getCurrentUser(), 0)).thenReturn(true);

        homeMainController.showNote(0,model);

        verify(userService, times(3)).getCurrentUser();
        verify(userService, times(1)).getCurrentEmail();
        verify(noteService, times(1)).getById(0);
        verify(noteService, times(1)).existsForUser(userService.getCurrentUser(), 0);
        verify(noteService, times(1)).existsById(0);
    }

    @Test
    public void deleteNote_shouldMakeAllCalls() {
        when(noteService.existsForUser(userService.getCurrentUser(), 0)).thenReturn(true);

        homeMainController.deleteNote(0);

        verify(userService, times(2)).getCurrentUser();
        verify(userService, times(1)).getCurrentEmail();
        verify(noteService, times(1)).delete(0);
        verify(noteService, times(1)).existsForUser(userService.getCurrentUser(), 0);
    }

    @Test
    public void createNote_shouldMakeAllCalls() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(noteDTO.convertToNewNote()).thenReturn(note);

        homeMainController.createNote(noteDTO, bindingResult);

        verify(userService, times(1)).getCurrentEmail();
        verify(noteService, times(1)).add(note);
    }

    @Test
    public void saveEditedNote_shouldMakeAllCalls() {
        when(noteDTO.getTitle()).thenReturn("testTitle");
        when(noteDTO.getBody()).thenReturn("testBody");
        when(noteService.getById(0)).thenReturn(note);

        homeMainController.saveEditedNote(0, noteDTO);

        verify(noteService, times(1)).getById(0);
        verify(userService, times(1)).getCurrentEmail();
        verify(noteService, times(1)).update(note);
    }
}
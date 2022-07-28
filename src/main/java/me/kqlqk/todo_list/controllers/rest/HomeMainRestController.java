package me.kqlqk.todo_list.controllers.rest;

import me.kqlqk.todo_list.dto.daoDTOs.NoteDTO;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeMainRestController {
    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public HomeMainRestController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notes")
    public List<NoteDTO> getAllNotesForUser(){
        List<Note> notes = noteService.getByUser(userService.getCurrentUser());
        return NoteDTO.convertListOfNotesToListOfNoteDTOs(notes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notes/{id}")
    public NoteDTO getNoteForUser(@PathVariable long id){
        if(! (noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            throw new NoteNotFoundException("Note with id = " + id + " not found OR note isn't available for current user");
        }
        return new NoteDTO(noteService.getById(id));
    }


    @RequestMapping(method = RequestMethod.POST, value = "/notes")
    public List<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO){
        noteService.add(noteDTO.convertToNewNote());

        return getAllNotesForUser();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/notes/{id}")
    public NoteDTO editNote(@PathVariable long id, @Valid @RequestBody NoteDTO noteDTO){
        if(!noteService.existsById(id)){
            throw new NoteNotFoundException("Note with id = " + id + " not found");
        }
        noteService.update(noteDTO.convertToEditedNote(noteService, id));

        return getNoteForUser(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/notes/{id}")
    public List<NoteDTO> deleteNote(@PathVariable long id){
        if (!noteService.existsById(id) || !noteService.existsForUser(userService.getCurrentUser(), id)){
            throw new NoteNotFoundException("Note with id = " + id + " not found OR note is not for current user");
        }

        noteService.delete(id);

        return getAllNotesForUser();
    }

}

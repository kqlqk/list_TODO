package me.kqlqk.todo_list.controllers.rest;

import me.kqlqk.todo_list.dto.daoDTOs.NoteDTO;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getNoteForUser(@PathVariable long id){
        if((noteService.existsForUser(userService.getCurrentUser(), id))){
            return ResponseEntity.ok(new NoteDTO(noteService.getById(id)));
        }
        return ResponseEntity.notFound().build();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/notes")
    public List<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO){
        noteService.add(noteDTO.convertToNewNote());

        return getAllNotesForUser();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/notes/{id}")
    public ResponseEntity<?> editNote(@PathVariable long id, @Valid @RequestBody NoteDTO noteDTO){
        noteService.update(noteDTO.convertToEditedNote(noteService, id));

        return getNoteForUser(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable long id){
        if (noteService.existsForUser(userService.getCurrentUser(), id)){
            noteService.delete(id);
            return ResponseEntity.ok(getAllNotesForUser());
        }

        return ResponseEntity.notFound().build();

    }

}

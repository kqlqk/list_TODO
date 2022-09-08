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

/**
 * Represents rest endpoints for authorized users
 * <p>
 * For more information of endpoints please visit our <a href="https://github.com/kqlqk/list_TODO#rest-api">github repository</a>
 */
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

    /**
     * Represents <b>"/api/notes" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notes")
    public List<NoteDTO> getAllNotesForUser() {
        List<Note> notes = noteService.getByUser(userService.getCurrentUser());
        return NoteDTO.convertListOfNotesToListOfNoteDTOs(notes);
    }

    /**
     * Represents <b>"/api/notes/{id of note}" [GET]</b> endpoint
     *
     * @return {@link me.kqlqk.todo_list.models.Note} OR json with exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notes/{id}")
    public ResponseEntity<?> getNoteForUser(@PathVariable long id) {
        if (!(noteService.existsForUser(userService.getCurrentUser(), id))) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new NoteDTO(noteService.getById(id)));
    }


    /**
     * Represents <b>"/api/notes" [POST]</b> endpoint
     *
     * @return list of all existing for user {@link me.kqlqk.todo_list.models.Note}
     */
    @RequestMapping(method = RequestMethod.POST, value = "/notes")
    public List<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        noteService.add(noteDTO.convertToNewNote());

        return getAllNotesForUser();
    }

    /**
     * Represents <b>"/api/notes/{id of note}" [PUT]</b> endpoint
     *
     * @return Edited {@link me.kqlqk.todo_list.models.Note}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/notes/{id}")
    public ResponseEntity<?> editNote(@PathVariable long id, @Valid @RequestBody NoteDTO noteDTO) {
        noteService.update(noteDTO.convertToEditedNote(noteService, id));

        return getNoteForUser(id);
    }

    /**
     * Represents <b>"/api/notes/{id of note}" [DELETE]</b> endpoint
     *
     * @return list of all existing for user notes OR json with exception
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable long id) {
        if (!noteService.existsForUser(userService.getCurrentUser(), id)) {
            return ResponseEntity.notFound().build();
        }

        noteService.delete(id);
        return ResponseEntity.ok(getAllNotesForUser());
    }

}

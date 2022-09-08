package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.daoDTOs.NoteDTO;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Represents endpoints for authorized users
 */
@Controller
@RequestMapping("/home")
public class HomeMainController {
    private final NoteService noteService;
    private final UserService userService;

    private boolean titleIsValid;
    private boolean bodyIsValid;
    private int countForTitleErrors;
    private int countForBodyErrors;

    private final String[] greetings = {
            "Hello",
            "Hi",
            "Howdy",
            "Welcome",
            "Bonjour",
            "Good day",
            "What's up",
            "Aloha",
            "Good morning",
            "Gey",
            "Hi-ya",
            "How are you",
            "How goes it",
            "Howdy-do",
            "Shalom",
            "What's happening"};

    @Autowired
    public HomeMainController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }


    /**
     * Represents <b>"/home" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getHomeMainPage(Model model){
        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));

        return "home-main-pages/home";
    }

    /**
     * Represents <b>"/home/{id of note}" [GET]</b> endpoint
     *
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException auto handling by
     * {@link me.kqlqk.todo_list.exceptions_handling.GlobalExceptionHandler}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String getNote(@PathVariable("id") long id, Model model){
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            throw new NoteNotFoundException("Note with id = " + id + " not found OR note isn't available for current user");
        }

        model.addAttribute("note", noteService.getById(id));

        return "home-main-pages/note";
    }

    /**
     * Represents <b>"/home/{id of note}" [DELETE]</b> endpoint
     *
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException auto handling by
     * {@link me.kqlqk.todo_list.exceptions_handling.GlobalExceptionHandler}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String deleteNote(@PathVariable("id") long id) {
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            throw new NoteNotFoundException("Note with id = " + id + " not found OR note isn't available for current user");
        }

        noteService.delete(id);

        return "redirect:/home";
    }

    /**
     * Represents <b>"/home/new" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public String getNewForm(Model model) {
        model.addAttribute("noteDTO", new NoteDTO());
        return "home-main-pages/new";
    }

    /**
     * Represents <b>"/home/new" [POST]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.POST, value ="/new")
    public String createNote(@ModelAttribute("noteDTO") @Valid NoteDTO noteDTO,
                             BindingResult bindingResult,
                             HttpServletResponse response){
        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "home-main-pages/new";
        }

        Note note = noteDTO.convertToNewNote();
        noteService.add(note);

        return "redirect:/home";
    }

    /**
     * Represents <b>"/home/{id of note}/edit" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/edit")
    public String editNote(@PathVariable("id") long id, Model model){
        if(countForTitleErrors == 0){
            titleIsValid = true;
        }
        countForTitleErrors = 0;

        if(countForBodyErrors == 0){
            bodyIsValid = true;
        }
        countForBodyErrors = 0;

        model.addAttribute("noteDTO", new NoteDTO(noteService.getById(id).getFullTitle(), noteService.getById(id).getBody()));
        model.addAttribute("pathVariable", id);
        model.addAttribute("titleIsValid", titleIsValid);
        model.addAttribute("bodyIsValid", bodyIsValid);
        return "home-main-pages/edit";
    }

    /**
     * Represents <b>"/home/{id of note}/edit" [Put]</b> endpoint
     *
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException auto handling by
     * {@link me.kqlqk.todo_list.exceptions_handling.GlobalExceptionHandler}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/edit")
    public String saveEditedNote(@PathVariable("id") long id, @ModelAttribute("noteDTO") NoteDTO noteDTO){
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            throw new NoteNotFoundException("Note with id = " + id + " not found OR note isn't available for current user");
        }

        if(noteDTO.getTitle().getBytes().length < 1 || noteDTO.getTitle().getBytes().length > 100){
            titleIsValid = false;
            countForTitleErrors++;
            return "redirect:/home/{id}/edit";
        }
        if(noteDTO.getBody().getBytes().length > 65000){
            bodyIsValid = false;
            countForBodyErrors++;
            return "redirect:/home/{id}/edit";
        }

        noteService.update(noteDTO.convertToEditedNote(noteService, id));

        return "redirect:/home";
    }
}

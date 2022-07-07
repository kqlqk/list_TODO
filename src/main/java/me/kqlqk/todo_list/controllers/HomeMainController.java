package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.NoteDTO;
import me.kqlqk.todo_list.exceptions.DAOException;
import me.kqlqk.todo_list.exceptions.dao_exceptions.note_exceptions.NoteNotFoundException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/home")
public class HomeMainController {
    private static final Logger logger = LoggerFactory.getLogger(HomeMainController.class);

    private final NoteService noteService;
    private final UserService userService;

    private boolean titleIsValid;
    private boolean bodyIsValid;
    private int countForTitle;
    private int countForBody;

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


    @GetMapping
    public String showHomeMainPage(Model model){
        logger.debug("was get request to /home by " + userService.getCurrentEmail());

        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));

        return "home-main-pages/home";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") int id, Model model){
        logger.debug("was get request to /home/" + id + " by " + userService.getCurrentEmail());

        if(userService.getCurrentUser() == null){
            return "redirect:/login";
        }

        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getById(id));

        return "home-main-pages/note";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") int id) {
        logger.debug("was delete request to /home/" + id + " by " + userService.getCurrentEmail());

        try {
            if(noteService.existsForUser(userService.getCurrentUser(), id)) {
                noteService.delete(id);
                return "redirect:/home";
            }
        }
        catch (NoteNotFoundException e) {
            logger.warn("was delete request for note that not exist for user " + userService.getCurrentUser());
            return "redirect:/home";
        }

        return "redirect:/home";
    }


    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.debug("was get request to /home/new by " + userService.getCurrentEmail());

        model.addAttribute("noteValid", new NoteDTO());
        return "home-main-pages/new";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute("noteValid") @Valid NoteDTO noteDTO, BindingResult bindingResult,
                             HttpServletRequest request){
        logger.debug("was post request to /home/new by " + userService.getCurrentEmail());

        if(bindingResult.hasErrors()){
            return "home-main-pages/new";
        }

        try{
            noteService.add(noteDTO.convertToNote());
        }
        catch (DAOException e){
            logger.warn(request.getRemoteAddr() + " got " + e);
            return "redirect:/home";
        }

        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
        logger.info("was get request to /home/" + id + "/edit by " + userService.getCurrentEmail());

        if(countForTitle == 0){
            titleIsValid = true;
        }
        countForTitle = 0;

        if(countForBody == 0){
            bodyIsValid = true;
        }
        countForBody = 0;

        model.addAttribute("noteValid", new NoteDTO(noteService.getById(id).getFullTitle(), noteService.getById(id).getBody()));
        model.addAttribute("pathVariable", id);
        model.addAttribute("titleIsValid", titleIsValid);
        model.addAttribute("bodyIsValid", bodyIsValid);
        return "home-main-pages/edit";
    }

    @PostMapping("/{id}/edit")
    public String saveEditedNote(@PathVariable("id") int id, @ModelAttribute("noteValid") NoteDTO noteDTO){
        logger.info("was post request to /home/" + id + "/edit by " + userService.getCurrentEmail());

        if(noteDTO.getTitle().getBytes().length < 1 || noteDTO.getTitle().getBytes().length > 100){
            titleIsValid = false;
            countForTitle++;
            return "redirect:/home/{id}/edit";
        }
        if(noteDTO.getBody().getBytes().length > 65000){
            bodyIsValid = false;
            countForBody++;
            return "redirect:/home/{id}/edit";
        }

        Note note = noteService.getById(id);
        note.setFullTitle(noteDTO.getTitle());
        note.setBody(noteDTO.getBody());

        noteService.update(note);
        return "redirect:/home/{id}";
    }
}

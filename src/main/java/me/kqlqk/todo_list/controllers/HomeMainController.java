package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.dto.NoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/home")
public class HomeMainController {
    private static final Logger logger = LoggerFactory.getLogger(HomeMainController.class);

    private final NoteService noteService;
    private final UserService userService;
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
        logger.info("was get request to /home");
        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));
        return "home-main-pages/home";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") int id, Model model){
        logger.info("was get request to /home/" + id + " by " + userService.getCurrentEmail());
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getById(id));
        return "home-main-pages/note";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") int id){
        logger.info("was delete request to /home/" + id + " by " + userService.getCurrentEmail());
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        noteService.delete(id);
        return "redirect:/home";
    }

    @PutMapping("/{id}")
    public String backToHomePage(@PathVariable String id){
        logger.info("was put request to /home/" + id + " by " + userService.getCurrentEmail());
        return "redirect:/home";
    }

    @GetMapping("/new")
    public String showNewForm(Model model){
        logger.info("was get request to /home/new by " + userService.getCurrentEmail());
        model.addAttribute("noteValid", new NoteDTO());
        return "home-main-pages/new";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute("noteValid") @Valid NoteDTO noteDTO, BindingResult bindingResult){
        logger.info("was post request to /home/new by " + userService.getCurrentEmail());
        if(bindingResult.hasErrors()){
            return "home-main-pages/new";
        }

        Note noteToDB = noteDTO.convertToNote();
        noteService.add(noteToDB);

        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
        logger.info("was get request to /home/" + id + "/edit by " + userService.getCurrentEmail());
        model.addAttribute("note", noteService.getById(id));
        return "home-main-pages/edit";
    }

    @PatchMapping("/{id}/edit")
    public String saveNote(@PathVariable("id") int id, @ModelAttribute("note") Note note){
        logger.info("was patch request to /home/" + id + "/edit by " + userService.getCurrentEmail());
        noteService.update(note);
        return "redirect:/home/{id}";
    }
}

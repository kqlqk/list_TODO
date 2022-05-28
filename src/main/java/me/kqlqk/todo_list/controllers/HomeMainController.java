package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.validation.NoteValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/home")
public class HomeMainController {
    private NoteService noteService;
    private UserService userService;
    private String[] greetings = {
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
        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));
        return "home-main-pages/home";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") long id, Model model){
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getById(id));
        return "home-main-pages/note";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") int id){
        noteService.delete(id);
        return "redirect:/home";
    }

    @PutMapping("/{id}")
    public String backToHomePage(){
        return "redirect:/home";
    }

    @GetMapping("/new")
    public String showNewForm(Model model){
        model.addAttribute("noteValid", new NoteValidation());
        return "home-main-pages/new";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute("noteValid") @Valid NoteValidation noteValidation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "home-main-pages/new";
        }

        Note noteToDB = new Note();

        noteToDB.setFullTitle(noteValidation.getTitle().trim());
        noteToDB.setBody(noteValidation.getBody());

        noteService.add(noteToDB);

        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
        model.addAttribute("note", noteService.getById(id));
        return "home-main-pages/edit";
    }

    @PatchMapping("/{id}/edit")
    public String saveNote(@ModelAttribute("note") Note note){
        noteService.update(note);
        return "redirect:/home/{id}";
    }

}

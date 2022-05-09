package me.kqlqk.springBootApp.controllers;

import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.service.NoteService;
import me.kqlqk.springBootApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {
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
    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }


    @GetMapping
    public String showMainPage(Model model){
        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));
        return "home-page/home";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") long id, Model model){
        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getById(id));
        return "home-page/note";
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
    public String showNewForm(@ModelAttribute("note") Note note){
        return "home-page/new";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute("note") Note note){
        noteService.add(note);
        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
        model.addAttribute("note", noteService.getById(id));
        return "home-page/edit";
    }

    @PatchMapping("/{id}/edit")
    public String saveNote(@ModelAttribute("note") Note note){
        noteService.update(note);
        return "redirect:/home/{id}";
    }

}

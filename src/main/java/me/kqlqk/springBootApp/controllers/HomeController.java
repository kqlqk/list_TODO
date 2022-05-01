package me.kqlqk.springBootApp.controllers;

import me.kqlqk.springBootApp.DAO.NoteDAO;
import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    private NoteService noteService;

    @Autowired
    public HomeController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping
    public String showMainPage(Model model){
        model.addAttribute("notes", noteService.getNotes());
        return "home-page/home";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") int id, Model model){
        if(!noteService.existsById(id)){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getNote(id));
        return "home-page/note";
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable("id") int id){
        noteService.deleteNote(id);
        return "redirect:/home";
    }

    @PutMapping("/{id}")
    public String bactToHomePage(){
        return "redirect:/home";
    }

    @GetMapping("/new")
    public String showNewForm(@ModelAttribute("note") Note note){
        return "home-page/new";
    }

    @PostMapping("/new")
    public String createNote(@ModelAttribute("note") Note note){
        noteService.saveNote(note);
        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
        model.addAttribute("note", noteService.getNote(id));
        return "home-page/edit";
    }

    @PatchMapping("/{id}/edit")
    public String saveNote(@ModelAttribute("note") Note note){
        noteService.update(note);
        return "redirect:/home/{id}";
    }

}

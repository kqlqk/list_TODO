package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.NoteDTO;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/home")
public class HomeMainController {
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


    @RequestMapping(method = RequestMethod.GET)
    public String showHomeMainPage(Model model){
        model.addAttribute("greetings", greetings[(int) (Math.random() * greetings.length)]);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("notes", noteService.getByUser(userService.getCurrentUser()));

        return "home-main-pages/home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String showNote(@PathVariable("id") int id, Model model){
        if(userService.getCurrentUser() == null){
            return "redirect:/login";
        }

        if(!(noteService.existsById(id) && noteService.existsForUser(userService.getCurrentUser(), id))){
            return "redirect:/home";
        }
        model.addAttribute("note", noteService.getById(id));

        return "home-main-pages/note";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public String deleteNote(@PathVariable("id") int id) {
        if(noteService.existsForUser(userService.getCurrentUser(), id)) {
            //throws NoteNotFoundException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
            noteService.delete(id);
        }

        return "redirect:/home";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public String showNewForm(Model model) {
        model.addAttribute("noteValid", new NoteDTO());
        return "home-main-pages/new";
    }

    @RequestMapping(method = RequestMethod.POST, value ="/new")
    public String createNote(@ModelAttribute("noteValid") @Valid NoteDTO noteDTO, BindingResult bindingResult,
                             HttpServletRequest ignoredRequest){
        if(bindingResult.hasErrors()){
            return "home-main-pages/new";
        }

        //throws NoteAlreadyExistException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
        noteService.add(noteDTO.convertToNote());

        return "redirect:/home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/edit")
    public String editNote(@PathVariable("id") int id, Model model){
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

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/edit")
    public String saveEditedNote(@PathVariable("id") int id, @ModelAttribute("noteValid") NoteDTO noteDTO){
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

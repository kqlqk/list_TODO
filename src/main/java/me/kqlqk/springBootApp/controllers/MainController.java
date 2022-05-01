package me.kqlqk.springBootApp.controllers;

import me.kqlqk.springBootApp.models.User;
import me.kqlqk.springBootApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    private UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showMainPage(){
        return "main-page/mainPage";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "main-page/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "main-page/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("user") User user){
        String decryptedPassword = user.getPassword();
        userService.addNew(user);

        if(userService.tryAutoLoginAfterRegistration(user.getEmail(), decryptedPassword)){ //TODO: here's adding decrypted password but need encrypted
            return "redirect:/home";
        }

        return "redirect:/mainPage";
    }
}

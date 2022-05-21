package me.kqlqk.springBootApp.controllers;

import me.kqlqk.springBootApp.models.User;
import me.kqlqk.springBootApp.service.UserService;
import me.kqlqk.springBootApp.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showMainPage(){
        return "main-page/mainLoginPage";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }
        model.addAttribute("user", new UserValidation());
        return "main-page/login";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("user") UserValidation userValidation){

        if(userService.getByEmail(userValidation.getEmail()) != null &&
                passwordEncoder.matches(userValidation.getPassword(), userService.getByEmail(userValidation.getEmail()).getPassword())){
                if(userService.tryAutoLogin(userValidation.getEmail(), userValidation.getPassword())) {
                    return "redirect:/home";
                }

                return "redirect:/error";
            }
        else {
            userValidation.setFormCorrect(false);
        }

        return "main-page/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new UserValidation());
        return "main-page/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("user") @Valid UserValidation userValidation, BindingResult bindingResult){
        if(bindingResult.hasErrors() || !userValidation.getConfirmPassword().equals(userValidation.getPassword())){
            return "main-page/registration";
        }

        User userToDB = new User();
        userToDB.setEmail(userValidation.getEmail());
        userToDB.setLogin(userValidation.getLogin());
        userToDB.setPassword(userValidation.getPassword());
        userToDB.setConfirmPassword(userValidation.getConfirmPassword());

        String decryptedPassword = userToDB.getPassword();

        userService.addNew(userToDB);

        if(userService.tryAutoLogin(userToDB.getEmail(), decryptedPassword)){
            return "redirect:/home";
        }

        return "redirect:/mainPage";
    }
}

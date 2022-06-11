package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.validation.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showMainPage(){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }
        return "main-pages/mainLoginPage";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }
        model.addAttribute("userValid", new UserValidation());
        model.addAttribute("rememberMe", null);
        return "main-pages/login";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("userValid") UserValidation userValidation,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletResponse response,
                        HttpServletRequest request){
        if(userService.getByEmailOrLogin(userValidation.getLoginObject()) != null &&
                passwordEncoder.matches(userValidation.getPassword(), userService.getByEmailOrLogin(userValidation.getLoginObject()).getPassword())){
                if(userService.canAutoLogin(userValidation.getLoginObject(), userValidation.getPassword())) {
                    setSessionCookie(request, response, rememberMe);
                    userService.autoLogin(userValidation.getLoginObject(), userValidation.getPassword());
                    return "redirect:/home";
                }

                return "redirect:/error";
        }
        else {
            userValidation.setFormCorrect(false);
        }
        return "main-pages/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("userValid", new UserValidation());
        return "main-pages/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("userValid") @Valid UserValidation userValidation, BindingResult bindingResult){
        if(bindingResult.hasErrors() || !userValidation.getConfirmPassword().equals(userValidation.getPassword())){
            return "main-pages/registration";
        }

        User userToDB = userValidation.convertToUser();
        userService.add(userToDB);

        String decryptedPassword = userToDB.getPassword();

        if(userService.canAutoLogin(userToDB.getEmail(), decryptedPassword)){
            userService.autoLogin(userToDB.getEmail(), decryptedPassword);
            logger.info("Was created new user " + userToDB.getEmail());
            return "redirect:/home";
        }

        return "redirect:/mainPage";
    }


    private void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String status){
        if (request.getCookies().length != 0) {
            if (status.equals("on")) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(60 * 60 * 24 * 7);
                        response.addCookie(cookie);
                    }
                }
            } else {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(-1);
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

}

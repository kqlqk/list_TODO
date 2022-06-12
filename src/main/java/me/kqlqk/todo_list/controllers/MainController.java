package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showMainPage(){
        logger.info("was get request to /");
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }
        return "main-pages/mainLoginPage";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        logger.info("was get request to /login");
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }
        model.addAttribute("userValid", new UserDTO());
        model.addAttribute("rememberMe", null);
        return "main-pages/login";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("userValid") UserDTO userDTO,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletResponse response,
                        HttpServletRequest request){
        logger.info("was post request to /login");
        if(userService.getByEmailOrLogin(userDTO.getLoginObject()) != null &&
                passwordEncoder.matches(userDTO.getPassword(), userService.getByEmailOrLogin(userDTO.getLoginObject()).getPassword())){
                if(userService.canAutoLogin(userDTO.getLoginObject(), userDTO.getPassword())) {
                    setSessionCookie(request, response, rememberMe);
                    userService.autoLogin(userDTO.getLoginObject(), userDTO.getPassword());
                    return "redirect:/home";
                }

                return "redirect:/error";
        }
        else {
            userDTO.setFormCorrect(false);
        }
        return "main-pages/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        logger.info("was get request to /registration");
        model.addAttribute("userValid", new UserDTO());
        return "main-pages/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult){
        logger.info("was post request to /registration");
        if(bindingResult.hasErrors() || !userDTO.getConfirmPassword().equals(userDTO.getPassword())){
            return "main-pages/registration";
        }
        String decryptedPassword = userDTO.getPassword();

        User userToDB = userDTO.convertToUser();
        userService.add(userToDB);


        if(userService.canAutoLogin(userToDB.getEmail(), decryptedPassword)){
            userService.autoLogin(userToDB.getEmail(), decryptedPassword);
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

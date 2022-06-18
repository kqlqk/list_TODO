package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.ControllersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ControllersUtil controllersUtil;

    @Value("${temp.password.oauth2}")
    private String tempPassword;
    private boolean loginWithForm;
    private int count;

    @Autowired
    public MainController(UserService userService, PasswordEncoder passwordEncoder, ControllersUtil controllersUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.controllersUtil = controllersUtil;
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

        if(count == 0){
            loginWithForm = false;
        }
        count = 0;

        model.addAttribute("userValid", new UserDTO());
        model.addAttribute("rememberMe", null);
        model.addAttribute("oAuth2LoginByFormAttempt", false);
        model.addAttribute("loginWithForm", loginWithForm);
        return "main-pages/login";
    }

    @GetMapping("/tempOAuth2LoginPage")
    public String transferOAuth2User(){
        logger.info("was get request to /tempOAuth2LoginPage");

        OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SecurityContextHolder.clearContext();

        if(userService.existsByEmail(oAuth2User.getAttribute("email"))){
            if(!userService.getByEmail(oAuth2User.getAttribute("email")).isOAuth2()) {
                loginWithForm = true;
                count++;
                return "redirect:/login";
            }
        }
        else{
            userService.convertOAuth2UserToUserAndSave(oAuth2User);
        }

            if(userService.canAutoLogin(oAuth2User.getAttribute("email"), tempPassword)){
            userService.autoLogin(oAuth2User.getAttribute("email"), tempPassword);
            return "redirect:/home";
        }
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("userValid") UserDTO userDTO,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletResponse response,
                        HttpServletRequest request,
                        Model model){
        logger.info("was post request to /login");

        if(userService.getByLoginObj(userDTO.getLoginObject()) == null){
            userDTO.setFormCorrect(false);
            return "main-pages/login";
        }

        if(userService.getByLoginObj(userDTO.getLoginObject()).isOAuth2()){
            model.addAttribute("oAuth2LoginByFormAttempt", true);
            return "main-pages/login";
        }

        if(passwordEncoder.matches(userDTO.getPassword(), userService.getByLoginObj(userDTO.getLoginObject()).getPassword())){
            if(userService.canAutoLogin(userDTO.getLoginObject(), userDTO.getPassword())) {
                controllersUtil.setSessionCookie(request, response, rememberMe);
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
        model.addAttribute("emailIsAlreadyRegistered", false);
        model.addAttribute("loginIsAlreadyRegistered", false);
        return "main-pages/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult, Model model){
        logger.info("was post request to /registration");

        if(bindingResult.hasErrors() || !userDTO.getConfirmPassword().equals(userDTO.getPassword())){
            return "main-pages/registration";
        }

        if(userService.existsByEmail(userDTO.getEmail())){
            model.addAttribute("emailIsAlreadyRegistered", true);
            return "main-pages/registration";
        }

        if(userService.existsByLogin(userDTO.getLogin())){
            model.addAttribute("loginIsAlreadyRegistered", true);
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

}

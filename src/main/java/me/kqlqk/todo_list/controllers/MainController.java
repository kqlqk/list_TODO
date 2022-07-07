package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.exceptions.DAOException;
import me.kqlqk.todo_list.exceptions.ServiceException;
import me.kqlqk.todo_list.exceptions.dao_exceptions.user_exceptions.UserAlreadyExistException;
import me.kqlqk.todo_list.exceptions.dao_exceptions.user_exceptions.status.UserStatus;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${temp.password.oauth2}")
    private String tempPassword;

    private boolean loginWithForm;
    private int countForLoginWithForm;

    @Autowired
    public MainController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @GetMapping
    public String showMainPage(HttpServletRequest request){
        logger.debug("was get request to / by " + request.getRemoteAddr());

        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }

        return "main-pages/mainLoginPage";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model){
        logger.debug("was get request to /login " + request.getRemoteAddr());

        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }

        if(countForLoginWithForm == 0){
            loginWithForm = false;
        }
        countForLoginWithForm = 0;

        model.addAttribute("userValid", new UserDTO());
        model.addAttribute("oAuth2LoginByFormAttempt", false);
        model.addAttribute("loginWithForm", loginWithForm);
        return "main-pages/login";
    }

    @GetMapping("/tempOAuth2LoginPage")
    public String transferOAuth2User(HttpServletRequest request){
        logger.debug("was get request to /tempOAuth2LoginPage " + request.getRemoteAddr());

        OAuth2User oAuth2User;

        if(!userService.isUserUsedOAuth2Login()){
            return "redirect:/";
        }

        try{
            oAuth2User = userService.getOAuth2UserFromSecurityContextHolder();
        } catch (DAOException e){
            logger.warn(request.getRemoteAddr() + " got " + e);
            return "redirect:/login";
        }

        if(userService.existsByEmail(oAuth2User.getAttribute("email"))){
            if(!userService.getByEmail(oAuth2User.getAttribute("email")).isOAuth2()){
                loginWithForm = true;
                countForLoginWithForm++;
                return "redirect:/login";
            }
        }
        else{
            userService.convertOAuth2UserToUserAndSave(oAuth2User);
        }

        try{
            userService.setAuth(oAuth2User.getAttribute("email"), tempPassword);
            return "redirect:/home";
        }
        catch (ServiceException e){
            logger.warn(request.getRemoteAddr() + " got " + e);
            return "redirect:/login";
        }

    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute("userValid") UserDTO userDTO,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletRequest request,
                        Model model) {
        logger.debug("was post request to /login " + request.getRemoteAddr());

        try{
            userService.getByLoginObj(userDTO.getLoginObject());
        }
        catch (DAOException e){
            userDTO.setFormCorrect(false);
            return "main-pages/login";
        }

        if(userService.getByLoginObj(userDTO.getLoginObject()).isOAuth2()){
            model.addAttribute("oAuth2LoginByFormAttempt", true);
            return "main-pages/login";
        }

        if(passwordEncoder.matches(userDTO.getPassword(), userService.getByLoginObj(userDTO.getLoginObject()).getPassword())){
            try{
                userService.setAuth(userDTO.getLoginObject().contains("@") ?
                                        userDTO.getLoginObject().toLowerCase() :
                                        userDTO.getLoginObject() ,
                                    userDTO.getPassword());
                return "redirect:/home";
            }
            catch (ServiceException e){
                logger.warn(request.getRemoteAddr() + " got " + e);
                return "redirect:/login";
            }
        }
        else{
            userDTO.setFormCorrect(false);
        }
        return "main-pages/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(HttpServletRequest request, Model model){
        logger.debug("was get request to /registration " + request.getRemoteAddr());

        model.addAttribute("userValid", new UserDTO());
        model.addAttribute("emailIsAlreadyRegistered", false);
        model.addAttribute("loginIsAlreadyRegistered", false);
        return "main-pages/registration";
    }

    @PostMapping("/registration")
    public String signUp(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request){
        logger.debug("was post request to /registration " + request.getRemoteAddr());

        if(bindingResult.hasErrors() || !userDTO.getConfirmPassword().equals(userDTO.getPassword())){
            return "main-pages/registration";
        }

        String decryptedPassword = userDTO.getPassword();

        User userToDB = userDTO.convertToUser();

        try{
            userService.add(userToDB);
        }
        catch (UserAlreadyExistException e){
            if (e.getUserStatus() == UserStatus.EMAIL_ALREADY_EXIST){
                model.addAttribute("emailIsAlreadyRegistered", true);
                return "main-pages/registration";
            }
            if(e.getUserStatus() == UserStatus.LOGIN_ALREADY_EXIST){
                model.addAttribute("loginIsAlreadyRegistered", true);
                return "main-pages/registration";
            }
        }


        try{
            userService.setAuth(userToDB.getEmail(), decryptedPassword);
            return "redirect:/home";
        }
        catch (ServiceException e){
            logger.warn(request.getRemoteAddr() + " got " + e);
        }

        return "redirect:/mainPage";
    }

}

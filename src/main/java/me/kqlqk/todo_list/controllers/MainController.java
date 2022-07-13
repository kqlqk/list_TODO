package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.exceptions.dao.user.UserAlreadyExistException;
import me.kqlqk.todo_list.exceptions.dao.user.UserNotFoundException;
import me.kqlqk.todo_list.exceptions.dao.user.status.UserStatus;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class MainController {
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


    @RequestMapping(method = RequestMethod.GET)
    public String showMainPage(HttpServletRequest ignoredRequest){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }

        return "main-pages/mainLoginPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String showLoginPage(HttpServletRequest ignoredRequest, Model model){
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

    @RequestMapping(method = RequestMethod.GET, value = "/tempOAuth2LoginPage")
    public String transferOAuth2User(HttpServletRequest ignoredRequest){
        OAuth2User oAuth2User;

        if(!userService.isUserUsedOAuth2Login()){
            return "redirect:/";
        }

        //throws UserNotFoundException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
        oAuth2User = userService.getOAuth2UserFromSecurityContextHolder();

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

       //throws AuthenticationNotAuthenticatedException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
        userService.setAuth(oAuth2User.getAttribute("email"), tempPassword);

        return "redirect:/home";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String logIn(@ModelAttribute("userValid") UserDTO userDTO,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletRequest ignoredRequest,
                        Model model) {
        try{
            userService.getByLoginObj(userDTO.getLoginObject());
        }
        catch (UserNotFoundException e){
            userDTO.setFormCorrect(false);
            return "main-pages/login";
        }

        if(userService.getByLoginObj(userDTO.getLoginObject()).isOAuth2()){
            model.addAttribute("oAuth2LoginByFormAttempt", true);
            return "main-pages/login";
        }

        if(passwordEncoder.matches(userDTO.getPassword(), userService.getByLoginObj(userDTO.getLoginObject()).getPassword())) {
            //throws AuthenticationNotAuthenticatedException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
            userService.setAuth(userDTO.getLoginObject().contains("@") ?
                            userDTO.getLoginObject().toLowerCase() :
                            userDTO.getLoginObject(),
                    userDTO.getPassword());
            return "redirect:/home";
        }
        else{
            userDTO.setFormCorrect(false);
        }
        return "main-pages/login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/registration")
    public String showRegistrationPage(HttpServletRequest ignoredRequest, Model model){
        model.addAttribute("userValid", new UserDTO());
        model.addAttribute("emailIsAlreadyRegistered", false);
        model.addAttribute("loginIsAlreadyRegistered", false);
        return "main-pages/registration";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public String signUp(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult,
                         Model model,
                         HttpServletRequest ignoredRequest){
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


        //throws AuthenticationNotAuthenticatedException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
        userService.setAuth(userToDB.getEmail(), decryptedPassword);

        return "redirect:/home";
    }

}

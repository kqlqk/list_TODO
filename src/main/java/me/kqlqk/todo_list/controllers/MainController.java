package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.dto.LoginDTO;
import me.kqlqk.todo_list.dto.RegistrationDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class MainController {
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MainController(AccessTokenService accessTokenService,
                          RefreshTokenService refreshTokenService,
                          UserService userService,
                          PasswordEncoder passwordEncoder){
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String showMainPage(){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }

        return "main-pages/main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String showLoginPage( Model model){
        if(userService.getCurrentUser() != null){
            return "redirect:/home";
        }

        model.addAttribute("loginDTO", new LoginDTO());
        return "main-pages/login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String logIn(@ModelAttribute("loginDTO") LoginDTO loginDTO,
                        @ModelAttribute("rememberMe") String rememberMe,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        User user = userService.getByLoginObj(loginDTO.getLoginObj());

        if(user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            loginDTO.setFormCorrect(false);
            return "main-pages/login";
        }

        refreshTokenService.update(user);

        String accessToken = accessTokenService.createToken(user.getEmail());
        String refreshToken = refreshTokenService.getByUser(user).getToken();

        UtilCookie.createOrUpdateCookie("at", accessToken, (int) (refreshTokenService.getValidity() / 1000), request, response);
        UtilCookie.createOrUpdateCookie("rt", refreshToken, (int) (refreshTokenService.getValidity() / 1000), request, response);

        return "redirect:/home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("emailAlreadyRegistered", false);
        model.addAttribute("loginAlreadyRegistered", false);

        return "main-pages/registration";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public String signUp(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request,
                         HttpServletResponse response){
        if(bindingResult.hasErrors() || !registrationDTO.getConfirmPassword().equals(registrationDTO.getPassword())){
            return "main-pages/registration";
        }

        if(userService.getByEmail(registrationDTO.getEmail()) != null){
            model.addAttribute("emailIsAlreadyRegistered", true);
            return "main-pages/registration";
        }
        if(userService.getByLogin(registrationDTO.getLogin()) != null){
            model.addAttribute("loginIsAlreadyRegistered", true);
            return "main-pages/registration";
        }

        User user = registrationDTO.convertToUser();
        userService.add(user);

        refreshTokenService.create(user);
        String accessToken = accessTokenService.createToken(user.getEmail());
        String refreshToken = refreshTokenService.getByUser(user).getToken();

        UtilCookie.createOrUpdateCookie("at", accessToken, (int) (refreshTokenService.getValidity() / 1000), request, response);
        UtilCookie.createOrUpdateCookie("rt", refreshToken, (int) (refreshTokenService.getValidity() / 1000), request, response);

        return "redirect:/home";
    }

}

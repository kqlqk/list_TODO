package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.LoginDTO;
import me.kqlqk.todo_list.dto.RegistrationDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.GlobalVariables;
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

/**
 * Represents endpoints for guests
 */
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
                          PasswordEncoder passwordEncoder) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Represents <b>"/" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getMainPage() {
        return "main-pages/main";
    }

    /**
     * Represents <b>"/login" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "main-pages/login";
    }

    /**
     * Represents <b>"/login" [POST]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String logIn(@ModelAttribute("loginDTO") LoginDTO loginDTO,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        User user = userService.getByLoginObj(loginDTO.getLoginObj());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            loginDTO.setFormCorrect(false);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "main-pages/login";
        }

        GlobalVariables.REMEMBER_ME = loginDTO.isRememberMe();
        refreshTokenService.updateAccessAndRefreshTokens(user, request, response, true, GlobalVariables.REMEMBER_ME);

        return "redirect:/home";
    }

    /**
     * Represents <b>"/registration" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/registration")
    public String getSignUpPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("emailAlreadyRegistered", false);
        model.addAttribute("loginAlreadyRegistered", false);

        return "main-pages/registration";
    }

    /**
     * Represents <b>"/registration" [POST]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public String signUp(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
                         BindingResult bindingResult,
                         Model model,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        if (bindingResult.hasErrors() || !registrationDTO.getConfirmPassword().equals(registrationDTO.getPassword())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "main-pages/registration";
        }

        if (userService.getByEmail(registrationDTO.getEmail()) != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("emailAlreadyRegistered", true);
            return "main-pages/registration";
        }
        if (userService.getByLogin(registrationDTO.getLogin()) != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("loginAlreadyRegistered", true);
            return "main-pages/registration";
        }

        User user = registrationDTO.convertToUser();
        userService.add(user);

        String refreshToken = refreshTokenService.createAndGetToken(user);
        String accessToken = accessTokenService.createAndGetToken(user.getEmail());

        GlobalVariables.REMEMBER_ME = registrationDTO.isRememberMe();

        UtilCookie.createOrUpdateOrDeleteCookie("at", accessToken, (int) (refreshTokenService.getValidity() / 1000), request, response);
        UtilCookie.createOrUpdateOrDeleteCookie("rt", refreshToken, (int) (refreshTokenService.getValidity() / 1000), request, response);

        return "redirect:/home";
    }

}

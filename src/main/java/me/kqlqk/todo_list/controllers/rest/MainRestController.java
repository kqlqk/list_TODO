package me.kqlqk.todo_list.controllers.rest;

import me.kqlqk.todo_list.dto.LoginDTO;
import me.kqlqk.todo_list.dto.RegistrationDTO;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.AccessTokenService;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MainRestController(UserService userService, AccessTokenService accessTokenService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
        User user = userService.getByLoginObj(loginDTO.getLoginObj());

        if(user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new UserNotFoundException("Email/Username or password incorrect");
        }

        Map<String, String> tokens = refreshTokenService.updateAccessAndRefreshTokens(user, request, response, loginDTO.isSetCookie(), false);

        return ResponseEntity.accepted().body(tokens);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public ResponseEntity<?> signUp(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request, HttpServletResponse response){
        if(userService.getByEmail(registrationDTO.getEmail()) != null){
            throw new UserAlreadyExistsException("Email " + registrationDTO.getEmail() + " already registered");
        }
        if(userService.getByLogin(registrationDTO.getLogin()) != null){
            throw new UserAlreadyExistsException("Login " + registrationDTO.getLogin() + " already registered");
        }

        User user = registrationDTO.convertToUser();
        userService.add(user);

        String refreshToken = refreshTokenService.createAndGetToken(user);
        String accessToken = accessTokenService.createToken(user.getEmail());

        if(registrationDTO.isSetCookie()) {
            UtilCookie.createOrUpdateCookie("at", accessToken, (int) (refreshTokenService.getValidity() / 1000), request, response);
            UtilCookie.createOrUpdateCookie("rt", refreshToken, (int) (refreshTokenService.getValidity() / 1000), request, response);
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return ResponseEntity.accepted().body(tokens);
    }
}

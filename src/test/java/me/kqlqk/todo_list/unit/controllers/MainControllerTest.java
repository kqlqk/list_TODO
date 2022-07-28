package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.MainController;
import me.kqlqk.todo_list.dto.LoginDTO;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private me.kqlqk.todo_list.models.User user;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private LoginDTO loginDTO;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    public void showMainPage() {
        mainController.showMainPage();

        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void showLoginPage() {
        mainController.showLoginPage( model);
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void transferOAuth2User() {
        when(userService.getOAuth2UserFromSecurityContextHolder()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("testMail");
        when(userService.existsByEmail("testMail")).thenReturn(true);
        when(userService.getByEmail("testMail")).thenReturn(user);
        when(user.isOAuth2()).thenReturn(true);
        when(userService.isUserUsedOAuth2Login()).thenReturn(true);


        verify(userService, times(1)).getOAuth2UserFromSecurityContextHolder();
        verify(userService, times(1)).existsByEmail("testMail");
        verify(userService, times(1)).getByEmail("testMail");
        //verify(userService, times(1)).setAuth("testMail", null);
    }

    @Test
    public void logIn() {
        when(userService.getByLoginObj("testMail")).thenReturn(user);
        when(user.getPassword()).thenReturn("testPSWD");
        when(loginDTO.getLoginObj()).thenReturn("testMail");
        when(loginDTO.getPassword()).thenReturn("testPSWD");
        when(user.isOAuth2()).thenReturn(false);
        when(passwordEncoder.matches("testPSWD", "testPSWD")).thenReturn(true);

        mainController.logIn(loginDTO, "", request, response);

        verify(userService, times(3)).getByLoginObj(loginDTO.getLoginObj());
       // verify(userService, times(1)).setAuth("testMail", "testPSWD");
    }

    @Test
    public void signUp() {
        when(bindingResult.hasErrors()).thenReturn(false);
        //when(loginDTO.convertToUser()).thenReturn(user);
        when(loginDTO.getPassword()).thenReturn("testPSWD");
       // when(loginDTO.getConfirmPassword()).thenReturn("testPSWD");
        when(user.getEmail()).thenReturn("testMail");

        //mainController.signUp(loginDTO, bindingResult, model);

        verify(userService, times(1)).add(any());
        //verify(userService, times(1)).setAuth("testMail", "testPSWD");
    }
}

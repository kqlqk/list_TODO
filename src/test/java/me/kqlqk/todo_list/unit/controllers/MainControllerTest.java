package me.kqlqk.todo_list.unit.controllers;

import me.kqlqk.todo_list.controllers.MainController;
import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.ControllersUtil;
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
    private ControllersUtil controllersUtil;

    @Mock
    private Model model;

    @Mock
    private User user;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private UserDTO userDTO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private BindingResult bindingResult;

    @Test
    public void showMainPage() {
        mainController.showMainPage();

        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void showLoginPage() {
        mainController.showLoginPage(model);
        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void transferOAuth2User() {
        when(userService.getOAuth2UserFromSecurityContextHolder()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("testMail");
        when(userService.existsByEmail("testMail")).thenReturn(true);
        when(userService.getByEmail("testMail")).thenReturn(user);
        when(user.isOAuth2()).thenReturn(true);
        when(userService.canAutoLogin("testMail", null)).thenReturn(true);

        mainController.transferOAuth2User();

        verify(userService, times(1)).getOAuth2UserFromSecurityContextHolder();
        verify(userService, times(1)).existsByEmail("testMail");
        verify(userService, times(1)).getByEmail("testMail");
        verify(userService, times(1)).canAutoLogin("testMail", null);
        verify(userService, times(1)).autoLogin("testMail", null);
    }

    @Test
    public void logIn() {
        when(userService.getByLoginObj("testMail")).thenReturn(user);
        when(user.getPassword()).thenReturn("testPSWD");
        when(userDTO.getLoginObject()).thenReturn("testMail");
        when(userDTO.getPassword()).thenReturn("testPSWD");
        when(user.isOAuth2()).thenReturn(false);
        when(passwordEncoder.matches("testPSWD", "testPSWD")).thenReturn(true);
        when(userService.canAutoLogin("testMail", "testPSWD")).thenReturn(true);

        mainController.logIn(userDTO, "", request, response, model);

        verify(userService, times(3)).getByLoginObj(userDTO.getLoginObject());
        verify(userService, times(1)).canAutoLogin("testMail", "testPSWD");
        verify(userService, times(1)).autoLogin("testMail", "testPSWD");
    }

    @Test
    public void signUp() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userDTO.convertToUser()).thenReturn(user);
        when(userDTO.getPassword()).thenReturn("testPSWD");
        when(userDTO.getConfirmPassword()).thenReturn("testPSWD");
        when(user.getEmail()).thenReturn("testMail");
        when(userService.canAutoLogin("testMail", "testPSWD")).thenReturn(true);

        mainController.signUp(userDTO, bindingResult, model);

        verify(userService, times(1)).add(any());
        verify(userService, times(1)).canAutoLogin("testMail", "testPSWD");
        verify(userService, times(1)).autoLogin("testMail", "testPSWD");
    }
}

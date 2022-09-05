package integration.me.kqlqk.todo_list.controllers;

import annotations.TestController;
import me.kqlqk.todo_list.util.GlobalVariables;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestController
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showMainPage_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TODO list")));
    }

    @Test
    public void showLoginPage_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loginDTO"));
    }

    @Test
    public void logIn_shouldCorrectLogInWithoutRememberMe() throws Exception {
        mockMvc.perform(post("/login")
                    .param("loginObj", "userLogin")
                    .param("password", "User1234"))
                .andDo(print())
                .andExpect(cookie().exists("at"))
                .andExpect(cookie().exists("rt"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
        Assertions.assertThat(GlobalVariables.REMEMBER_ME).isFalse();
    }

    @Test
    public void logIn_shouldCorrectLoginWithRememberMe() throws Exception{
        mockMvc.perform(post("/login")
                        .param("loginObj", "userLogin")
                        .param("password", "User1234")
                        .param("rememberMe", "on"))
                .andDo(print())
                .andExpect(cookie().exists("at"))
                .andExpect(cookie().exists("rt"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Assertions.assertThat(GlobalVariables.REMEMBER_ME).isTrue();
    }

    @Test
    public void logIn_shouldIncorrectLogIn() throws Exception {
        mockMvc.perform(post("/login")
                    .param("loginObj", "userLogin")
                    .param("password", "badPswd"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("loginDTO"))
                .andExpect(model().attribute("loginDTO", hasProperty("formCorrect", is(false))))
                .andExpect(content().string(containsString("Email/Login or password incorrect, try one more time")));

        mockMvc.perform(post("/login")
                        .param("loginObj", "badUserLogin")
                        .param("password", "User1234"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("loginDTO"))
                .andExpect(model().attribute("loginDTO", hasProperty("formCorrect", is(false))))
                .andExpect(content().string(containsString("Email/Login or password incorrect, try one more time")));
    }

    @Test
    public void showRegistrationPage_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/registration"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registrationDTO"))
                .andExpect(model().attributeExists("emailAlreadyRegistered"))
                .andExpect(model().attributeExists("loginAlreadyRegistered"))
                .andExpect(model().attribute("emailAlreadyRegistered", is(false)))
                .andExpect(model().attribute("loginAlreadyRegistered", is(false)));
    }

    @Test
    public void signUp_shouldCorrectSignUp() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "newUser@mail.com")
                        .param("login", "newUserLogin")
                        .param("password", "User1234")
                        .param("confirmPassword", "User1234"))
                .andDo(print())
                .andExpect(cookie().exists("at"))
                .andExpect(cookie().exists("rt"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

    }

    @Test
    public void signUp_shouldIncorrectSignUp() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("email", "")
                        .param("login", "newUserLogin")
                        .param("password", "User1234")
                        .param("confirmPassword", "User1234"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email must be valid")));

        mockMvc.perform(post("/registration")
                        .param("email", "user@mail.com")
                        .param("login", "newUserLogin")
                        .param("password", "User1234")
                        .param("confirmPassword", "User1234"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().attribute("emailAlreadyRegistered", is(true)));

        mockMvc.perform(post("/registration")
                        .param("email", "newUser@mail.com")
                        .param("login", "userLogin")
                        .param("password", "User1234")
                        .param("confirmPassword", "User1234"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().attribute("loginAlreadyRegistered", is(true)));

        mockMvc.perform(post("/registration")
                        .param("email", "newUser@mail.com")
                        .param("login", "newUserLogin")
                        .param("password", "User1234")
                        .param("confirmPassword", "User12345"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Passwords don&#39;t match. Try again.")));
    }

}

package integration.me.kqlqk.todo_list.controllers;

import annotations.TestController;
import me.kqlqk.todo_list.controllers.RecoveryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestController
public class RecoveryControllerTest {
    @Autowired
    private RecoveryController recoveryController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showRecoveryPage_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/recovery"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recoveryDTO"))
                .andExpect(content().string(containsString("Password recovery")));
    }

    @Test
    public void sendEmail_shouldSendEmail() throws Exception {
        mockMvc.perform(post("/recovery")
                        .param("email", "user@mail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("recoveryDTO"))
                .andExpect(model().attribute("recoveryDTO", hasProperty("email", is("user@mail.com"))))
                .andExpect(content().string(containsString("Success")));
    }

    @Test
    public void sendEmail_shouldHasErrors() throws Exception {
        mockMvc.perform(post("/recovery")
                        .param("email", "bad_email"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("recoveryDTO"))
                .andExpect(model().attribute("recoveryDTO", hasProperty("formCorrect", is(false))))
                .andExpect(content().string(containsString("Password recovery")));

        mockMvc.perform(post("/recovery")
                        .param("email", "email_not_exist@mail.com"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("recoveryDTO"))
                .andExpect(model().attribute("recoveryDTO", hasProperty("formCorrect", is(false))))
                .andExpect(content().string(containsString("Password recovery")));
    }

    @Test
    public void showChangingPasswordPage_shouldLoadContent() throws Exception {
        recoveryController.getRecoveryPageIdEmail().put(1, "user@mail.com");

        mockMvc.perform(get("/recovery/1"))
                .andDo(print())
                .andExpect(model().attributeExists("pageId"))
                .andExpect(model().attributeExists("recoveryDTO"))
                .andExpect(model().attribute("pageId", is(1)))
                .andExpect(content().string(containsString("Changing password")));
    }

    @Test
    public void showChangingPasswordPage_shouldRedirect() throws Exception {
        mockMvc.perform(get("/recovery/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void changePassword_shouldChangePassword() throws Exception {
        recoveryController.getRecoveryPageIdEmail().put(1, "user@mail.com");

        mockMvc.perform(post("/recovery/1")
                        .param("password", "User12345")
                        .param("confirmPassword", "User12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));
    }

    @Test
    public void changePassword() throws Exception {
        mockMvc.perform(post("/recovery/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        recoveryController.getRecoveryPageIdEmail().put(1, "user@mail.com");

        mockMvc.perform(post("/recovery/1")
                    .param("password", "User12345")
                    .param("confirmPassword", "AnotherPswd"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Changing password")));
    }
}
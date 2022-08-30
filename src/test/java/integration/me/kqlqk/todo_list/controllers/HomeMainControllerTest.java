package integration.me.kqlqk.todo_list.controllers;

import annotations.TestController;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestController
public class HomeMainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    private String userRefreshToken;

    @BeforeEach
    public void init(){
        userRefreshToken = refreshTokenService.updateRefreshToken(userService.getByEmail("user@mail.com"));
        System.out.println(userRefreshToken);
    }

    @Test
    public void showHomeMainPage_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/home")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("greetings"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(content().string(containsString("Home")));
    }

    @Test
    public void showNote_shouldLoadNote() throws Exception {
        mockMvc.perform(get("/home/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("note"))
                .andExpect(content().string(containsString("Note")));
    }

    @Test
    public void showNote_shouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/home/9999")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));


        mockMvc.perform(get("/home/3")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void deleteNote_shouldDeleteNote() throws Exception {
        mockMvc.perform(delete("/home/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void deleteNote_shouldRedirectToHome() throws Exception {
        mockMvc.perform(delete("/home/9999")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));


        mockMvc.perform(delete("/home/3")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void showNewForm_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/home/new")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noteDTO"))
                .andExpect(content().string(containsString("New note")));
    }

    @Test
    public void createNote_shouldCreateNote() throws Exception {
        mockMvc.perform(post("/home/new")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", "anyTitle"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void createNote_shouldHasErrors() throws Exception {
        mockMvc.perform(post("/home/new")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", ""))
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("New note")));
    }

    @Test
    public void editNote_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/home/1/edit")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(model().attributeExists("noteDTO"))
                .andExpect(model().attributeExists("pathVariable"))
                .andExpect(model().attributeExists("titleIsValid"))
                .andExpect(model().attributeExists("bodyIsValid"))
                .andExpect(content().string(containsString("Edit note")));
    }

    @Test
    public void saveEditedNote_shouldSaveEditedNote() throws Exception {
        mockMvc.perform(put("/home/1/edit")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", "new title")
                        .param("body", "new BODY"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void saveEditedNote_shouldHasErrors() throws Exception {
        mockMvc.perform(put("/home/90/edit")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", "new title")
                        .param("body", "new BODY"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));


        mockMvc.perform(put("/home/3/edit")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", "new title")
                        .param("body", "new BODY"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));


        mockMvc.perform(put("/home/1/edit")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .param("title", "")
                        .param("body", "new BODY"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home/1/edit"));

    }
}
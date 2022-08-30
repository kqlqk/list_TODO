package integration.me.kqlqk.todo_list.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import integration.me.kqlqk.todo_list.IntegrationControllerParent;
import me.kqlqk.todo_list.dto.daoDTOs.NoteDTO;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HomeMainRestControllerTest extends IntegrationControllerParent {
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
    }

    @Test
    public void getAllNotesForUser_shouldReturnNotes() throws Exception {
        mockMvc.perform(get("/api/notes")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getNoteForUser_shouldReturnNote() throws Exception {
        mockMvc.perform(get("/api/notes/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getNoteForUser_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/notes/99")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNote_shouldCreateNote() throws Exception {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("anyTitle");
        String jsonNoteDTO = new ObjectMapper().writeValueAsString(noteDTO);

        mockMvc.perform(post("/api/notes")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNoteDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void createNote_shouldReturnExceptionJson() throws Exception {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("");
        String jsonNoteDTO = new ObjectMapper().writeValueAsString(noteDTO);

        mockMvc.perform(post("/api/notes")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNoteDTO))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info").exists());
    }

    @Test
    public void editNote_shouldSaveEditedNote() throws Exception {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("editedTitle");
        String jsonNoteDTO = new ObjectMapper().writeValueAsString(noteDTO);

        mockMvc.perform(put("/api/notes/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNoteDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.title", is("editedTitle")));
    }

    @Test
    public void editNote_shouldReturnExceptionJson() throws Exception {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("");
        String jsonNoteDTO = new ObjectMapper().writeValueAsString(noteDTO);

        mockMvc.perform(put("/api/notes/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNoteDTO))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info").exists());
    }

    @Test
    public void deleteNote_shouldDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/1")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void deleteNote_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/notes/99")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
package integration.me.kqlqk.todo_list.controllers.rest;

import annotations.TestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.kqlqk.todo_list.controllers.rest.RecoveryRestController;
import me.kqlqk.todo_list.dto.RecoveryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
public class RecoveryRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecoveryRestController recoveryRestController;

    @Test
    public void sendEmail_shouldReturnJson() throws Exception {
        RecoveryDTO recoveryDTO = new RecoveryDTO();
        recoveryDTO.setEmail("user@mail.com");
        String jsonRecoveryDTO = new ObjectMapper().writeValueAsString(recoveryDTO);

        mockMvc.perform(post("/api/recovery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRecoveryDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("A message with further actions has been sent to your email.")));
    }

    @Test
    public void sendEmail_shouldReturnExceptionJson() throws Exception {
        RecoveryDTO recoveryDTO = new RecoveryDTO();
        recoveryDTO.setEmail("bad mail");
        String jsonRecoveryDTO = new ObjectMapper().writeValueAsString(recoveryDTO);

        mockMvc.perform(post("/api/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecoveryDTO))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("Email must be valid")));


        recoveryDTO.setEmail("not_existed_mail@mail.com");
        jsonRecoveryDTO = new ObjectMapper().writeValueAsString(recoveryDTO);

        mockMvc.perform(post("/api/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecoveryDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("User with email = " + recoveryDTO.getEmail() + " not found")));
    }

    @Test
    public void changePassword_shouldChangePassword() throws Exception {
        recoveryRestController.getRecoveryPageIdEmail().put(1, "user@mail.com");

        RecoveryDTO recoveryDTO = new RecoveryDTO();
        recoveryDTO.setPassword("User12345");
        recoveryDTO.setConfirmPassword("User12345");
        String jsonRecoveryDTO = new ObjectMapper().writeValueAsString(recoveryDTO);

        mockMvc.perform(post("/api/recovery/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecoveryDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("Password was successfully changed")));
    }

    @Test
    public void changePassword_shouldReturnNotFound() throws Exception {
        RecoveryDTO recoveryDTO = new RecoveryDTO();
        recoveryDTO.setPassword("User12345");
        recoveryDTO.setConfirmPassword("User12345");
        String jsonRecoveryDTO = new ObjectMapper().writeValueAsString(recoveryDTO);

        mockMvc.perform(post("/api/recovery/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRecoveryDTO))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
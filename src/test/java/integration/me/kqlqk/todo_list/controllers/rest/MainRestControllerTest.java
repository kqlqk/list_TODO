package integration.me.kqlqk.todo_list.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import integration.me.kqlqk.todo_list.IntegrationControllerParent;
import me.kqlqk.todo_list.dto.LoginDTO;
import me.kqlqk.todo_list.dto.RegistrationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MainRestControllerTest extends IntegrationControllerParent {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void login_shouldCorrectLogIn() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLoginObj("userLogin");
        loginDTO.setPassword("User1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginDTO = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.access_token", matchesRegex("^[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=.+/]*")))
                .andExpect(jsonPath("$.refresh_token", matchesRegex("^[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=.+/]*")));
    }

    @Test
    public void login_shouldIncorrectLogIn() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLoginObj("userLogin");
        loginDTO.setPassword("badPSWD");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLoginDTO = objectMapper.writeValueAsString(loginDTO);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.info", is("Email/Username or password incorrect")));


        loginDTO.setLoginObj("IncorrectLogin");
        jsonLoginDTO = objectMapper.writeValueAsString(loginDTO);
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.info", is("Email/Username or password incorrect")));
    }

    @Test
    public void signUp_shouldCorrectSignUp() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("newUser@mail.com");
        registrationDTO.setLogin("newUserLogin");
        registrationDTO.setPassword("newUser1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRegistrationDTO = objectMapper.writeValueAsString(registrationDTO);

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegistrationDTO))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.access_token", matchesRegex("^[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=.+/]*")))
                .andExpect(jsonPath("$.refresh_token", matchesRegex("^[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=.+/]*")));
    }

    @Test
    public void signUp_shouldIncorrectSignUp() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setEmail("user@mail.com");
        registrationDTO.setLogin("newUserLogin");
        registrationDTO.setPassword("newUser1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRegistrationDTO = objectMapper.writeValueAsString(registrationDTO);

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegistrationDTO))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.info", is("Email " + registrationDTO.getEmail() + " already registered")));


        registrationDTO.setEmail("newUser@mail.com");
        registrationDTO.setLogin("userLogin");
        jsonRegistrationDTO = objectMapper.writeValueAsString(registrationDTO);
        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegistrationDTO))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.info", is("Login " + registrationDTO.getLogin() + " already registered")));


        registrationDTO.setEmail("");
        jsonRegistrationDTO = objectMapper.writeValueAsString(registrationDTO);
        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegistrationDTO))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.info").exists());
    }
}
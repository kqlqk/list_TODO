package integration.me.kqlqk.todo_list.controllers.rest;

import integration.me.kqlqk.todo_list.IntegrationControllerParent;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRestControllerTest extends IntegrationControllerParent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private String adminRefreshToken;

    @BeforeEach
    public void init(){
        adminRefreshToken = refreshTokenService.updateRefreshToken(userService.getByEmail("admin@mail.com"));
    }

    @Test
    public void getAdminMenu_shouldReturnJson() throws Exception {
        mockMvc.perform(get("/api/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + adminRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.site_starting_worked").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users[0].id", is(1)))
                .andExpect(jsonPath("$.connection_to_db", is(true)));
    }
}
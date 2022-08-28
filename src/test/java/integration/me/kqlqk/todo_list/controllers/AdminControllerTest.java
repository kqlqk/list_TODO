package integration.me.kqlqk.todo_list.controllers;

import integration.me.kqlqk.todo_list.IntegrationControllerParent;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest extends IntegrationControllerParent {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    private String adminRefreshToken;

    @BeforeEach
    public void init(){
        adminRefreshToken = refreshTokenService.updateRefreshToken(userService.getByEmail("admin@mail.com"));
    }

    @Test
    public void getAdminMenu_shouldLoadContent() throws Exception {
        mockMvc.perform(get("/admin")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + adminRefreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("startTime"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("validConnection"))
                .andExpect(content().string(containsString("Admin menu")));
    }

    @Test
    public void testConnectionInDb_shouldTestConnection() throws Exception {
        mockMvc.perform(patch("/admin")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + adminRefreshToken))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }
}
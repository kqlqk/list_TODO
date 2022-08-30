package integration.me.kqlqk.todo_list.config.filters;

import annotations.TestController;
import me.kqlqk.todo_list.service.RefreshTokenService;
import me.kqlqk.todo_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestController
public class JWTFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    private String userRefreshToken;
    private String userAccessToken;

    @BeforeEach
    public void init(){
        Map<String, String> tokens = refreshTokenService.updateAccessAndRefreshTokens(userService.getByEmail("user@mail.com"));
        userAccessToken = tokens.get("access_token");
        userRefreshToken = tokens.get("refresh_token");
    }

    @Test
    public void jwtFilter_shouldNotCheckRequest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void jwtFilter_shouldCheckRequestAndCompleteCorrectly() throws Exception {
        mockMvc.perform(get("/api/notes")
                        .header("Authorization_access", "Bearer_" + userAccessToken)
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void jwtFilter_shouldCheckRequestWithIncorrectAccessTokenAndCompleteCorrectly() throws Exception {
        mockMvc.perform(get("/api/notes")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_" + userRefreshToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void jwtFilter_shouldCheckRequestWithIncorrectAccessAndRefreshTokenAndReturnExceptionJson() throws Exception {
        mockMvc.perform(get("/api/notes")
                        .header("Authorization_access", "Bearer_random")
                        .header("Authorization_refresh", "Bearer_random"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("Access and refresh tokens aren't valid, try to log in one more time")));
    }

    @Test
    public void jwtFilter_shouldCheckRequestWithIncorrectRefreshTokenAndCompleteWithExceptionJson() throws Exception {
        mockMvc.perform(get("/api/notes")
                        .header("Authorization_access", "Bearer_" + userAccessToken)
                        .header("Authorization_refresh", "Bearer_random"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.info", is("Refresh token aren't valid, try to log in one more time")));

    }

}
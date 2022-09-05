package integration.me.kqlqk.todo_list.service.impl;

import annotations.TestService;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestService
public class RefreshTokenServiceImplTest {

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private UserService userService;

    @Test
    public void createAndAddToken_shouldCreateAndAddToDbToken() {
        User user = new User();
        user.setEmail("random@mail.com");
        user.setLogin("randomLogin");
        user.setPassword("randomPswd1");
        userService.add(user);


        assertThat(user.getRefreshToken()).isNull();

        String token = refreshTokenService.createAndGetToken(user);

        assertThat(token).isNotNull();
        assertThat(user.getRefreshToken()).isNotNull();
    }

    @Test
    public void getEmail_shouldReturnValidEmail() {
        User user = new User();
        user.setEmail("random@mail.com");
        user.setLogin("randomLogin");
        user.setPassword("randomPswd1");
        userService.add(user);
        refreshTokenService.createAndGetToken(user);

        String email = refreshTokenService.getEmail(user.getRefreshToken().getToken());

        assertThat(email).isEqualTo("random@mail.com");
    }

    @Test
    public void updateRefreshToken_shouldUpdateRefreshToken() {
        User user = userService.getById(1);
        String oldToken = user.getRefreshToken().getToken();

        refreshTokenService.updateRefreshToken(user);

        String newToken = user.getRefreshToken().getToken();

        assertThat(oldToken).isNotEqualTo(newToken);
    }

    @Test
    public void updateAccessAndRefreshTokens_shouldUpdateRefreshAndAccessToken() {
        User user = userService.getById(1);
        String oldToken = user.getRefreshToken().getToken();

        Map<String, String> tokens = refreshTokenService.updateAccessAndRefreshTokens(user);

        assertThat(tokens).isNotNull();
        assertThat(tokens).isNotEmpty();

        String newToken = tokens.get("refresh_token");

        assertThat(oldToken).isNotEqualTo(newToken);
    }
}
package integration.me.kqlqk.todo_list.service.impl;

import annotations.TestService;
import me.kqlqk.todo_list.service.impl.AccessTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestService
public class AccessServiceImplTest {
    @Autowired
    private AccessTokenServiceImpl accessTokenService;

    @Test
    public void createToken_shouldReturnValidToken() {
        assertThat(accessTokenService.createAndGetToken("user@mail.com")).isNotNull();
    }

    @Test
    public void getEmail_shouldReturnValidEmail() {
        String email = accessTokenService.getEmail(accessTokenService.createAndGetToken("user@mail.com"));

        assertThat(email).isEqualTo("user@mail.com");
    }


    @Test
    public void isValid_shouldCheckToken() {
        String token = accessTokenService.createAndGetToken("user@mail.com");

        assertThat(accessTokenService.isValid(token)).isTrue();

        String invalidToken = token + 'g';

        assertThat(accessTokenService.isValid(invalidToken)).isFalse();
    }
}
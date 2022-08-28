package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.IntegrationServiceParent;
import me.kqlqk.todo_list.service.impl.AccessTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessServiceImplTest extends IntegrationServiceParent {
    @Autowired
    private AccessTokenServiceImpl accessTokenService;

    @Test
    public void createToken_shouldReturnValidToken() {
        assertThat(accessTokenService.createToken("user@mail.com")).isNotNull();
    }

    @Test
    public void getEmail_shouldReturnValidEmail() {
        String email = accessTokenService.getEmail(accessTokenService.createToken("user@mail.com"));

        assertThat(email).isEqualTo("user@mail.com");
    }


    @Test
    public void validateToken_shouldValidateToken() {
        String token = accessTokenService.createToken("user@mail.com");

        assertThat(accessTokenService.validateToken(token)).isTrue();

        String invalidToken = token + 'g';

        assertThat(accessTokenService.validateToken(invalidToken)).isFalse();
    }
}
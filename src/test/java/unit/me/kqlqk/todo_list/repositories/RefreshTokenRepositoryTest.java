package unit.me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshTokenRepositoryTest extends RepositoryParent {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Test
    public void existsById_shouldCheckIfExistsById() {
        assertThat(refreshTokenRepository.existsById(1)).isTrue();
        assertThat(refreshTokenRepository.existsById(99)).isFalse();
    }

    @Test
    public void getByUserId() {
        RefreshToken refreshToken = refreshTokenRepository.getByUserId(1);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getId()).isEqualTo(1);
        assertThat(refreshToken.getUser().getId()).isEqualTo(1);
    }
}
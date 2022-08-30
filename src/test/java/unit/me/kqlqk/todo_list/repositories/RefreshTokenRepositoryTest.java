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
    public void getByUserId_shouldGetByUserId() {
        RefreshToken refreshToken = refreshTokenRepository.getByUserId(1);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getId()).isEqualTo(1);
        assertThat(refreshToken.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void getByToken_shouldGetByStringToken(){
        RefreshToken refreshToken = refreshTokenRepository.getByToken(
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwiaWF0IjoxNjYwNDkzMzEzLCJleHAiOjk2NjMwODUzMTN9._dP6FpQNAkT7fX8KTeQ5JwCdZEosvSszTHRJ9a5lelA");

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getId()).isEqualTo(1);
    }
}
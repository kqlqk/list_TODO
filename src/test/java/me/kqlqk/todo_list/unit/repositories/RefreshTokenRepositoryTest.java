package me.kqlqk.todo_list.unit.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/boot-up-h2-db.sql", "/add-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RefreshTokenRepositoryTest {

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
package me.kqlqk.todo_list.unit.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/boot-up-h2-db.sql", "/add-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void getByEmail() {
        User user = userRepository.getByEmail("user@mail.com");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
        assertThat(user.getNotes().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void getById() {
        User user = userRepository.getById(1);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
        assertThat(user.getNotes().size()).isEqualTo(2);
    }

    @Test
    public void getByLogin() {
        User user = userRepository.getByLogin("userLogin");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
        assertThat(user.getNotes().size()).isEqualTo(2);
    }

    @Test
    public void getByRefreshToken() {
        RefreshToken refreshToken = refreshTokenRepository.getById(1L);

        User user = userRepository.getByRefreshToken(refreshToken);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
        assertThat(user.getNotes().size()).isEqualTo(2);
    }

    @Test
    public void existsByEmail() {
        assertThat(userRepository.existsByEmail("user@mail.com")).isTrue();
        assertThat(userRepository.existsByEmail("random@mail.com")).isFalse();
    }

    @Test
    public void existsById() {
        assertThat(userRepository.existsById(1)).isTrue();
        assertThat(userRepository.existsById(99)).isFalse();
    }

    @Test
    public void existsByLogin() {
        assertThat(userRepository.existsByLogin("userLogin")).isTrue();
        assertThat(userRepository.existsByLogin("randomLogin")).isFalse();
    }
}
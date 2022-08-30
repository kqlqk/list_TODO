package unit.me.kqlqk.todo_list.repositories;

import annotations.TestRepository;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestRepository
public class UserRepositoryTest{

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
    @Transactional
    public void getById_shouldThrowsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () -> userRepository.getById(99).getEmail());
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
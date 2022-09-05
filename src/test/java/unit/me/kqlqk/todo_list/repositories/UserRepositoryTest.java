package unit.me.kqlqk.todo_list.repositories;

import annotations.TestRepository;
import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.RefreshTokenRepository;
import me.kqlqk.todo_list.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestRepository
public class UserRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void findByEmail() {
        User user = userRepository.findByEmail("user@mail.com");
        System.out.println(user);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
    }

    @Test
    public void findById_shouldReturnValidUser() {
        User user = userRepository.findById(1);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
    }

    @Test
    public void findById_shouldReturnNull(){
        assertThat(userRepository.findById(99)).isNull();
    }

    @Test
    public void findByLogin() {
        User user = userRepository.findByLogin("userLogin");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
    }

    @Test
    public void findByRefreshToken() {
        RefreshToken refreshToken = refreshTokenRepository.getById(1L);

        User user = userRepository.findByRefreshToken(refreshToken);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("user@mail.com");
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
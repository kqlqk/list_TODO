package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents JPA repository for {@link me.kqlqk.todo_list.models.User}
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findById(long id);

    User findByLogin(String login);

    User findByRefreshToken(RefreshToken refreshToken);

    boolean existsByEmail(String email);

    boolean existsById(long id);

    boolean existsByLogin(String login);
}

package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents JPA repository for {@link me.kqlqk.todo_list.models.RefreshToken}
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsById(long id);

    RefreshToken findByUserId(long userId);

    RefreshToken findByToken(String token);
}

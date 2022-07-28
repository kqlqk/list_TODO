package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.RefreshToken;
import me.kqlqk.todo_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsById(long id);
    boolean existsByToken(String token);
    RefreshToken getByUser(User user);
}

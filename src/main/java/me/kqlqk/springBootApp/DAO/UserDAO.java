package me.kqlqk.springBootApp.DAO;

import me.kqlqk.springBootApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

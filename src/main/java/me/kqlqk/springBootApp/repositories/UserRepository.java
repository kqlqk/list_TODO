package me.kqlqk.springBootApp.repositories;

import me.kqlqk.springBootApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
}

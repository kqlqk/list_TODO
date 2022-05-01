package me.kqlqk.springBootApp.DAO;

import me.kqlqk.springBootApp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
}

package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents JPA repository for {@link me.kqlqk.todo_list.models.Role}
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(long id);
}

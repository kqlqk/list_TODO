package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getById(long id);
}

package me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
    User getByLogin(String login);
}

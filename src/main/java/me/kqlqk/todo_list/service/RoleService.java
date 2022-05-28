package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.Role;
import org.springframework.stereotype.Component;

@Component
public interface RoleService {
    Role getById(Long id);
}

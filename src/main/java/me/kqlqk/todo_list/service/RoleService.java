package me.kqlqk.todo_list.service;

import me.kqlqk.todo_list.models.Role;
import org.springframework.stereotype.Component;

/**
 * Represents service-layer for {@link me.kqlqk.todo_list.models.Role}
 */
@Component
public interface RoleService {
    Role getById(long id);
}

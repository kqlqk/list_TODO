package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.Role;
import org.springframework.stereotype.Component;

@Component
public interface RoleService {
    Role getById(Long id);
}

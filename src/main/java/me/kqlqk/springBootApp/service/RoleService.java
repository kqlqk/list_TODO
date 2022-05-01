package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.models.Role;

public interface RoleService {
    Role findById(Long id);
}

package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getById(long id) {
        return roleRepository.getById(id);
    }
}

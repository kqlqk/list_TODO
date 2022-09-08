package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents implementation for {@link me.kqlqk.todo_list.service.RoleService}
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    /**
     * @return {@link me.kqlqk.todo_list.models.Role} or null, if {@link me.kqlqk.todo_list.models.Role} not found
     */
    @Override
    public Role getById(long id) {
        return roleRepository.findById(id);
    }
}

package me.kqlqk.springBootApp.service.impl;

import me.kqlqk.springBootApp.DAO.RoleRepository;
import me.kqlqk.springBootApp.models.Role;
import me.kqlqk.springBootApp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //JPA-repository methods
    @Override
    public Role getById(Long id) {
        return roleRepository.getById(id);
    }
}

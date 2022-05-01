package me.kqlqk.springBootApp.service.impl;

import me.kqlqk.springBootApp.DAO.RoleDAO;
import me.kqlqk.springBootApp.models.Role;
import me.kqlqk.springBootApp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleDAO.findById(id);
        return role.get();
    }
}

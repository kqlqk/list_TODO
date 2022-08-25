package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.service.IntegrationServiceParent;
import me.kqlqk.todo_list.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceImplTest extends IntegrationServiceParent {
    @Autowired
    private RoleServiceImpl roleService;

    @Test
    @Transactional
    public void getById_shouldReturnNull(){
        assertThat(roleService.getById(99)).isNull();
    }

}
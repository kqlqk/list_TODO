package integration.me.kqlqk.todo_list.service.impl;

import annotations.TestService;
import me.kqlqk.todo_list.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@TestService
public class RoleServiceImplTest {
    @Autowired
    private RoleServiceImpl roleService;

    @Test
    @Transactional
    public void getById_shouldReturnNull(){
        assertThat(roleService.getById(99)).isNull();
    }

}
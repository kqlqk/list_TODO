package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.repositories.RoleRepository;
import me.kqlqk.todo_list.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void getById_shouldCallRoleRepository(){
        roleServiceImpl.getById(10L);

        verify(roleRepository, times(1)).getById(10L);
    }
}

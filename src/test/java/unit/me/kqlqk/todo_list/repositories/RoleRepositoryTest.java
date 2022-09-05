package unit.me.kqlqk.todo_list.repositories;

import annotations.TestRepository;
import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestRepository
public class RoleRepositoryTest{

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void findById() {
        Role role = roleRepository.findById(1);

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(1);
        assertThat(role.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    public void findById_shouldReturnNull(){
        assertThat(roleRepository.findById(99)).isNull();
    }
}
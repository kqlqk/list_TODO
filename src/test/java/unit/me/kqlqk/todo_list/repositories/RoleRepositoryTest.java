package unit.me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleRepositoryTest extends RepositoryParent{

    @Autowired
    private RoleRepository roleRepository;


    @Test
    @Transactional
    public void getById() {
        Role role = roleRepository.getById(1);

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(1);
        assertThat(role.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    @Transactional
    public void getById_shouldThrowsEntityNotFoundException(){
        assertThrows(EntityNotFoundException.class, () -> roleRepository.getById(99).getName());
    }
}
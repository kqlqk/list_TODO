package me.kqlqk.todo_list.unit.repositories;

import me.kqlqk.todo_list.models.Role;
import me.kqlqk.todo_list.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/boot-up-h2-db.sql", "/add-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleRepositoryTest {

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
}
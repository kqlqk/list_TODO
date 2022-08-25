package unit.me.kqlqk.todo_list.repositories;

import me.kqlqk.todo_list.Init;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = Init.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/boot-up-h2-db.sql", "/add-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class RepositoryParent {
}

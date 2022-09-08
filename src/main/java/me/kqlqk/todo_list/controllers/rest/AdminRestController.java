package me.kqlqk.todo_list.controllers.rest;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.dto.daoDTOs.UserDTO;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents rest endpoints for admins
 * <p>
 * For more information of endpoints please visit our <a href="https://github.com/kqlqk/list_TODO#rest-api">github repository</a>
 */
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final UserService userService;
    private final DataSource dataSource;

    @Autowired
    public AdminRestController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    /**
     * Represents <b>"/api/admin" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAdminMenu() {
        Map<String, Object> items = new HashMap<>();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMMM, HH:mm:ss");

        boolean conn;
        String log = null;
        try {
            dataSource.getConnection();
            conn = true;
        } catch (Exception e) {
            e.printStackTrace();
            conn = false;
            log = e.toString();
        }

        items.put("site_starting_worked", dateTimeFormat.format(Init.startTime));
        items.put("users", UserDTO.convertListOfUsersToListOfUserDTOs(userService.getAll()));
        items.put("connection_to_db", conn);
        if (!conn) {
            logger.warn(log);
            items.put("info", log);
        }

        return ResponseEntity.ok(items);
    }

}

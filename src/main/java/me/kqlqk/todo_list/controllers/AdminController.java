package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.dto.daoDTOs.UserDTO;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Represents endpoints for admins
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final UserService userService;
    private final DataSource dataSource;

    private boolean connection = true;
    private String log;

    @Autowired
    public AdminController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    /**
     * Represents <b>"/admin" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getAdminMenu(Model model) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");

        model.addAttribute("startTime", format.format(Init.startTime));
        model.addAttribute("users", UserDTO.convertListOfUsersToListOfUserDTOs(userService.getAll()));
        model.addAttribute("validConnection", connection);
        model.addAttribute("log", log);
        return "admin-pages/main";
    }

    /**
     * Represents <b>"/admin" [PATCH]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public String testConnectionInDb() {
        try {
            dataSource.getConnection();
            connection = true;
        } catch (SQLException e) {
            connection = false;
            log = e.toString();
            logger.warn(log);
        }

        return "redirect:/admin";
    }


}

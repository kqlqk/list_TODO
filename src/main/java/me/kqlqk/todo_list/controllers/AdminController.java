package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.dto.daoDTOs.UserDTO;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final DataSource dataSource;

    private boolean connection = true;
    private String log;

    @Autowired
    public AdminController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAdminMenu(Model model){
        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");

        model.addAttribute("startTime", format.format(Init.appStartTime));
        model.addAttribute("users", UserDTO.convertListOfUsersToListOfUserDTOs(userService.getAll()));
        model.addAttribute("validConnection", connection);
        model.addAttribute("log", log);
        return "admin-pages/main";
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public String testConnectionInDb(){
        try {
            dataSource.getConnection();
            connection = true;
        }
        catch (SQLException e){
            connection = false;
            log = e.toString();
        }

        return "redirect:/admin";
    }



}

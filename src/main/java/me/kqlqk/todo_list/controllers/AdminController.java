package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

@Controller
public class AdminController {
    private final UserService userService;

    private boolean connection = true;
    private String log;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin")
    public String getAdminMenu(Model model){
        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");

        model.addAttribute("startTime", format.format(Init.appStartTime));
        model.addAttribute("users", userService.getAll());
        model.addAttribute("validConnection", connection);
        model.addAttribute("log", log);
        return "admin-pages/mainAdminPage";
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/admin")
    public String testConnectionInDB(){
        try {
            UtilMethods.getDataSource().getConnection();
            connection = true;
        }
        catch (SQLException e){
            connection = false;
            log = e.getMessage();
        }

        return "redirect:/admin";
    }



}

package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;

@Controller
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin")
    public String getAdminMenu(Model model){
        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");
        model.addAttribute("startTime", format.format(Init.appStartTime));

        model.addAttribute("users", userService.getAll());

        return "admin-pages/mainAdminPage";
    }
}

package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;

@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getAdminMenu(Model model){
        logger.info("was get request to /admin by " + userService.getCurrentEmail());

        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");
        model.addAttribute("startTime", format.format(Init.appStartTime));

        return "admin-pages/mainAdminPage";
    }
}

package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.Init;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;

@Controller
public class AdminController {

    @RequestMapping(method = RequestMethod.GET, value = "/admin")
    public String getAdminMenu(Model model){

        SimpleDateFormat format = new SimpleDateFormat("MMMM-dd, HH:mm:ss");
        model.addAttribute("startTime", format.format(Init.appStartTime));

        return "admin-pages/mainAdminPage";
    }
}

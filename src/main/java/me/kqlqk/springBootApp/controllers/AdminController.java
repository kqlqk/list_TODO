package me.kqlqk.springBootApp.controllers;

import me.kqlqk.springBootApp.WebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String getAdminMenu(Model model){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd, HH:mm:ss");
        model.addAttribute("startTime", format.format(WebApplication.appTime));


        return "admin-pages/mainAdminPage";
    }
}

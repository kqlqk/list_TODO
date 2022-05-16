package me.kqlqk.springBootApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {


    @GetMapping("/error")
    public String handleError(HttpServletRequest request){
        int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status == 403){
            return "redirect:/login";
        }

        return "error-pages/errorPage";
    }
}

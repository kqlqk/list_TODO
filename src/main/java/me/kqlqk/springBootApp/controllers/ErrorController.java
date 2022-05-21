package me.kqlqk.springBootApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest request){
        int status = (int) (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null ?
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) : 0);

        String message = "";

        if((status + "").startsWith("4")){
            message = status + " Client error";
        }
        else if((status + "").startsWith("5")){
            message = status + " Server error";
        }

        if(status == 403){
            return "redirect:/login";
        }

        if(status == 404 || status == 400){
            message = status + " Page not found";
        }

        model.addAttribute("error", message);

        return "error-pages/errorPage";
    }
}

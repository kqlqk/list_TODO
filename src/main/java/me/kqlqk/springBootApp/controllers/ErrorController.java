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
            message = status + "(client error)";
        }
        else if((status + "").startsWith("5")){
            message = status + "(server error)";
        }

        model.addAttribute("error", message);

        if(status == 403){
            //FIXME: add groups of roles
            return "redirect:/login";
        }

        return "error-pages/errorPage";
    }
}

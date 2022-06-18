package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    private final UserService userService;

    @Autowired
    public ErrorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/error")
    public String handleError(Model model, HttpServletRequest request){
        logger.info("was get request to /error by " + userService.getCurrentEmail());
        int status = (int) (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null ?
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) : 0);

        logger.warn(userService.getCurrentEmail() + " got an " + "error: " + status);

        String message = "";

        if((status + "").startsWith("4")){
            message = status + " Client error";
        }
        else if((status + "").startsWith("5")){
            message = status + " Server error";
        }

        if(status == 403){
            if(userService.getCurrentUser() != null){
                message = status + " Sorry, you haven't permissions to do that.";
            }
            if(userService.getCurrentUser() == null) {
                return "redirect:/login";
            }
        }

        if(status == 404 || status == 400){
            message = status + " Page not found";
        }

        model.addAttribute("error", message);

        return "error-pages/errorPage";
    }
}

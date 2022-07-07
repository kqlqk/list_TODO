package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.service.ErrorsHandlerService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    private final UserService userService;
    private final ErrorsHandlerService errorsHandlerService;

    @Autowired
    public ErrorController(UserService userService, ErrorsHandlerService errorsHandlerService){
        this.userService = userService;
        this.errorsHandlerService = errorsHandlerService;
    }

    @GetMapping("/error")
    public String handleErrors(Model model, HttpServletRequest request){
        logger.warn("was get request to /error by " + userService.getCurrentEmail());

        model.addAttribute("error", errorsHandlerService.getErrorCodeWithDetails(request));

        return "error-pages/generalErrors";
    }

}

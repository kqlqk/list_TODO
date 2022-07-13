package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.service.ErrorsHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private final ErrorsHandlerService errorsHandlerService;

    @Autowired
    public ErrorController(ErrorsHandlerService errorsHandlerService){
        this.errorsHandlerService = errorsHandlerService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/error")
    public String handleErrors(Model model, HttpServletRequest request){
        model.addAttribute("error", errorsHandlerService.getErrorCodeWithDetails(request));

        return "error-pages/generalErrors";
    }

}

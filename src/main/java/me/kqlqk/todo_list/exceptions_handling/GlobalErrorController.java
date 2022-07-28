package me.kqlqk.todo_list.exceptions_handling;

import me.kqlqk.todo_list.dto.ExceptionDTO;
import me.kqlqk.todo_list.service.ErrorsHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GlobalErrorController implements org.springframework.boot.web.servlet.error.ErrorController{
    private static Exception e;
    private static boolean isRest;

    private final ErrorsHandlerService errorsHandlerService;

    @Autowired
    public GlobalErrorController(ErrorsHandlerService errorsHandlerService) {
        this.errorsHandlerService = errorsHandlerService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/error")
    public String handle(Model model, HttpServletRequest request){
        if(isRest) {
            return "redirect:/api/error";
        }

        String error = errorsHandlerService.getErrorCodeWithDetails(request);

        if(error.equals("shouldLogIn")){
            return "redirect:/login";
        }

        model.addAttribute("error", error);
        return "error-pages/generalErrors";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/error")
    public ResponseEntity<?> restHandle(){
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setInfo(e.getMessage());
        e = null;
        return new ResponseEntity<>(exceptionDTO, HttpStatus.FORBIDDEN);
    }

    public static void setInfo(Exception e, boolean isRest){
        GlobalErrorController.e = e;
        GlobalErrorController.isRest = isRest;
    }
}

package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.ErrorsHandlerService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Service
public class ErrorsHandlerServiceImpl implements ErrorsHandlerService {
    private final UserService userService;

    @Autowired
    public ErrorsHandlerServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public int getErrorCode(HttpServletRequest request) {
        if(request == null){
            throw new IllegalArgumentException("HttpServletRequest cannot be null");
        }

        return (int) (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null ?
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) : 0);
    }

    @Override
    public String getErrorCodeWithDetails(HttpServletRequest request) {
        if(request == null){
            throw new IllegalArgumentException("HttpServletRequest cannot be null");
        }

        int status = getErrorCode(request);

        String details = "";

        if (status == 400) {
            details = "Error " + status + " -  Bad request";
        }
        else if (status == 403) {
            if (userService.getCurrentUser() != null) {
                details = "Error " + status + " Page forbidden";
            }
            if (userService.getCurrentUser() == null) {
                //FIXME
                return "shouldLogIn";
            }
        }
        else if (status == 404) {
            details = "Error " + status + " Page not found";
        }
        else if (status == 500) {
            details = status + " Internal Server Error";
        }
        else if (status == 502) {
            details = status + " Bad Gateway";
        }

        return details;
    }

}

package me.kqlqk.todo_list.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ControllersUtil {

    public void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String status){
        if (request.getCookies().length != 0) {
            if (status.equals("on")) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(60 * 60 * 24 * 7);
                        response.addCookie(cookie);
                    }
                }
            } else {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(-1);
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

}

package me.kqlqk.todo_list.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilCookie {
    public static void createOrUpdateCookie(String name, String value, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = getCookieByName(name, request);
        if(cookie != null){
            cookie.setValue(value);
            cookie.setMaxAge(maxAge);
        }
        else{
            cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Cookie getCookieByName(String name, HttpServletRequest request){
        try {
            if(request.getCookies().length < 1){
                return null;
            }
        }
        catch (NullPointerException e){
            return null;
        }
        for(Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public static boolean isCookieExistsByName(String name, HttpServletRequest request){
        try {
            if(request.getCookies().length < 1){
                return false;
            }
        }
        catch (NullPointerException e){
            return false;
        }

        for(Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}

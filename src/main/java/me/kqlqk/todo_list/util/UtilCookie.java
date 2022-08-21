package me.kqlqk.todo_list.util;

import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletRequestNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.security.HttpServletResponseNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilCookie {
    public static void createOrUpdateCookie(String name, String value, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        if(name == null){
            throw new NullPointerException("Name cannot be null");
        }
        if(value == null){
            throw new NullPointerException("Value cannot be null");
        }
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }
        if(response == null){
            throw new HttpServletResponseNotFoundException("HttpServletResponse cannot be null");
        }
        if(maxAge < -1){
            throw new IllegalArgumentException("Max age cannot be less -1");
        }

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
        if(name == null){
            throw new NullPointerException("Name cannot be null");
        }
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }

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
        if(name == null){
            throw new NullPointerException("Name cannot be null");
        }
        if(request == null){
            throw new HttpServletRequestNotFoundException("HttpServletRequest cannot be null");
        }

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

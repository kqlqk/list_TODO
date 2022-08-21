package me.kqlqk.todo_list.exceptions_handling.exceptions.security;

public class HttpServletRequestNotFoundException extends RuntimeException{
    public HttpServletRequestNotFoundException(String message) {
        super(message);
    }
}

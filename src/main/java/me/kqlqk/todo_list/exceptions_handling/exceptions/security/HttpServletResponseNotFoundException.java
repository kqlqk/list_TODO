package me.kqlqk.todo_list.exceptions_handling.exceptions.security;

public class HttpServletResponseNotFoundException extends RuntimeException{
    public HttpServletResponseNotFoundException(String message) {
        super(message);
    }
}

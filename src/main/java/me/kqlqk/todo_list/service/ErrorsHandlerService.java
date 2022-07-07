package me.kqlqk.todo_list.service;

import javax.servlet.http.HttpServletRequest;

public interface ErrorsHandlerService {
    int getErrorCode(HttpServletRequest request);
    String getErrorCodeWithDetails(HttpServletRequest request);
}

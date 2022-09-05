package me.kqlqk.todo_list.util;

import me.kqlqk.todo_list.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilMethods {

    public static String getImprovedUrl(String url){
        if(url == null || url.equals("")){
            throw new IllegalArgumentException("Url cannot be null");
        }

        StringBuilder improvedUrl = new StringBuilder();

        List<String> words = new ArrayList<>(List.of(url.split("/")));

        for (String word : words) {
            if(word.equals("")|| word.equals("http:") || word.equals("localhost:8080")){
                continue;
            }
            improvedUrl.append(word).append("/");
        }

        improvedUrl.insert(0, "/");

        return improvedUrl.toString();
    }

    public static String getURLPath(JoinPoint joinPoint){
        if(joinPoint == null){
            throw new IllegalArgumentException("JoinPoint cannot be null");
        }

        StringBuilder path = new StringBuilder();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping currentMethodAnnotation = method.getAnnotation(RequestMapping.class);
        String[] methodURIs = currentMethodAnnotation.value();

        Class<?> declaringClass = method.getDeclaringClass();

        if(declaringClass.getAnnotation(RequestMapping.class) != null) {
            RequestMapping currentClassAnnotation = declaringClass.getAnnotation(RequestMapping.class);
            String[] classURIs = currentClassAnnotation.value();

            path.append(Arrays.toString(classURIs)
                    .replace("[", "")
                    .replace("]", ""));
            path.append(Arrays.toString(methodURIs)
                    .replace("[", "")
                    .replace("]", ""));
        }
        else {
            path.append(Arrays.toString(methodURIs)
                    .replace("[", "")
                    .replace("]", ""));
        }

        return path.toString();
    }

    public static String getUserFromJoinPoint(UserService userService){
        if (userService == null){
            throw new IllegalArgumentException("UserService cannot be null");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        try{
            return userService.getCurrentEmail();
        }
        catch (NullPointerException e){
            return request.getRemoteAddr();
        }
    }
}

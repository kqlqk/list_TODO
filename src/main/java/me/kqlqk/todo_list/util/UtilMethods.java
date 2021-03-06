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

    public static String getURLPath(JoinPoint joinPoint, String[] mappingValues){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        StringBuilder path = new StringBuilder();

        if(method.getDeclaringClass().getAnnotation(RequestMapping.class) != null) {
            path.append(Arrays.toString(method.getDeclaringClass().getAnnotation(RequestMapping.class).value()));
            path.append(Arrays.toString(mappingValues));
        }
        else {
            path.append(Arrays.toString(mappingValues));
        }
        if(path.indexOf("{id}") != -1){
            for(Object arg : joinPoint.getArgs()) {
                if (arg instanceof Long) {
                    path.replace(path.indexOf("{"), path.indexOf("{") + 4   , Long.toString((Long) arg));
                    break;
                }
            }
        }

        return path.toString();
    }

    public static String getUserFromJoinPoint(UserService userService){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        return userService.getCurrentEmail().equals("anonymousUser") ? request.getRemoteAddr() :  userService.getCurrentEmail();
    }
}

package me.kqlqk.todo_list.util;

import me.kqlqk.todo_list.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilMethods {
    @Value("${spring.datasource.driver-class-name}")
    private static String driverClassName;

    @Value("${spring.datasource.url}")
    private static String url;

    @Value("${spring.datasource.username}")
    private static String username;

    @Value("${spring.datasource.password}")
    private static String password;

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
                if (arg instanceof Integer) {
                    path.replace(path.indexOf("{"), path.indexOf("{") + 4   , Integer.toString((Integer) arg));
                    break;
                }
            }
        }

        return path.toString();
    }

    public static String getUserFromJoinPoint(JoinPoint joinPoint, UserService userService){
        HttpServletRequest request = null;

        for(Object arg : joinPoint.getArgs()){
            if(arg instanceof HttpServletRequest){
                request = (HttpServletRequest) arg;
                break;
            }
        }

        return userService.getCurrentEmail().equals("anonymousUser") ? request.getRemoteAddr() :  userService.getCurrentEmail();
    }

    public static DataSource getDataSource(){
        DataSourceBuilder dataSource = DataSourceBuilder.create();
        dataSource.driverClassName(driverClassName);
        dataSource.url(url);
        dataSource.username(username);
        dataSource.password(password);

        return dataSource.build();
    }

}

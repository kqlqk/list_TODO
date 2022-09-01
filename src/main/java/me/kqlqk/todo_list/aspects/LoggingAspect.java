package me.kqlqk.todo_list.aspects;

import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilMethods;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final UserService userService;

    @Autowired
    public LoggingAspect(UserService userService) {
        this.userService = userService;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void allMappingAnnotations(){}


    @Before("allMappingAnnotations()")
    public void beforeRequestMappingLoggingAdvice(JoinPoint joinPoint){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        RequestMapping currentRequestMappingAnnotation = method.getAnnotation(RequestMapping.class);

        String requestMethod = Arrays.toString(currentRequestMappingAnnotation.method());
        String path = UtilMethods.getURLPath(joinPoint);
        String user = UtilMethods.getUserFromJoinPoint(userService);
        logger.info("was " + requestMethod + " request to " + path + " by " + user);
    }

}

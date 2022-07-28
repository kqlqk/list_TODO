package me.kqlqk.todo_list.aspects;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilMethods;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    //CONTROLLERS ADVICES
    @Before("me.kqlqk.todo_list.aspects.Pointcuts.allMappingAnnotations()")
    public void beforeRequestMappingLoggingAdvice(JoinPoint joinPoint){
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        String requestMethod = Arrays.toString(method.getAnnotation(RequestMapping.class).method());
        String path = UtilMethods.getURLPath(joinPoint, method.getAnnotation(RequestMapping.class).value());
        String user = UtilMethods.getUserFromJoinPoint(userService);

        if(joinPoint.getSignature().getName().equals("handleErrors")){
            logger.warn("was " + requestMethod  + " request to " + path + " by " + user);
        }
        else{
            logger.info("was " + requestMethod + " request to " + path + " by " + user);
        }
    }


    //SERVICES ADVICES
    @AfterReturning("me.kqlqk.todo_list.aspects.Pointcuts.add()")
    public void afterAddUserOrNoteLoggingAdvice(JoinPoint joinPoint){
        for(Object arg : joinPoint.getArgs()){
            if(arg instanceof User){
                logger.info("was created " + arg);
                break;
            }
            if(arg instanceof Note){
                logger.info("was created " + arg);
                break;
            }
        }
    }

    @AfterReturning("me.kqlqk.todo_list.aspects.Pointcuts.update()")
    public void afterUpdateUserOrNoteLoggingAdvice(JoinPoint joinPoint){
        for(Object arg : joinPoint.getArgs()){
            if(arg instanceof User){
                logger.info("was updated " + arg);
                break;
            }
            if(arg instanceof Note){
                logger.info("was updated " + arg);
                break;
            }
        }
    }

    @AfterReturning("me.kqlqk.todo_list.aspects.Pointcuts.delete()")
    public void afterDeleteNoteLoggingAdvice(JoinPoint joinPoint){
        for(Object arg : joinPoint.getArgs()){
            if(arg instanceof Note){
                logger.info("was deleted " + arg);
                break;
            }
            if(arg instanceof Long){
                logger.info("was deleted note with id = " + arg);
                break;
            }
        }
    }

    @AfterReturning(pointcut = "me.kqlqk.todo_list.aspects.Pointcuts.userServiceImplConvertOAuth2()", returning = "user")
    public void afterConvertingOAuth2UserLoggingAdvice(User user) {
        logger.info("was converted and saved " + user);
    }

}

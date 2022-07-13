package me.kqlqk.todo_list.aspects;

import me.kqlqk.todo_list.exceptions.dao.note.NoteAlreadyExistException;
import me.kqlqk.todo_list.exceptions.dao.note.NoteNotFoundException;
import me.kqlqk.todo_list.exceptions.dao.user.UserNotFoundException;
import me.kqlqk.todo_list.exceptions.service_exceptions.AuthenticationNotAuthenticatedException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.util.UtilMethods;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
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
        String user = UtilMethods.getUserFromJoinPoint(joinPoint, userService);

        if(joinPoint.getSignature().getName().equals("handleErrors")){
            logger.warn("was " + requestMethod  + " request to " + path + " by " + user);
        }
        else{
            logger.info("was " + requestMethod + " request to " + path + " by " + user);
        }
    }

    @Around("me.kqlqk.todo_list.aspects.Pointcuts.allMappingAnnotations()")
    public Object aroundExceptionsInControllersLoggingAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();

        Object targetMethodResult = null;

        try {
            targetMethodResult = proceedingJoinPoint.proceed();
        }
        catch (Exception e) {
            String log = null;

            if(e instanceof NullPointerException && methodName.equals("sendEmail")){
                targetMethodResult = "recovery-pages/badRecovery";
            }

            if(e instanceof NoteNotFoundException && methodName.equals("deleteNote")){
                log = "was [DELETE] request for note that not exist for user " + userService.getCurrentUser();
                targetMethodResult = "redirect:/home";
            }

            if(e instanceof NoteAlreadyExistException && methodName.equals("showNewForm")){
                targetMethodResult = "redirect:/home";
            }

            if(e instanceof UserNotFoundException || e instanceof AuthenticationNotAuthenticatedException) {
                targetMethodResult = "redirect:/login";
            }

            logger.warn(log == null ?
                    UtilMethods.getUserFromJoinPoint(proceedingJoinPoint, userService) + " got " + e :
                    log);
        }

        return targetMethodResult;
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
    public void afterUpdateUserLoggingAdvice(JoinPoint joinPoint){
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
    public void afterDeleteLoggingAdvice(JoinPoint joinPoint){
        for(Object arg : joinPoint.getArgs()){
            if(arg instanceof Note){
                logger.info("was deleted " + arg);
                break;
            }
            if(arg instanceof Long){
                logger.info("was deleted note by id = " + arg);
                break;
            }
        }
    }

    @AfterReturning(pointcut = "me.kqlqk.todo_list.aspects.Pointcuts.userServiceImplConvertOAuth2()", returning = "user")
    public void afterConvertingOAuth2UserLoggingAdvice(User user) {
        logger.info("was converted and saved " + user);
    }

}

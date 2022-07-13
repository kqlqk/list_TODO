package me.kqlqk.todo_list.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void allMappingAnnotations(){}

    @Pointcut("execution(* add(..)) && within(me.kqlqk.todo_list.service.impl..*)")
    public void add(){}

    @Pointcut("execution(* update(..)) && within(me.kqlqk.todo_list.service.impl..*)")
    public void update(){}

    @Pointcut("execution(* me.kqlqk.todo_list.service.impl.NoteServiceImpl.delete(..))")
    public void delete(){}

    @Pointcut("execution(* me.kqlqk.todo_list.service.impl.UserServiceImpl.convertOAuth2UserToUserAndSave(..))")
    public void userServiceImplConvertOAuth2(){}
}

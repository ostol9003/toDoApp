package io.github.ostol9003.ToDoApp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogicAspect {
    private final Timer projectCreateGroupTimer;

    public LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Around("execution(* io.github.ostol9003.ToDoApp.logic.ProjectService.createGroup(..))")
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp){
        return projectCreateGroupTimer.record(()->
        {
            try {
                return  jp.proceed();
            } catch (Throwable e) {
                if(e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}

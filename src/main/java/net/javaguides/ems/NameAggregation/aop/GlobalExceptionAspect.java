package net.javaguides.ems.NameAggregation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class GlobalExceptionAspect {

    @Pointcut("execution(* net.javaguides.ems.NameAggregation.controller.NameAggregationController.*(..))")
    public void nameAggregationMethods() {}

    @Before("nameAggregationMethods()")
    public void validateRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Map) {
            Map<?, ?> request = (Map<?, ?>) args[0];
            Object names = request.get("name");
            if (names == null || (names instanceof List && ((List<?>) names).isEmpty())) {
                throw new IllegalArgumentException("Request body must contain a non-empty 'name' field");
            }
        }
    }

    @Around("nameAggregationMethods()")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
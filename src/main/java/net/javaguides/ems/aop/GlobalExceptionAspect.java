package net.javaguides.ems.aop;

import lombok.extern.slf4j.Slf4j;
import net.javaguides.ems.exception.DownStreamServiceException;
import net.javaguides.ems.exception.InvalidRequestException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import net.javaguides.ems.exception.InvalidRequestException;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class GlobalExceptionAspect {

    @Around("execution(* net.javaguides.ems.controller..*(..))")
    public Object handleControllerExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (InvalidRequestException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (DownStreamServiceException e) {
            log.error("Downstream service error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
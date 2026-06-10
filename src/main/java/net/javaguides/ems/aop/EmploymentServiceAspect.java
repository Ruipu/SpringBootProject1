package net.javaguides.ems.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmploymentServiceAspect {
        private static final Logger log =
                LoggerFactory.getLogger(EmploymentServiceAspect.class);
        // Targets only the employee service
        @Pointcut("execution(* net.javaguides.ems.service.impl.EmployeeServiceImpl.*(..))")
        public void serviceMethod() {
        }

        // Runs before every service method
        @Before("serviceMethod()")
        public void logBefore(JoinPoint jp) {
            log.info("[BEFORE] {}",jp.getSignature().toShortString());
        }

        // Runs after a service method returns
        @AfterReturning(pointcut = "serviceMethod()", returning = "result")
        public void logAfterReturning(JoinPoint jp, Object result) {
            log.info(
                    "[AFTER RETURNING] {} => {}",
                    jp.getSignature().toShortString(),
                    result
            );
        }

        // Runs when a service method throws an exception
        @AfterThrowing(
                pointcut = "serviceMethod()",
                throwing = "ex"
        )
        public void logAfterThrowing(JoinPoint jp, Exception ex) {
            log.warn(
                    "[AFTER THROWING] {}",
                    jp.getSignature().toShortString(),
                    ex.getMessage()
            );
        }

        // Runs after service method regardless of outcome
        @After("serviceMethod()")
        public void logAfter(JoinPoint jp) {
            log.info(
                    "[AFTER] {} completed",
                    jp.getSignature().toShortString()
            );
        }

        // Wraps every service method - measures and logs execution time
        @Around("serviceMethod()")
        public Object measureTime(ProceedingJoinPoint pjp)
                throws Throwable {
                long start = System.currentTimeMillis();
                Object result = pjp.proceed();
                long duration =
                    System.currentTimeMillis() - start;
                log.info(
                    "[AROUND] {} took {}ms",
                    pjp.getSignature().toShortString(),
                    duration
            );
            return result;
        }
    }


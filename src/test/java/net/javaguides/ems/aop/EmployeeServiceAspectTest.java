package net.javaguides.ems.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceAspectTest {

    @InjectMocks
    private EmploymentServiceAspect aspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private Signature signature;

    @BeforeEach
    void setUp() {
        lenient().when(joinPoint.getSignature()).thenReturn(signature);
        lenient().when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        lenient().when(signature.toShortString()).thenReturn("EmployeeServiceImpl.createEmployee(..)");
    }

    @Test
    @DisplayName("serviceMethod pointcut - should exist without error")
    void serviceMethod_Pointcut() {
        assertDoesNotThrow(() -> aspect.serviceMethod());
    }

    // ==================== logBefore Tests ====================

    @Test
    @DisplayName("logBefore - should log before method execution")
    void logBefore_Success() {
        assertDoesNotThrow(() -> aspect.logBefore(joinPoint));
        verify(joinPoint, times(1)).getSignature();
    }

    // ==================== logAfterReturning Tests ====================

    @Test
    @DisplayName("logAfterReturning - should log with return value")
    void logAfterReturning_WithResult() {
        Object result = "some result";
        assertDoesNotThrow(() -> aspect.logAfterReturning(joinPoint, result));
        verify(joinPoint, times(1)).getSignature();
    }

    @Test
    @DisplayName("logAfterReturning - should log with null return value")
    void logAfterReturning_NullResult() {
        assertDoesNotThrow(() -> aspect.logAfterReturning(joinPoint, null));
        verify(joinPoint, times(1)).getSignature();
    }

    // ==================== logAfterThrowing Tests ====================

    @Test
    @DisplayName("logAfterThrowing - should log exception info")
    void logAfterThrowing_Success() {
        Exception ex = new RuntimeException("Something went wrong");
        assertDoesNotThrow(() -> aspect.logAfterThrowing(joinPoint, ex));
        verify(joinPoint, times(1)).getSignature();
    }

    // ==================== logAfter Tests ====================

    @Test
    @DisplayName("logAfter - should log after method completion")
    void logAfter_Success() {
        assertDoesNotThrow(() -> aspect.logAfter(joinPoint));
        verify(joinPoint, times(1)).getSignature();
    }

    // ==================== measureTime Tests ====================

    @Test
    @DisplayName("measureTime - should proceed and return result")
    void measureTime_Success() throws Throwable {
        Object expectedResult = "expected";
        when(proceedingJoinPoint.proceed()).thenReturn(expectedResult);

        Object result = aspect.measureTime(proceedingJoinPoint);

        assertEquals(expectedResult, result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("measureTime - should propagate exception from proceed")
    void measureTime_ThrowsException() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Error"));

        assertThrows(RuntimeException.class, () -> aspect.measureTime(proceedingJoinPoint));
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("measureTime - should handle null return from proceed")
    void measureTime_NullResult() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(null);

        Object result = aspect.measureTime(proceedingJoinPoint);

        assertNull(result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}

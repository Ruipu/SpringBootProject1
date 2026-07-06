package net.javaguides.ems.NameAggregationaop;


import net.javaguides.ems.NameAggregation.aop.GlobalExceptionAspect;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionAspectTest {

    @InjectMocks
    private GlobalExceptionAspect aspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    // ==================== pointcut ====================

    @Test
    @DisplayName("nameAggregationMethods pointcut - should exist without error")
    void nameAggregationMethods_Pointcut() {
        assertDoesNotThrow(() -> aspect.nameAggregationMethods());
    }

    // ==================== validateRequest Tests ====================

    @Test
    @DisplayName("validateRequest - should pass with valid name list")
    void validateRequest_ValidNames() {
        Map<String, List<String>> request = Map.of("name", List.of("Jessica", "Simon"));
        when(joinPoint.getArgs()).thenReturn(new Object[]{request});

        assertDoesNotThrow(() -> aspect.validateRequest(joinPoint));
    }

    @Test
    @DisplayName("validateRequest - should throw when name is null")
    void validateRequest_NullNames() {
        Map<String, Object> request = new HashMap<>();
        request.put("name", null);
        when(joinPoint.getArgs()).thenReturn(new Object[]{request});

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> aspect.validateRequest(joinPoint)
        );
        assertTrue(ex.getMessage().contains("non-empty"));
    }

    @Test
    @DisplayName("validateRequest - should throw when name list is empty")
    void validateRequest_EmptyNameList() {
        Map<String, List<String>> request = Map.of("name", new ArrayList<>());
        when(joinPoint.getArgs()).thenReturn(new Object[]{request});

        assertThrows(IllegalArgumentException.class, () -> aspect.validateRequest(joinPoint));
    }

    @Test
    @DisplayName("validateRequest - should pass when no args")
    void validateRequest_NoArgs() {
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.validateRequest(joinPoint));
    }

    @Test
    @DisplayName("validateRequest - should pass when arg is not a Map")
    void validateRequest_ArgNotMap() {
        when(joinPoint.getArgs()).thenReturn(new Object[]{"not a map"});

        assertDoesNotThrow(() -> aspect.validateRequest(joinPoint));
    }

    // ==================== handleExceptions Tests ====================

    @Test
    @DisplayName("handleExceptions - should return result on success")
    void handleExceptions_Success() throws Throwable {
        ResponseEntity<?> expected = ResponseEntity.ok(Map.of("name", List.of("Simon")));
        when(proceedingJoinPoint.proceed()).thenReturn(expected);

        Object result = aspect.handleExceptions(proceedingJoinPoint);

        assertEquals(expected, result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("handleExceptions - should return 400 on IllegalArgumentException")
    void handleExceptions_IllegalArgument() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenThrow(new IllegalArgumentException("bad request"));

        Object result = aspect.handleExceptions(proceedingJoinPoint);

        assertInstanceOf(ResponseEntity.class, result);
        ResponseEntity<?> response = (ResponseEntity<?>) result;
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("bad request", body.get("error"));
    }

    @Test
    @DisplayName("handleExceptions - should return 500 on unexpected exception")
    void handleExceptions_UnexpectedException() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("something broke"));

        Object result = aspect.handleExceptions(proceedingJoinPoint);

        assertInstanceOf(ResponseEntity.class, result);
        ResponseEntity<?> response = (ResponseEntity<?>) result;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("Internal server error", body.get("error"));
    }
}
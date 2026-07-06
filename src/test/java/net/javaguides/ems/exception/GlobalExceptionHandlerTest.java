package net.javaguides.ems.exception;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    // ==================== handleNotFound Tests ====================

    @Test
    @DisplayName("handleNotFound - should return 404 with message")
    void handleNotFound_Success() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Employee not found with id: 1");

        ErrorResponse response = globalExceptionHandler.handleNotFound(ex);

        assertEquals(404, response.getStatus());
        assertEquals("Employee not found with id: 1", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    // ==================== handleNoResource Tests ====================

    @Test
    @DisplayName("handleNoResource - should return 404 with message")
    void handleNoResource_Success() throws Exception {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(ex.getMessage()).thenReturn("No static resource api/v1/unknown");
        ErrorResponse response = globalExceptionHandler.handleNoResource(ex);

        assertEquals(404, response.getStatus());
        assertNotNull(response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    // ==================== handleValidation Tests ====================

    @Test
    @DisplayName("handleValidation - should return 400 with field error message")
    void handleValidation_Success() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "employeeDto");
        bindingResult.addError(new FieldError("employeeDto", "email", "Email is required"));

        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getDeclaredMethod("handleValidation_Success"), -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ErrorResponse response = globalExceptionHandler.handleValidation(ex);

        assertEquals(400, response.getStatus());
        assertEquals("Email is required", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    // ==================== handleAll Tests ====================

    @Test
    @DisplayName("handleAll - should return 500 with unexpected error message")
    void handleAll_Success() {
        Exception ex = new Exception("Something broke");

        ErrorResponse response = globalExceptionHandler.handleAll(ex);

        assertEquals(500, response.getStatus());
        assertEquals("Unexpected error: Something broke", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("handleAll - should handle null message")
    void handleAll_NullMessage() {
        Exception ex = new Exception();

        ErrorResponse response = globalExceptionHandler.handleAll(ex);

        assertEquals(500, response.getStatus());
        assertEquals("Unexpected error: null", response.getMessage());
        assertNotNull(response.getTimestamp());
    }
}

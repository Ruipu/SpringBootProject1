package net.javaguides.ems.controller;


import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeController2Test {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController2 employeeController;

    private EmployeeDto sampleDto;
    private String userAgent;

    @BeforeEach
    void setUp() {
        sampleDto = new EmployeeDto();
        sampleDto.setId(1L);
        sampleDto.setFirstName("Simon");
        sampleDto.setLastName("Gao");
        sampleDto.setEmail("simon@test.com");
        userAgent = "JUnit-Test-Agent";
    }

    // ==================== createEmployee Tests ====================

    @Test
    @DisplayName("createEmployee - should return CREATED status and saved employee")
    void createEmployee_Success() {
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(sampleDto);

        ResponseEntity<EmployeeDto> response = employeeController.createEmployee(sampleDto, userAgent);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Simon", response.getBody().getFirstName());
        assertEquals("Gao", response.getBody().getLastName());
        assertEquals("simon@test.com", response.getBody().getEmail());
        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class));
    }

    @Test
    @DisplayName("createEmployee - should work with null User-Agent")
    void createEmployee_NullUserAgent() {
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(sampleDto);

        ResponseEntity<EmployeeDto> response = employeeController.createEmployee(sampleDto, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class));
    }

    // ==================== getEmployeeById Tests ====================

    @Test
    @DisplayName("getEmployeeById - should return OK and employee")
    void getEmployeeById_Success() {
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleDto);

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1L, userAgent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Simon", response.getBody().getFirstName());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @DisplayName("getEmployeeById - should work with null User-Agent")
    void getEmployeeById_NullUserAgent() {
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleDto);

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    // ==================== getAllEmployees Tests ====================

    @Test
    @DisplayName("getAllEmployees - should return OK and list of employees")
    void getAllEmployees_Success() {
        EmployeeDto secondDto = new EmployeeDto();
        secondDto.setId(2L);
        secondDto.setFirstName("Jessica");
        secondDto.setLastName("Li");
        secondDto.setEmail("jessica@test.com");

        List<EmployeeDto> employeeList = Arrays.asList(sampleDto, secondDto);
        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees(userAgent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("getAllEmployees - should return OK and empty list when no employees")
    void getAllEmployees_EmptyList() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees(userAgent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("getAllEmployees - should work with null User-Agent")
    void getAllEmployees_NullUserAgent() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(employeeService, times(1)).getAllEmployees();
    }

    // ==================== updateEmployee Tests ====================

    @Test
    @DisplayName("updateEmployee - should return OK and updated employee")
    void updateEmployee_Success() {
        EmployeeDto updatedDto = new EmployeeDto();
        updatedDto.setId(1L);
        updatedDto.setFirstName("UpdatedSimon");
        updatedDto.setLastName("UpdatedGao");
        updatedDto.setEmail("updated@test.com");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(updatedDto);

        ResponseEntity<EmployeeDto> response = employeeController.updateEmployee(1L, updatedDto, userAgent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UpdatedSimon", response.getBody().getFirstName());
        assertEquals("updated@test.com", response.getBody().getEmail());
        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    @Test
    @DisplayName("updateEmployee - should work with null User-Agent")
    void updateEmployee_NullUserAgent() {
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(sampleDto);

        ResponseEntity<EmployeeDto> response = employeeController.updateEmployee(1L, sampleDto, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    // ==================== deleteEmployee Tests ====================

    @Test
    @DisplayName("deleteEmployee - should return OK and success message")
    void deleteEmployee_Success() {
        doNothing().when(employeeService).deleteEmployee(1L);

        ResponseEntity<String> response = employeeController.deleteEmployee(1L, userAgent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    @DisplayName("deleteEmployee - should work with null User-Agent")
    void deleteEmployee_NullUserAgent() {
        doNothing().when(employeeService).deleteEmployee(1L);

        ResponseEntity<String> response = employeeController.deleteEmployee(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
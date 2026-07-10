package net.javaguides.ems.service;


import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.exception.ResourceNotFoundException;
import net.javaguides.ems.repository.EmployeeRepository;
import net.javaguides.ems.service.NotificationService;
import net.javaguides.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sampleEmployee;
    private EmployeeDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee(1L, "Simon", "Gao", "simon@test.com",null);
        sampleDto = new EmployeeDto(1L, "Simon", "Gao", "simon@test.com",null);
    }

    // ==================== createEmployee Tests ====================

    @Test
    @DisplayName("createEmployee - should save and return employee")
    void createEmployee_Success() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);
        doNothing().when(notificationService).sendNotification();

        EmployeeDto result = employeeService.createEmployee(sampleDto);

        assertNotNull(result);
        assertEquals("Simon", result.getFirstName());
        assertEquals("Gao", result.getLastName());
        assertEquals("simon@test.com", result.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(notificationService, times(1)).sendNotification();
    }

    // ==================== getEmployeeById Tests ====================

    @Test
    @DisplayName("getEmployeeById - should return employee when found")
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        EmployeeDto result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Simon", result.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getEmployeeById - should throw exception when not found")
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(employeeRepository, times(1)).findById(99L);
    }

    // ==================== getAllEmployees Tests ====================

    @Test
    @DisplayName("getAllEmployees - should return list of employees")
    void getAllEmployees_Success() {
        Employee secondEmployee = new Employee(2L, "Jessica", "Li", "jessica@test.com",null);
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sampleEmployee, secondEmployee));

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Simon", result.get(0).getFirstName());
        assertEquals("Jessica", result.get(1).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllEmployees - should return empty list when no employees")
    void getAllEmployees_EmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    // ==================== updateEmployee Tests ====================

    @Test
    @DisplayName("updateEmployee - should update and return employee")
    void updateEmployee_Success() {
        EmployeeDto updatedDto = new EmployeeDto(1L, "UpdatedSimon", "UpdatedGao", "updated@test.com",null);
        Employee updatedEmployee = new Employee(1L, "UpdatedSimon", "UpdatedGao", "updated@test.com",null);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeDto result = employeeService.updateEmployee(1L, updatedDto);

        assertNotNull(result);
        assertEquals("UpdatedSimon", result.getFirstName());
        assertEquals("UpdatedGao", result.getLastName());
        assertEquals("updated@test.com", result.getEmail());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("updateEmployee - should throw exception when not found")
    void updateEmployee_NotFound() {
        EmployeeDto updatedDto = new EmployeeDto(99L, "Test", "Test", "test@test.com",null);
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(99L, updatedDto)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ==================== deleteEmployee Tests ====================

    @Test
    @DisplayName("deleteEmployee - should delete employee when found")
    void deleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        doNothing().when(employeeRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEmployee - should throw exception when not found")
    void deleteEmployee_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(99L)
        );

        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}

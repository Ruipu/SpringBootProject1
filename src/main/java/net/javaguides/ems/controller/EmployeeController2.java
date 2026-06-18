package net.javaguides.ems.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")// + URL on method
@Tag(name = "Employee API", description = "Employee Management REST APIs")
public class EmployeeController2 {
    private EmployeeService employeeService;
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Build Add Employee REST API
    @Operation(summary = "Create a new employee")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid
                                                      @RequestBody EmployeeDto employeeDto,
                                                      @RequestHeader(value = "User-Agent", required = false)
                                                      String userAgent) {
        log.info("Create request from: {}", userAgent);
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Build Get Employee REST API
    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}") // /api/v1/employees/{id}"
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long employeeId,
                                                       @RequestHeader(value = "User-Agent", required = false)
                                                        String userAgent) {
        log.info("Request from: {}", userAgent);
        log.info("Fetching employee with id: {}", employeeId);
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeDto);
    }

    //Build Search Employee REST API
    @Operation(summary = "Search employee by name")
    @GetMapping("/search")
    public ResponseEntity<String> searchEmployee(
            @RequestParam String name,@RequestHeader(value = "User-Agent", required = false) String userAgent) {
        log.info("Request of searching from: {}", userAgent);
        log.info("Searching employee with name: {}", name);
        return ResponseEntity.ok(
                "Searching employee: " + name);
    }

    //Build Get All Employee REST API
    @Operation(summary = "Get all employees")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestHeader(value = "User-Agent", required = false)
                                                                 String userAgent) {
        log.info("Request of getting all from: {}", userAgent);
        log.info("Fetching all employees");
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    //Build Update Employee REST API
    @Operation(summary = "Update employee by id")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long employeeId,
                                                      @RequestBody EmployeeDto updatedEmployeeDto
                                                        ,@RequestHeader(value = "User-Agent", required = false)
                                                      String userAgent) {
        log.info("Request updating from: {}", userAgent);
        log.info("Updating employee with id: {}", employeeId);
        EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    //Build Delete Employee REST API
    @Operation(summary = "Delete employee by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId,
                                                 @RequestHeader(value = "User-Agent", required = false)
                                                String userAgent) {
        log.info("Request deleting from: {}", userAgent);
        log.info("Deleting employee with id: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        log.info("Employee deleted successfully. id={}", employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}

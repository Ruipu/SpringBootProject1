package net.javaguides.ems.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.service.EmployeeService;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employees") // + URL on method

//@Scope("singleton") -> default scope
//@Scope("prototype")
////@Scope("requestScope")
////@Scope("SessionScope")
////@Scope("application")
////@Scope("websocket")


// different classs
// @RequestMapping("/api/salary")
// @RequestMapping("/api/profile")
//@RequestMapping("/api/manager")

// A 0% youtube users -> 3s
// B 100% youtube users  -> 10s -> 20s -> 30s
//     -> 100 million users ??? watch 10s ?%, watch 5S ?%, watch 3S ?%
//     -> mouser hovering on ads?
//     -> click the ads

// old clients() ->
// @RequestMapping("v1/api/employees") -> version1 -> 2010 ->  10 years

// new clients
// @RequestMapping("v2/api/employees")
public class EmployeeController {
    private EmployeeService employeeService;
//    public EmployeeController(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }

    /**
     *
     *
     * @Lazy or setter DI
     *
     * @Component
     * class A {
     *     B b;
     *
     * }
     *
     * @Component
     * class B {
     *     @Lazy
     *     A a;
     *
     * }
     *
     *
     */

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Build Add Employee REST API
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid
                                                      @RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);

    }
    //Build Get Employee REST API
    @GetMapping("/{id}") // /api/employees/{id}"
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employeeDto);
    }
    //Build Search Employee REST API
    @GetMapping("/search")
    public ResponseEntity<String> searchEmployee(
            @RequestParam String name) {
        return ResponseEntity.ok(
                "Searching employee: " + name);
    }
    //Build Get All Employee REST API
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    //Build Update Employee REST API
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
                                                      @RequestBody EmployeeDto updatedEmployeeDto) {
        EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    //Build Delete Employee REST API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}


// ioc -> DI -> spring bean -> scopes(singleton), prototype, request, session, application, webscoket
// controller1 // service 1(connection timeout -> 1s to 3s), 2, 3, 4, 5 ->
//public class YoutubeVideoNonPremiumController { // videoService(enable) -> play ads in 60s
//    Service1 s1
//
//
//}

// YoutubeVideoPremiumController // videoService(enable) -> play ads in 3s and then enabl skip button





package net.javaguides.ems.service.impl;

import net.javaguides.ems.entity.Department;
import net.javaguides.ems.kafka.event.EmployeeEvent;
import net.javaguides.ems.kafka.producer.EmployeeEventProducer;
import net.javaguides.ems.service.NotificationService;
import org.springframework.cache.annotation.Cacheable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.exception.ResourceNotFoundException;
import net.javaguides.ems.mapper.EmployeeMapper;
import net.javaguides.ems.repository.EmployeeRepository;
import net.javaguides.ems.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

// (3-tier MVC layer)

// @Controller
// @RestController


// @Repository



@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private NotificationService notificationService;
    private EmployeeEventProducer employeeEventProducer;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating employee");
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        employeeEventProducer.publish(new EmployeeEvent(
                EmployeeEvent.EventType.CREATED,
                savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getEmail()
        ));
//        employeeEventProducer.publish(new EmployeeEvent(
//                EmployeeEvent.EventType.CREATED,
//                employee.getId(),
//                employee.getFirstName(),
//                employee.getLastName(),
//                employee.getEmail()
//        ));

        notificationService.sendNotification();
        log.info("Employee created successfully");
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    @Cacheable(
            value = "employees",
            key = "#employeeId"
    )
    public EmployeeDto getEmployeeById(Long employeeId) {
        log.info("Getting employee with id: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(()->
                    new ResourceNotFoundException("Employee not found with id: " + employeeId));
        log.info("Employee found successfully");
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("Getting all employees");
        List<Employee> employees = employeeRepository.findAll();
        log.info("Retrieved {} employees", employees.size());
        return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
                .collect(java.util.stream.Collectors.toList());

    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployeeDto) {
        log.info("Updating employee with id: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()
                -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        employee.setFirstName(updatedEmployeeDto.getFirstName());
        employee.setLastName(updatedEmployeeDto.getLastName());
        employee.setEmail(updatedEmployeeDto.getEmail());
        employee.setDepartment(updatedEmployeeDto.getDepartment());
        Employee updatedEmployeeObj = employeeRepository.save(employee);

        employeeEventProducer.publish(new EmployeeEvent(
                EmployeeEvent.EventType.UPDATED,
                updatedEmployeeObj.getId(),
                updatedEmployeeObj.getFirstName(),
                updatedEmployeeObj.getLastName(),
                updatedEmployeeObj.getEmail()
        ));

        log.info("Employee updated successfully");
        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Deleting employee with id: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found with id: " + employeeId)
        );
        for (Department department : new HashSet<>(employee.getDepartments())) {
            employee.removeDepartment(department);
        }
        employeeRepository.deleteById(employeeId);

        employeeEventProducer.publish(new EmployeeEvent(
                EmployeeEvent.EventType.DELETED,
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        ));

        log.info("Employee deleted successfully");
    }
    @Override
    public List<EmployeeDto> searchEmployees(String query) {
        log.info("Searching employees with query: {}", query);
        List<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
        return employees.stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(java.util.stream.Collectors.toList());
    }
    @Override
    public Page<EmployeeDto> getEmployeesPaged(String query, Pageable pageable) {
        log.info("Getting paged employees with query: {}", query);
        Page<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, pageable);
        return employees.map(EmployeeMapper::mapToEmployeeDto);
    }
}

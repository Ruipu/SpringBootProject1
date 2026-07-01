package net.javaguides.ems.repository;
import net.javaguides.ems.entity.Department;
import net.javaguides.ems.entity.Employee;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeHibernate {
        Employee save(Employee employee);
        Optional<Employee> findById(Long id);
        List<Employee> findAll();
        Employee update(Employee employee);
        void deleteById(Long id);
        // ─── Many-to-Many
        Employee addDepartmentToEmployee(Long employeeID, Long departmentId);
        Employee removeDepartmentFromEmployee(Long employeeId, Long departmentId);
        Set<Department> findDepartmentsByEmployeeId(Long employeeId);
}


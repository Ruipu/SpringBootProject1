package net.javaguides.ems.repository;
import net.javaguides.ems.entity.Employee;
import java.util.List;
import java.util.Optional;
public interface EmployeeHibernate {

        Employee save(Employee employee);
        Optional<Employee> findById(Long id);
        List<Employee> findAll();
        Employee update(Employee employee);
        void deleteById(Long id);
}


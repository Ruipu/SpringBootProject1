package net.javaguides.ems.repository;


import net.javaguides.ems.entity.Department;
import net.javaguides.ems.entity.Employee;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DepartmentHibernate {

    Department save(Department department);
    Optional<Department> findById(Long id);
    List<Department> findAll();
    Department update(Department department);
    void deleteById(Long id);
    Set<Employee> findEmployeesByDepartmentId(Long departmentId);
}
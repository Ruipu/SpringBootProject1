package net.javaguides.ems.repository.impl;
import net.javaguides.ems.entity.Department;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.repository.EmployeeHibernate;
import net.javaguides.ems.repository.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class EmployeeHibernateImpl implements EmployeeHibernate {
    private final SessionFactory sessionFactory;
    public EmployeeHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    // ─── CREATE ──────────────────────────────────────────────
    @Override
    public Employee save(Employee employee) {
        getSession().persist(employee);
        return employee;
    }

    // ─── READ ONE ────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        Employee employee = getSession().get(Employee.class, id);
        return Optional.ofNullable(employee);
    }

    // ─── READ ALL ────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return getSession()
                .createQuery("FROM Employee", Employee.class)
                .list();
    }

    // ─── UPDATE ──────────────────────────────────────────────
    @Override
    public Employee update(Employee employee) {
        getSession().merge(employee);
        return employee;
    }

    // ─── DELETE ──────────────────────────────────────────────
    @Override
    public void deleteById(Long id) {
        Employee employee = getSession().find(Employee.class, id);
        if (employee != null) {
            getSession().remove(employee);
        }
    }
    // ─── Many-to-Many ─────────────────────────────────────
    @Override
    public Employee addDepartmentToEmployee(Long employeeId, Long departmentKey) {
        Employee employee = getSession().find(Employee.class, employeeId);
        Department department = getSession().find(Department.class, departmentKey);
        if (employee != null && department != null) {
            employee.addDepartment(department);
        }
        return employee;
    }

    @Override
    public Employee removeDepartmentFromEmployee(Long employeeId, Long departmentKey) {
        Employee employee = getSession().find(Employee.class, employeeId);
        Department department = getSession().find(Department.class, departmentKey);
        if (employee != null && department != null) {
            employee.removeDepartment(department);
        }
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Department> findDepartmentsByEmployeeId(Long employeeId) {
        Employee employee = getSession().find(Employee.class, employeeId);
        if (employee != null) {
            return employee.getDepartments();
        }
        return new HashSet<>();
    }
}

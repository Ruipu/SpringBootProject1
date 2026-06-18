package net.javaguides.ems.repository.impl;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.repository.EmployeeHibernate;
import net.javaguides.ems.repository.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
}

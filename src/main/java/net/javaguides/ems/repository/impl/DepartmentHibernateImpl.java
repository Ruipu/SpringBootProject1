package net.javaguides.ems.repository.impl;

import jakarta.persistence.EntityManagerFactory;
import net.javaguides.ems.entity.Department;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.repository.DepartmentHibernate;
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
public class DepartmentHibernateImpl implements DepartmentHibernate {
    private final SessionFactory sessionFactory;

    public DepartmentHibernateImpl(EntityManagerFactory emf) {
        this.sessionFactory = emf.unwrap(SessionFactory.class);
    }
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    // ─── CREATE ───────────────────────────────────────────
    @Override
    public Department save(Department department) {
        getSession().persist(department);
        return department;
    }
    // ─── READ ONE ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {
        Department department = getSession().find(Department.class, id);
        return Optional.ofNullable(department);
    }
    // ─── READ ALL ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return getSession()
                .createQuery("FROM Department", Department.class)
                .list();
    }
    // ─── UPDATE ───────────────────────────────────────────
    @Override
    public Department update(Department department) {
        getSession().merge(department);
        return department;
    }
    // ─── DELETE ───────────────────────────────────────────
    @Override
    public void deleteById(Long id) {
        Department department = getSession().find(Department.class, id);
        if (department != null) {
            getSession().remove(department);
        }
    }
    // Many to Many
    @Override
    @Transactional(readOnly = true)
    public Set<Employee> findEmployeesByDepartmentId(Long departmentId) {
        Department department = getSession().find(Department.class, departmentId);
        if (department != null) {
            return department.getEmployees();
        }
        return new HashSet<>();
    }
}
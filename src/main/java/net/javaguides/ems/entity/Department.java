package net.javaguides.ems.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long id;

    @Column(name = "name", nullable = false)
    private String departmentName;

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    // ─── Constructors ─────────────────────────────────────
    public Department() {}
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    // ─── Getters & Setters ────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}


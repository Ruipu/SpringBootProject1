package net.javaguides.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email_id", nullable = false, unique = true)
    private String email;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "employee_department",
            joinColumns = @JoinColumn(
                    name = "employee_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "department_key"
            )
    )
    private Set<Department> departments = new HashSet<>();
    public void addDepartment(Department department) {
        this.departments.add(department);
        department.getEmployees().add(this);
    }

    public void removeDepartment(Department department) {
        this.departments.remove(department);
        department.getEmployees().remove(this);
    }

    // ─── Constructors ─────────────────────────────────────
    public Employee() {}

    public Employee(Long id, String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
    }

    // ─── Getters & Setters ────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Department> getDepartments() { return departments; }
    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }
}

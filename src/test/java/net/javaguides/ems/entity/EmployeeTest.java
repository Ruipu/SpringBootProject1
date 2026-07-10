package net.javaguides.ems.entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    @DisplayName("no-arg constructor - should create empty Employee")
    void noArgConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
        assertNull(employee.getId());
        assertNull(employee.getFirstName());
        assertNotNull(employee.getDepartments());
        assertTrue(employee.getDepartments().isEmpty());
    }

    @Test
    @DisplayName("all-args constructor - should set all fields")
    void allArgsConstructor() {
        Set<Department> depts = new HashSet<>();
        Employee employee = new Employee(1L, "Simon", "Gao", "simon@test.com", null);
        assertEquals(1L, employee.getId());
        assertEquals("Simon", employee.getFirstName());
        assertEquals("Gao", employee.getLastName());
        assertEquals("simon@test.com", employee.getEmail());
        assertSame(depts, employee.getDepartments());
    }

    @Test
    @DisplayName("4-arg constructor - should set basic fields")
    void fourArgConstructor() {
        Employee employee = new Employee(1L, "Simon", "Gao", "simon@test.com", null);
        assertEquals(1L, employee.getId());
        assertEquals("Simon", employee.getFirstName());
        assertEquals("Gao", employee.getLastName());
        assertEquals("simon@test.com", employee.getEmail());
    }

    @Test
    @DisplayName("setters and getters - should work correctly")
    void settersAndGetters() {
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setFirstName("Jessica");
        employee.setLastName("Li");
        employee.setEmail("jessica@test.com");
        Set<Department> depts = new HashSet<>();
        employee.setDepartments(depts);

        assertEquals(2L, employee.getId());
        assertEquals("Jessica", employee.getFirstName());
        assertEquals("Li", employee.getLastName());
        assertEquals("jessica@test.com", employee.getEmail());
        assertSame(depts, employee.getDepartments());
    }

    @Test
    @DisplayName("addDepartment - should add department and sync employee")
    void addDepartment() {
        Employee employee = new Employee(1L, "Simon", "Gao", "simon@test.com",null);
        Department dept = new Department("Engineering");

        employee.addDepartment(dept);

        assertEquals(1, employee.getDepartments().size());
        assertTrue(employee.getDepartments().contains(dept));
        assertTrue(dept.getEmployees().contains(employee));
    }

    @Test
    @DisplayName("removeDepartment - should remove department and sync employee")
    void removeDepartment() {
        Employee employee = new Employee(1L, "Simon", "Gao", "simon@test.com",null);
        Department dept = new Department("Engineering");
        employee.addDepartment(dept);

        employee.removeDepartment(dept);

        assertTrue(employee.getDepartments().isEmpty());
        assertFalse(dept.getEmployees().contains(employee));
    }
}

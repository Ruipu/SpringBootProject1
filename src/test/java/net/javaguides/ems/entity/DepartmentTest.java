package net.javaguides.ems.entity;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    @DisplayName("no-arg constructor - should create empty Department")
    void noArgConstructor() {
        Department dept = new Department();
        assertNotNull(dept);
        assertNull(dept.getId());
        assertNull(dept.getDepartmentName());
        assertNotNull(dept.getEmployees());
        assertTrue(dept.getEmployees().isEmpty());
    }

    @Test
    @DisplayName("one-arg constructor - should set department name")
    void oneArgConstructor() {
        Department dept = new Department("Engineering");
        assertEquals("Engineering", dept.getDepartmentName());
    }

    @Test
    @DisplayName("setters and getters - should work correctly")
    void settersAndGetters() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setDepartmentName("Sales");
        Set<Employee> employees = new HashSet<>();
        dept.setEmployees(employees);

        assertEquals(1L, dept.getId());
        assertEquals("Sales", dept.getDepartmentName());
        assertSame(employees, dept.getEmployees());
    }
}

package net.javaguides.ems.repository.impl;

import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.repository.EmployeeJDBC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class EmployeeJDBCimpl implements EmployeeJDBC {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeJDBCimpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ─── RowMapper ───────────────────────────────────────────────────────────────
    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setEmail(rs.getString("email"));
            return employee;
        }
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────────
    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getEmail());
            return ps;
        }, keyHolder);

        employee.setId(keyHolder.getKey().longValue());
        return employee;
    }

    // ─── READ ONE ────────────────────────────────────────────────────────────────
    @Override
    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";

        List<Employee> results = jdbcTemplate.query(sql, new EmployeeRowMapper(), id);
        return results.stream().findFirst();
    }

    // ─── READ ALL ────────────────────────────────────────────────────────────────
    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────
    @Override
    public Employee update(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getId()
        );

        return employee;
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

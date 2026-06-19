//package net.javaguides.ems.repository.impl;
//
//import lombok.RequiredArgsConstructor;
//import net.javaguides.ems.entity.Employee;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//import java.sql.PreparedStatement;
//import java.sql.Statement;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class EmployeeJDBCimpl {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    private static final RowMapper<Employee> ROW_MAPPER = (rs, rowNum) -> new Employee(
//            rs.getLong("id"),
//            rs.getString("first_name"),
//            rs.getString("last_name"),
//            rs.getString("email_id")
//    );
//
//    public Employee save(Employee employee) {
//        if (employee.getId() == null) {
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//            jdbcTemplate.update(con -> {
//                PreparedStatement ps = con.prepareStatement(
//                        "INSERT INTO employees (first_name, last_name, email_id) VALUES (?, ?, ?)",
//                        Statement.RETURN_GENERATED_KEYS);
//                ps.setString(1, employee.getFirstName());
//                ps.setString(2, employee.getLastName());
//                ps.setString(3, employee.getEmail());
//                return ps;
//            }, keyHolder);
//            employee.setId(keyHolder.getKey().longValue());
//        } else {
//            jdbcTemplate.update(
//                    "UPDATE employees SET first_name = ?, last_name = ?, email_id = ? WHERE id = ?",
//                    employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getId());
//        }
//        return employee;
//    }
//
//    public Optional<Employee> findById(Long id) {
//        List<Employee> results = jdbcTemplate.query(
//                "SELECT * FROM employees WHERE id = ?", ROW_MAPPER, id);
//        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
//    }
//
//    public List<Employee> findAll() {
//        return jdbcTemplate.query("SELECT * FROM employees", ROW_MAPPER);
//    }
//
//    public void deleteById(Long id) {
//        jdbcTemplate.update("DELETE FROM employees WHERE id = ?", id);
//    }
//
//    public long count() {
//        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employees", Long.class);
//        return count != null ? count : 0L;
//    }
//}
package testCases.ut;

import org.example.Model.Employee;
import org.example.ServiceImpl.SalaryRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryRuleTest {
    private SalaryRule salaryRule;
    private Map<Integer, Employee> employees;
    private Map<Integer, List<Employee>> managerToSubordinates;

    @BeforeEach
    void setUp() {
        salaryRule = new SalaryRule();
        employees = new HashMap<>();
        managerToSubordinates = new HashMap<>();

        Employee ceo = new Employee(1, "John", "CEO", 150000, null);
        Employee manager1 = new Employee(2, "Mary", "Manager1", 60000, 1); // underpaid
        Employee manager2 = new Employee(3, "Bob", "Manager2", 120000, 1); // overpaid
        Employee emp1 = new Employee(4, "Alice", "Lead1", 30000, 2);
        Employee emp2 = new Employee(5, "Tom", "Dev1", 20000, 2);

        employees.put(1, ceo);
        employees.put(2, manager1);
        employees.put(3, manager2);
        employees.put(4, emp1);
        employees.put(5, emp2);

        managerToSubordinates.put(1, List.of(manager1, manager2));
        managerToSubordinates.put(2, List.of(emp1, emp2));

    }

    @Test
    void testUnderpaidManager() {
        employees.get(2).getSalary(); // optional to debug
        employees.put(2, new Employee(2, "Mary", "Manager1", 20_000, 1));

        List<String> results = salaryRule.validate(employees, managerToSubordinates);
        assertTrue(results.stream().anyMatch(s -> s.contains("Mary")));
        assertFalse(results.stream().anyMatch(s -> s.contains("Bob")));
    }

    @Test
    void testOverpaidManager() {
        Employee sub = new Employee(6, "Jake", "Dev2", 50000, 3);
        employees.put(6, sub);
        managerToSubordinates.put(3, List.of(sub));
        List<String> results = salaryRule.validate(employees, managerToSubordinates);
        assertTrue(results.stream().anyMatch(s -> s.contains("Bob")));
    }

    @Test
    void testSalaryWithinRange() {
        employees.put(2, new Employee(2, "Mary", "Manager1", 32_000, 1));
        List<String> results = salaryRule.validate(employees, managerToSubordinates);
        assertFalse(results.stream().anyMatch(s -> s.contains("Mary")));
    }

    @Test
    void testEmployeeWithNoSubordinatesSkipped() {
        // Employee 4 (Alice) has no subordinates → skipped
        List<String> results = salaryRule.validate(employees, managerToSubordinates);
        assertFalse(results.stream().anyMatch(s -> s.contains("Alice")));
    }
}

package testCases.ut;

import org.example.Model.Employee;
import org.example.ServiceImpl.ReportingLineRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportingLineRuleTest {
    private ReportingLineRule reportingLineRule;
    private Map<Integer, Employee> employees;
    private Map<Integer, List<Employee>> managerToSubordinates;

    @BeforeEach
    void setUp() {
        reportingLineRule = new ReportingLineRule();
        employees = new HashMap<>();
        managerToSubordinates = new HashMap<>();

        // Build sample hierarchy
        Employee ceo = new Employee(1, "John", "CEO", 150000, null);
        Employee m1 = new Employee(2, "Mary", "Manager1", 60000, 1);
        Employee m2 = new Employee(3, "Bob", "Manager2", 70000, 1);
        Employee e1 = new Employee(4, "Alice", "Lead1", 30000, 2);
        Employee e2 = new Employee(5, "Tom", "Dev1", 20000, 4);
        Employee e3 = new Employee(6, "Kate", "Dev2", 22000, 5); // depth 4
        Employee e4 = new Employee(7, "Sam", "Intern", 15000, 6); // depth 5 → should trigger

        employees.put(1, ceo);
        employees.put(2, m1);
        employees.put(3, m2);
        employees.put(4, e1);
        employees.put(5, e2);
        employees.put(6, e3);
        employees.put(7, e4);

        managerToSubordinates.put(1, List.of(m1, m2));
        managerToSubordinates.put(2, List.of(e1));
        managerToSubordinates.put(4, List.of(e2));
        managerToSubordinates.put(5, List.of(e3));
        managerToSubordinates.put(6, List.of(e4));
    }

    @Test
    void testReportingLineWithinLimit() {
        List<String> results = reportingLineRule.validate(employees, managerToSubordinates);
        assertTrue(results.stream().anyMatch(s -> s.contains("Sam")));
        assertFalse(results.stream().anyMatch(s -> s.contains("John")));
    }

    @Test
    void testEmployeeWithTooLongReportingLine() {
        List<String> results = reportingLineRule.validate(employees, managerToSubordinates);
        assertTrue(results.stream().anyMatch(s -> s.contains("Sam") && s.contains("too long")));
    }

    @Test
    void testCEOAlwaysNoIssue() {
        List<String> results = reportingLineRule.validate(employees, managerToSubordinates);
        assertFalse(results.stream().anyMatch(s -> s.contains("John")), "CEO should not appear in reporting issues");
    }
}

package org.example.ServiceImpl;

import org.example.Model.Employee;
import org.example.OrgRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OrgAnalyzer {
    private final Map<Integer, Employee> employees = new HashMap<>();
    private final Map<Integer, List<Employee>> managerToSubordinates = new HashMap<>();
    private Employee ceo;

    public void loadEmployees(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            lines.skip(1)
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] parts = line.split(",", -1);

                        String idStr = parts[0].replaceAll("\"", "").trim();
                        if (idStr.isEmpty()) return;

                        int id;
                        try {
                            id = Integer.parseInt(idStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid employee ID: " + idStr);
                            return;
                        }

                        String firstName = parts[1].trim();
                        String lastName = parts[2].trim();

                        double salary;
                        try {
                            salary = Double.parseDouble(parts[3].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid salary for employee ID " + idStr + ": " + parts[3]);
                            return;
                        }

                        String mgr = parts[4].trim().replace("\"", "");
                        Integer managerId = mgr.isBlank() ? null : Integer.parseInt(mgr);

                        Employee emp = new Employee(id, firstName, lastName, salary, managerId);
                        employees.put(id, emp);

                        if (managerId == null) {
                            if (ceo != null) {
                                System.err.println("Warning: Multiple CEOs detected! Previous: " + ceo + ", New: " + emp);
                            }
                            ceo = emp;
                        } else {
                            managerToSubordinates.computeIfAbsent(managerId, k -> new ArrayList<>()).add(emp);
                        }
                    });
        }
    }

    public Map<String, List<String>> runRules(List<OrgRule> rules) {
        Map<String, List<String>> results = new HashMap<>();

        for (OrgRule rule : rules) {
            List<String> output = rule.validate(employees, managerToSubordinates);
            if (!output.isEmpty()) {
                results.put(rule.getClass().getSimpleName(), output);
            }
        }

        return results;
    }

    // Getter for testing
    public Map<Integer, Employee> getEmployees() { return employees; }
    public Employee getCeo() { return ceo; }

}

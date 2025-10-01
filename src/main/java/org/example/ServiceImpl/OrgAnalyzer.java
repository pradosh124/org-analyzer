package org.example.ServiceImpl;

import org.example.Model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgAnalyzer {
    private Map<Integer, Employee> employees = new HashMap<>();
    private Map<Integer, List<Employee>> managerToSubordinates = new HashMap<>();
    private Employee ceo;

    // Load CSV
    public void loadEmployees(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines.remove(0); // remove header

        for (String line : lines) {
            String[] parts = line.split(",", -1);
            String idStr = parts[0].replaceAll("\"", "").trim();
            if (idStr.isEmpty()) {
                continue;
            }
            int id = Integer.parseInt(idStr);
            String firstName = parts[1].trim();
            String lastName = parts[2].trim();
            double salary = Double.parseDouble(parts[3].trim());
            String mgr = parts[4].trim().replace("\"", "");
            Integer managerId = mgr.isBlank() ? null : Integer.parseInt(mgr);

            Employee emp = new Employee(id, firstName, lastName, salary, managerId);
            employees.put(id, emp);

            if (managerId == null) {
                ceo = emp;
            } else {
                managerToSubordinates.computeIfAbsent(managerId, k -> new ArrayList<>()).add(emp);
            }
        }
    }

    // Rule 1: Salary check
    public Map<String, List<String>> checkManagerSalaries() {
        List<String> underpaid = new ArrayList<>();
        List<String> overpaid = new ArrayList<>();

        for (Employee manager : employees.values()) {
            List<Employee> subs = managerToSubordinates.get(manager.getId());
            if (subs == null || subs.isEmpty()) continue;

            double avgSalary = subs.stream().mapToDouble(Employee::getSalary).average().orElse(0);
            double minAllowed = avgSalary * 1.2;
            double maxAllowed = avgSalary * 1.5;

            if (manager.getSalary() < minAllowed) {
                underpaid.add(manager + " earns less than they should by " + String.format("%.2f", (minAllowed - manager.getSalary())));
            } else if (manager.getSalary() > maxAllowed) {
                overpaid.add(manager + " earns more than they should by " + String.format("%.2f", (manager.getSalary() - maxAllowed)));
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("underpaid", underpaid);
        result.put("overpaid", overpaid);
        return result;
    }


    // Rule 2: Reporting line too long
    public List<String> checkReportingLines() {
        List<String> results = new ArrayList<>();
        for (Employee emp : employees.values()) {
            int depth = countManagers(emp);
            if (depth > 4) {
                results.add(emp + " have a reporting line which is too long, and by " + (depth - 4) );
            }
        }
        return results;
    }

    private int countManagers(Employee emp) {
        int depth = 0;
        Integer mgrId = emp.getManagerId();
        while (mgrId != null) {
            depth++;
            mgrId = employees.get(mgrId).getManagerId();
        }
        return depth;
    }
}

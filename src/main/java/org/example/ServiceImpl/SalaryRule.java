package org.example.ServiceImpl;

import org.example.Model.Employee;
import org.example.OrgRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalaryRule implements OrgRule {
    private static final double MIN_MULTIPLIER = 1.2;
    private static final double MAX_MULTIPLIER = 1.5;
    @Override
    public List<String> validate(Map<Integer, Employee> employees, Map<Integer, List<Employee>> managerToSubordinates) {
        List<String> results = new ArrayList<>();

        for (Employee manager : employees.values()) {
            List<Employee> subs = managerToSubordinates.get(manager.getId());
            if (subs == null || subs.isEmpty()) continue;

            double avgSalary = subs.stream().mapToDouble(Employee::getSalary).average().orElse(0);
            double minAllowed = avgSalary * MIN_MULTIPLIER;
            double maxAllowed = avgSalary * MAX_MULTIPLIER;

            if (manager.getSalary() < minAllowed) {
                results.add(manager + " earns LESS than allowed by " + String.format("%.2f", minAllowed - manager.getSalary()));
            } else if (manager.getSalary() > maxAllowed) {
                results.add(manager + " earns MORE than allowed by " + String.format("%.2f", manager.getSalary() - maxAllowed));
            }
        }

        return results;
    }
}

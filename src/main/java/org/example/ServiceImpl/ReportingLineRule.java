package org.example.ServiceImpl;

import org.example.Model.Employee;
import org.example.OrgRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportingLineRule implements OrgRule {
    private static final int MAX_DEPTH = 4;

    @Override
    public List<String> validate(Map<Integer, Employee> employees, Map<Integer, List<Employee>> managerToSubordinates) {
        List<String> results = new ArrayList<>();
        for (Employee emp : employees.values()) {
            int depth = countManagers(emp,employees);
            if (depth > 4) {
                results.add(emp + " have a reporting line which is too long, and by " + (depth - 4) );
            }
        }
        return results;
    }

    private int countManagers(Employee emp, Map<Integer, Employee> employees) {
        int depth = 0;
        Integer mgrId = emp.getManagerId();
        while (mgrId != null) {
            depth++;
            mgrId = employees.get(mgrId).getManagerId();
        }
        return depth;
    }
}

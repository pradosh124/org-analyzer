package org.example;

import org.example.Model.Employee;

import java.util.List;
import java.util.Map;

public interface OrgRule {
    List<String> validate(Map<Integer, Employee> employees, Map<Integer, List<Employee>> managerToSubordinates);
}

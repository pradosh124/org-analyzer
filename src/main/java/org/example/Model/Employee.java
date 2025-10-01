package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private Integer managerId; // null for CEO

    // Constructor
    public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getSalary() { return salary; }
    public Integer getManagerId() { return managerId; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (id=" + id + ", salary=" + salary + ")";
    }
}

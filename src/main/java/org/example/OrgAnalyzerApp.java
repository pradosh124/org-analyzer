package org.example;

import org.example.ServiceImpl.OrgAnalyzer;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class OrgAnalyzerApp {
    public static void main(String[] args) throws IOException {
        OrgAnalyzer analyzer = new OrgAnalyzer();
        analyzer.loadEmployees("src/main/resources/employees1.csv");

        Map<String, List<String>> salaryIssues = analyzer.checkManagerSalaries();
        System.out.println("\n=== Underpaid Managers ===");
        List<String> underpaid = salaryIssues.get("underpaid");
        if (underpaid.isEmpty()) {
            System.out.println("No issues in salary, all OK");
        }
        else{
            underpaid.forEach(System.out::println);
        }

        System.out.println("\n=== Overpaid Managers ===");
        List<String> overpaid = salaryIssues.get("overpaid");
        if (overpaid.isEmpty()) {
            System.out.println("No issues in salary, all OK");
        }
        else {
            overpaid.forEach(System.out::println);
        }

        System.out.println("\n=== Reporting Line Issues ===");
        List<String> reportingIssues = analyzer.checkReportingLines();

        if (reportingIssues.isEmpty()) {
            System.out.println("No issues in reporting, all OK");
        } else {
            reportingIssues.forEach(System.out::println);
        }
    }
}
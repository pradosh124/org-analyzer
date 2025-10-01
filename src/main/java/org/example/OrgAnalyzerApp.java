package org.example;

import org.example.ServiceImpl.OrgAnalyzer;
import org.example.ServiceImpl.ReportingLineRule;
import org.example.ServiceImpl.SalaryRule;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class OrgAnalyzerApp {
    public static void main(String[] args) throws IOException {
        OrgAnalyzer analyzer = new OrgAnalyzer();
        analyzer.loadEmployees("src/main/resources/employees1.csv");

        List<OrgRule> rules = List.of(new SalaryRule(), new ReportingLineRule());
        Map<String, List<String>> issues = analyzer.runRules(rules);

        issues.forEach((ruleName, messages) -> {
            System.out.println("\n=== " + ruleName + " ===");
            messages.forEach(System.out::println);
        });

        if (issues.isEmpty()) {
            System.out.println("\nNo issues found. All OK!");
        }
    }
}
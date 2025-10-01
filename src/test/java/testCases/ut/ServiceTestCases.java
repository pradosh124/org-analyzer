package testCases.ut;

import org.example.ServiceImpl.OrgAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

 class ServiceTestCases {
    private OrgAnalyzer analyzer;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        analyzer = new OrgAnalyzer();

        // Load CSV from resources folder
        URL resource = getClass().getClassLoader().getResource("employees.csv");
        assertNotNull(resource, "employees.csv not found in resources");

        String filePath = Paths.get(resource.toURI()).toString();
        analyzer.loadEmployees(filePath);
    }

     @Test
     void testManagerSalaryChecksWhenOnlyUnderPaidManagers() {

         Map<String, List<String>> salaryIssues = analyzer.checkManagerSalaries();

         List<String> underpaid = salaryIssues.get("underpaid");
         List<String> overpaid = salaryIssues.get("overpaid");
         assertTrue(overpaid.isEmpty());
         assertTrue(underpaid.stream().anyMatch(s -> s.contains("Martin")));
     }
    @Test
    void testManagerSalaryChecks() throws IOException, URISyntaxException {
        analyzer = new OrgAnalyzer();

        // Load CSV from resources folder
        URL resource = getClass().getClassLoader().getResource("employees1.csv");
        assertNotNull(resource, "employees1.csv not found in resources");

        String filePath = Paths.get(resource.toURI()).toString();
        analyzer.loadEmployees(filePath);
        Map<String, List<String>> salaryIssues = analyzer.checkManagerSalaries();

        List<String> underpaid = salaryIssues.get("underpaid");
        List<String> overpaid = salaryIssues.get("overpaid");

        // Mary (Manager1) has sub Alice (30k) -> minAllowed=36k, Mary=60k -> overpaid
        assertTrue(overpaid.stream().anyMatch(s -> s.contains("Mary")));

        // John (CEO) has sub Mary (60k) and Bob (70k) -> avg=65k, max=97.5k, John=150k -> overpaid
        assertTrue(overpaid.stream().anyMatch(s -> s.contains("John")));

        assertTrue(underpaid.stream().anyMatch(s -> s.contains("Lisa")));
    }

    @Test
    void testReportingLineTooLong() throws IOException, URISyntaxException {
        analyzer = new OrgAnalyzer();

        // Load CSV from resources folder
        URL resource = getClass().getClassLoader().getResource("employees1.csv");
        assertNotNull(resource, "employees1.csv not found in resources");

        String filePath = Paths.get(resource.toURI()).toString();
        analyzer.loadEmployees(filePath);
        List<String> reportingIssues = analyzer.checkReportingLines();

        // Depth >4 employees: Mark (depth 5), Lucy (depth 6)
        assertTrue(reportingIssues.stream().anyMatch(s -> s.contains("Mark")));
        assertTrue(reportingIssues.stream().anyMatch(s -> s.contains("Lucy")));

        // Employees with depth <=4: Tom, Kate, Alice -> not reported
        assertFalse(reportingIssues.stream().anyMatch(s -> s.contains("Tom")));
        assertFalse(reportingIssues.stream().anyMatch(s -> s.contains("Alice")));
    }

     @Test
    void testNoReportingLineIssuesForShortChains() throws IOException {
        // Create a CSV with depth ≤4
        OrgAnalyzer analyzer2 = new OrgAnalyzer();
        Path tempFile = Files.createTempFile("shortHierarchy", ".csv");
        String csvContent = """
                Id,firstName,lastName,salary,managerId
                1,John,CEO,150000,
                2,Mary,Manager1,60000,1
                3,Alice,Lead1,30000,2
                4,Tom,Dev1,20000,3
                """;

        Files.writeString(tempFile, csvContent);
        analyzer2.loadEmployees(tempFile.toString());

        List<String> issues = analyzer2.checkReportingLines();
        assertTrue(issues.isEmpty());
    }
}

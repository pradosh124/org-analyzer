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

 class OrgAnalyzerTest {
    private OrgAnalyzer analyzer;

     @Test
     void testLoadEmployeeData() throws IOException, URISyntaxException {
        analyzer = new OrgAnalyzer();

        // Load CSV from resources folder
        URL resource = getClass().getClassLoader().getResource("employees.csv");
        assertNotNull(resource, "employees.csv not found in resources");

        String filePath = Paths.get(resource.toURI()).toString();
        analyzer.loadEmployees(filePath);
    }

     @Test
     void testLoadEmployeeDataForEmptyId() throws IOException, URISyntaxException {
         OrgAnalyzer analyzer2 = new OrgAnalyzer();
         Path tempFile = Files.createTempFile("shortHierarchy", ".csv");
         String csvContent = """
                Id,firstName,lastName,salary,managerId
                1,John,CEO,150000,
                2,Mary,Manager1,60000,1
                3,Alice,Lead1,30000,2
                4,Tom,Dev1,20000,3
                "",Bob,Dev2,20000,3
                """;

         Files.writeString(tempFile, csvContent);
         analyzer2.loadEmployees(tempFile.toString());
     }
}

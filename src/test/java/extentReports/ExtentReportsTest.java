package extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.Test;

public class ExtentReportsTest {

    @Test
    public void extentTest() {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/java/reports/index.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("My Report");
        spark.config().setReportName("Max Sudik");
        extent.attachReporter(spark);

        ExtentTest test1 = extent.createTest("Login Test");
        test1.pass("Login Test started successfully");
        test1.pass("URL is loaded");
        test1.pass("Value entered");
        test1.pass("Login Test completed successfully");
        test1.fail("Test Failed here");
        test1.info("This is some info for testing logs");

        ExtentTest test2 = extent.createTest("Home Page test");
        test2.pass("Login Test started successfully");
        test2.pass("URL is loaded");
        test2.pass("Value entered");
        test2.pass("Login Test completed successfully");
        test2.fail("Test Failed here");
        test2.info("This is some info for testing logs");

        extent.flush();
    }
}

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
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("My Report");
        spark.config().setReportName("Max Sudik");
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("Login Test");
        test.pass("Login Test started successfully");
        test.pass("URL is loaded");
        test.pass("Value entered");
        test.pass("Login Test completed successfully");
        test.fail("Test Failed here");
        test.info("This is some info for testing logs");


    }
}

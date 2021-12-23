package extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ExtentReportsTest {

    @Test
    public void extentTest() throws IOException {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/java/reports/index.html");

        final File CONF = new File("src/test/java/config/spark-config.json");
        spark.loadJSONConfig(CONF);


//        final File CONF = new File("src/test/java/config/spark-config.xml");
//        spark.loadXMLConfig(CONF);

//        spark.config().setTheme(Theme.DARK);
//        spark.config().setDocumentTitle("My Report");
//        spark.config().setReportName("Max Sudik");
        extent.attachReporter(spark);

        ExtentTest test1 = extent.createTest("Login Test").assignAuthor("Max Sudik").assignCategory("Smoke").assignCategory("Regression").assignDevice("chrome 96");
        test1.pass("Login Test started successfully");
        test1.pass("URL is loaded");
        test1.pass("Value entered");
        test1.pass("Login Test completed successfully");
        test1.fail("Test Failed here");
        test1.info("This is some info for testing logs");

        ExtentTest test2 = extent.createTest("Home Page test").assignAuthor("Another Author").assignCategory("Integration").assignDevice("safari latest");
        test2.pass("Login Test started successfully");
        test2.pass("URL is loaded");
        test2.pass("Value entered");
        test2.pass("Login Test completed successfully");
        test2.fail("Test Failed here");
        test2.info("This is some info for testing logs");

        extent.flush();
        Desktop.getDesktop().browse(new File("src/test/java/reports/index.html").toURI()); //opens report automatically after the execution
    }
}

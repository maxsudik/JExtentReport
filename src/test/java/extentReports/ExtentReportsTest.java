package extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportsTest {

    ExtentReports extent;

    @BeforeSuite
    public void setUp() throws IOException {

        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/java/reports/index.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.CATEGORY,
                        ViewName.EXCEPTION,
                        ViewName.LOG}).apply();
        ExtentSparkReporter failedSpark = new ExtentSparkReporter("src/test/java/reports/failed-tests-index.html")
                .filter().statusFilter().as(new Status[]{Status.FAIL}).apply();

        final File CONF = new File("src/test/java/config/spark-config.json");
        spark.loadJSONConfig(CONF);
        failedSpark.loadJSONConfig(CONF);

        /**
         This section is for configuring Extent report using XML file config located at
         src/test/java/config/spark-config.xml
         */
//        final File CONF = new File("src/test/java/config/spark-config.xml");
//        spark.loadXMLConfig(CONF);

        /**
         * This section is for configuration Extent report using .config() method of ExtentSparkReporter class
         */
//        spark.config().setTheme(Theme.DARK);
//        spark.config().setDocumentTitle("My Report");
//        spark.config().setReportName("Max Sudik");

        extent.attachReporter(spark, failedSpark);
    }

    @AfterSuite
    public void setDown() throws IOException {
        extent.flush();
        Desktop.getDesktop().browse(new File("src/test/java/reports/index.html").toURI()); //opens report automatically after the execution
    }

    @Test
    public void extentTest() throws IOException {

        String jsonExample = new String(Files.readAllBytes((Paths.get("src/test/java/resources/example.json"))));
        String xmlExample = new String(Files.readAllBytes((Paths.get("src/test/java/resources/example.xml"))));
        Map<String, String> map = new HashMap<>();
        map.put("key1", "Selenium");
        map.put("key2", "Appium");
        map.put("key3", "RestAssured");

        ExtentTest test1 = extent.createTest("Login Test").assignAuthor("Max Sudik").assignCategory("Smoke").assignCategory("Regression").assignDevice("chrome 96");
        test1.pass("Login Test started successfully");
        test1.pass("URL is loaded");
        test1.pass("Value entered");
        test1.pass("Login Test completed successfully");
        test1.fail("Test Failed here");
        test1.info("This is some info for testing logs");
        test1.pass(MarkupHelper.createOrderedList(Arrays.asList(new String[]{"Selenium", "Appium", "RestAssured"})).getMarkup());
        test1.pass(MarkupHelper.createOrderedList(map).getMarkup());
        test1.info(MarkupHelper.createCodeBlock(jsonExample, CodeLanguage.JSON));
        test1.info(MarkupHelper.createCodeBlock(xmlExample, CodeLanguage.XML));
        test1.fail(MarkupHelper.createLabel("Login test failed", ExtentColor.RED));

        ExtentTest test2 = extent.createTest("Home Page test").assignAuthor("Another Author").assignCategory("Integration").assignDevice("safari latest");
        test2.pass("Login Test started successfully");
        test2.pass("URL is loaded");
        test2.pass("Value entered");
        test2.pass("Login Test completed successfully");
        test2.info("This is some info for testing logs");
        test2.pass(MarkupHelper.createLabel("Login test passed", ExtentColor.GREEN));
    }
}

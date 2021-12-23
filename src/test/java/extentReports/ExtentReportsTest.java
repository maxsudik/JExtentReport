package extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportsTest {

    WebDriver driver;
    ExtentReports extent;

    public String getScreenshotPath() throws IOException {
        TakesScreenshot camera = (TakesScreenshot) driver;
        File source = camera.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/src/test/java/reports/screenshots/image.png";
        System.out.println(path);
        File destination = new File(path);
        FileUtils.copyFile(source, destination);
        return path;
    }

    public String getScreenshotAsBase64() throws IOException {
        TakesScreenshot camera = (TakesScreenshot) driver;
        File source = camera.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/src/test/java/reports/screenshots/image64.png";
        System.out.println(path);
        File destination = new File(path);
        FileUtils.copyFile(source, destination);
        byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(path));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public String getBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    @BeforeSuite
    public void setUp() throws IOException {
        WebDriverManager.chromedriver().setup();

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

    @BeforeTest
    public void setUpDriver() {
        driver = new ChromeDriver();
    }

    @AfterTest
    public void setDownDriver() {
        driver.quit();
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

        driver.get("https://www.google.com/?client=safari");
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
        test1.pass("Regular Screenshot attached here", MediaEntityBuilder.createScreenCaptureFromPath(getScreenshotPath()).build());

        driver.navigate().to("https://duckduckgo.com");
        ExtentTest test2 = extent.createTest("Home Page test").assignAuthor("Another Author").assignCategory("Integration").assignDevice("safari latest");
        test2.pass("Login Test started successfully");
        test2.pass("URL is loaded");
        test2.pass("Value entered");
        test2.pass("Login Test completed successfully");
        test2.info("This is some info for testing logs");
        test2.pass(MarkupHelper.createLabel("Login test passed", ExtentColor.GREEN));
        test2.pass("Base64 screenshot attached here", MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotAsBase64()).build());

        driver.navigate().to("https://www.bing.com");
        ExtentTest test3 = extent.createTest("Base 64 screenshot test").assignAuthor("QA Author").assignCategory("Parallel").assignDevice("Edge");
        test3.pass("Login Test started successfully");
        test3.pass("URL is loaded");
        test3.pass("Value entered");
        test3.pass("Login Test completed successfully");
        test3.info("This is some info for testing logs");
        test3.pass(MarkupHelper.createLabel("Login test passed", ExtentColor.GREEN));
        test3.pass("Base64 1 liner screenshot attached here", MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64()).build());
    }
}

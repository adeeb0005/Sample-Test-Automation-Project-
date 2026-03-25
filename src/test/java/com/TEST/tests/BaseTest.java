package com.TEST.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.model.SystemEnvInfo;
import com.TEST.pages.Page;
import com.TEST.tests.api.ApiClient;
import com.TEST.utils.*;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BaseTest {

    protected ExtentReports extent;
    protected ExtentTest test;

    public WebDriver driver;
    public ApiClient apiClient;
    public static Page page;
    screenRecorderUtility scRecord = new screenRecorderUtility();
    protected static String dbUrl = Config.DBUrl;
    protected static String dbUser = Config.DBUser;
    protected static String dbPassword = Config.DBPassword;
    protected static String url = Config.UIUrl;
    protected static String webhookUrl = Config.ReportTeamsWebhookUrl;
    protected static String reportSendReportToTeams = Config.ReportSendReportToTeams;
    protected static String baseUrl = Config.APIBaseUrl;
    protected static String accessToken = Config.APIAccessToken;
    protected static String organizationId = Config.APIOrgId;

    public void setupAPI() {
        apiClient = new ApiClient(baseUrl, accessToken, organizationId);
    }

    public void setupDB() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            DBUtility.setConnection(connection);
            System.out.println("Database connection established successfully.");

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToWebpage(){
        driver.get(url);
    }

    public void closeDBConnection() {
        try {
            DBUtility.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void sendReportToTeams() {
//        String htmlFilePath = PathUtility.getRunDirectory() + "/ExtentSparkReport.html";
//        System.out.println("Report file path: " + htmlFilePath);
//
//        String runID = RunManager.getRunID();
//
//        if (Objects.equals(ConfigManager.getProperty("sendReportToTeams"), "Yes")) {
//            try {
//                System.out.println("Sending report to Microsoft Teams...");
//
//                ExtentReportParser.TestSuiteData suiteData = ExtentReportParser.parseExtentReport(htmlFilePath);
//
//                JSONObject teamsMessage = TeamsMessageFormatter.formatForTeams(runID, suiteData);
//                String webhookUrl = ConfigManager.getProperty("teamsWebhookUrl");
//                TeamsWebhookUtility.sendToTeams(teamsMessage, webhookUrl);
//            } catch (Exception e) {
//                System.err.println("Error while sending report to Teams: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Sending report to Teams is disabled in the configuration");
//        }
//    }

    public void flushReport() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("Extent report flushed successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error while flushing Extent report: " + e.getMessage());
        }
    }

    // Todo: setupSuite
    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        RunManager.initializeRunDirectory();
        extent = ExtentManager.getExtent();

        System.out.println("Initializing Test Suite...");
        setupDB();
    }

    // Todo: tearDownSuite
    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        String runID = RunManager.getRunID();
        String jsonFilePath = PathUtility.getRunDirectory() + "/extent-report.json";
        String htmlFilePath = PathUtility.getRunDirectory() + "/ExtentSparkReport.html";

        System.out.println("Tearing down test suite...");
        // Todo: Teardown each test
        // Todo: Teardown db
        closeDBConnection();
        // Todo: Teardown api
        // Done: Teardown suite and save to html
        flushReport();

        JSONObject jsonReport = ReportUtility.generateJsonReport(extent, runID);
        ReportUtility.writeJsonReportToFile(jsonReport, jsonFilePath);
        // Todo: Send report to teams
        if (Objects.equals(reportSendReportToTeams, "Yes")) {
            TeamsUtility.sendReportToTeams(jsonReport, webhookUrl);
        } else {
            System.out.println("Sending report to Teams is disabled in the configuration");
        }
//        sendReportToTeams();

        // Todo: Send report to email
    }

    // Todo: setupGroup
    // Todo: tearDownGroup

    // Todo: setupClass
    // Todo: tearDownClass

    // Todo: setupTest or setupMethod

    @BeforeMethod(alwaysRun = true)
    public void setupMethod(Method method) throws Exception {
        driver = WebDriverManagerUtility.getDriver();
        goToWebpage();
        if (driver == null) {
            throw new IllegalStateException("Driver is null! Check WebDriverManagerUtility.getDriver()");
        }
        assert driver != null;
        if (extent == null) {
            extent = ExtentManager.getExtent();
        }
        test = extent.createTest(method.getName());
        String testName = method.getName();
        if ("false".equalsIgnoreCase(WebDriverManagerUtility.isHeadless())) {
            //        scRecord.startRecording(testName);
            ScreenRecorderUtil.startRecord(testName);
        }
        setupAPI();
    }

    // Todo: tearDownTest or tearDownMethod
    @AfterMethod(alwaysRun = true)
    public void teardownMethod(ITestResult result) throws Exception{
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test done.");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test skipped.");
        }

        boolean isTestFailed = result.getStatus() == ITestResult.FAILURE;
        String testName = result.getName();
        if ("false".equalsIgnoreCase(WebDriverManagerUtility.isHeadless())) {
//            scRecord.stopRecording(testName, isTestFailed);
//            scRecord.stopRecording(isTestFailed);
            File videoFile = ScreenRecorderUtil.stopRecord();
            // Add the video to the Extent Report if it's not null and exists
            if (videoFile != null && videoFile.exists()) {
                // Adjust the path as needed (e.g., move to a specific folder for Extent report)
                String videoPath = videoFile.getCanonicalPath();

                // Change the path below to a relative path that fits the Extent Report's folder structure
                // String relativePath = "test-recordings/" + videoFile.getName();

                // Attach the video to the test in the Extent report
                test.info("Video Recording: " + videoPath);
                test.addVideoFromPath(videoPath);
            }
            // Attach Screen Capture or screenshots
            // test.addScreenCaptureFromPath(screenCapturePath);
        }

//        if (driver != null) {
//            driver.quit();
//            page.logAction("Driver instance closed!");
//        }
        WebDriverManagerUtility.quitDriver();
    }

    public static void writeTextToFile(String text, String filePath) {
        try(FileWriter file = new FileWriter(filePath)) {
            file.write(text);
            System.out.println("Saved file at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main func
    public static void main(String[] args) {
        // Todo: send teams report manually by params
        // Todo: if params empty then send the last one
//        sendReportToTeams();
    }
}

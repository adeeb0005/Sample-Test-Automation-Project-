package com.TEST.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;
    private static final String environment = Config.ReportEnvironment;
    private static final String tester = Config.UserTester;
    private static final String documentTitle = Config.ReportDocumentTitle;
    private static final String reportName = Config.ReportReportName;

    public static ExtentReports createInstance() {

        String reportPath = PathUtility.getRunDirectory() + "/ExtentSparkReport.html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setReportName(reportName);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Environment", environment);
        extent.setSystemInfo("Tester", tester);

        return extent;
    }

    public static ExtentReports getExtent() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }
}

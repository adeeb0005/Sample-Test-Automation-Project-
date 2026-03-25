package com.TEST.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.SystemEnvInfo;
import com.aventstack.extentreports.model.Test;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportUtility {

    public static String formatExecutionTime(long startTime, long endTime) {
//        long totalExecutionTime = (endTime - startTime) / 1000;
//
//        long hours = totalExecutionTime % 3600;
//        long minutes = (totalExecutionTime - (hours * 3600)) % 60;
//        long seconds = totalExecutionTime - (hours * 3600) - (minutes * 60);
//
//        if (totalExecutionTime % 3600 > 0) { // hours
//            // Todo: convert to hours, minutes and secs
//            return String.format("%d hours %d mins %d secs", hours, minutes, seconds);
//        } else if (totalExecutionTime % 60 > 0) { // minutes
//            // Todo: convert to minutes and secs
//            return String.format("%d mins %d secs", minutes, seconds);
//        } else {
//            return String.format("%d secs", totalExecutionTime);
//        }
        Instant startInstant = Instant.ofEpochMilli(startTime);
        Instant endInstant = Instant.ofEpochMilli(endTime);

        long totalSeconds = ChronoUnit.SECONDS.between(startInstant, endInstant);
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + " mins " + seconds + " secs";
    }

    public static JSONObject generateJsonReport(ExtentReports extent, String runID) {
        if (extent == null) {
            throw new IllegalStateException("ExtentReports is null! No report data available.");
        }

        Report reportData = extent.getReport();

        List<SystemEnvInfo> envInfoList = reportData.getSystemEnvInfo();
        Map<String, String> systemInfoMap = new HashMap<>();

        for (SystemEnvInfo envInfo : envInfoList) {
            systemInfoMap.put(envInfo.getName(), envInfo.getValue());
        }

        String environment = systemInfoMap.getOrDefault("Environment", "Unknown");
        String tester = systemInfoMap.getOrDefault("Tester", "Unknown");

        long startTime = reportData.getStartTime().getTime();
        long endTime = reportData.getEndTime().getTime();
//        long totalExecutionTime = endTime - startTime;

        // Convert the report object to JSON
        JSONObject jsonReport = new JSONObject();
        jsonReport.put("runId", runID);
        jsonReport.put("environment", environment);
        jsonReport.put("tester", tester);
        jsonReport.put("status", reportData.getStatus());
        jsonReport.put("start_time", reportData.getStartTime().toString());
        jsonReport.put("end_time", reportData.getEndTime().toString());
        jsonReport.put("total_execution_time", formatExecutionTime(startTime, endTime));

        List<Test> testList = reportData.getTestList();
        JSONArray testArray = new JSONArray();

        int totalTests = 0, passedTests = 0, failedTests = 0, skippedTests = 0, ignoredTests = 0;

        for (Test test: testList) {
            totalTests++;

            JSONObject testJson = new JSONObject();
            // Todo: get order
            testJson.put("id", test.getId());
            testJson.put("name", test.getName());
            testJson.put("startTime", test.getStartTime().toString());
            testJson.put("endTime", test.getEndTime().toString());
            // Calculate test duration
//            long testDuration = Duration.between(test.getStartTime().toInstant(), test.getEndTime().toInstant()).toMillis();
//            testJson.put("duration", testDuration);
            testJson.put("duration", formatExecutionTime(test.getStartTime().getTime(), test.getEndTime().getTime()));
            String status = test.getStatus().getName();
            testJson.put("status", status);

            // Categorize test based on status
            switch (status.toLowerCase()) {
                case "pass":
                    passedTests++;
                    break;
                case "fail":
                    failedTests++;
                    break;
                case "skip":
                    skippedTests++;
                    break;
                case "ignore":
                    ignoredTests++;
                    break;
            }
            // Extract media path if available
            if (!test.getMedia().isEmpty()) {
                testJson.put("media_path", test.getMedia().get(0).getPath());
            } else {
                testJson.put("media_path", "None");
            }
            // Extract exception details if available
            if (!test.getExceptions().isEmpty()) {
                testJson.put("exception", test.getExceptions().get(0).getName());
                testJson.put("exception_stacktrace", test.getExceptions().get(0).getStackTrace());
            } else {
                testJson.put("exception", "None");
                testJson.put("exception_stacktrace", "None");
            }

            JSONArray logArray = new JSONArray();
            test.getLogs().forEach(log -> {
                JSONObject logJson = new JSONObject();
                logJson.put("sequence", log.getSeq());
                logJson.put("timestamp", log.getTimestamp().toString());
                logJson.put("status", log.getStatus().toString());
                logJson.put("details", log.getDetails());
                logJson.put("media", log.getMedia());
                if (log.getException() != null) {
                    logJson.put("exception", log.getException().getName());
                } else {
                    logJson.put("exception", "None");
                }
                logArray.put(logJson);
            });
            testJson.put("logs", logArray);
            testArray.put(testJson);
        }
        jsonReport.put("total_tests", totalTests);
        jsonReport.put("passed_tests", passedTests);
        jsonReport.put("failed_tests", failedTests);
        jsonReport.put("skipped_tests", skippedTests);
        jsonReport.put("ignored_tests", ignoredTests);
        jsonReport.put("tests", testArray);

        // String jsonReportString = jsonReport.toString(4);
        // System.out.println(jsonReportString);

        return jsonReport;
    }

    public static void writeJsonReportToFile(JSONObject jsonReport, String filePath) {
        try(FileWriter file = new FileWriter(filePath)) {
            String jsonReportString = jsonReport.toString(4);
            file.write(jsonReportString);
            System.out.println(jsonReportString);
            System.out.println("Json Report Saved at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

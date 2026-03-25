package com.TEST.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class RunManager {

    private static String runID;
    private static String runDirectory;
    private static String environment;
    private static String suiteName;

    // Initializes the run ID and directory
    public static String initializeRunDirectory() {
        if (runID != null || runDirectory != null) {
            throw new IllegalStateException("RunManager has already been initialized.");
        }
        runID = generateRunId(getTargetTimezone());

        String rootDirectory = System.getProperty("user.dir") + File.separator + "report";
//        createDirectory(rootDirectory);

        runDirectory = rootDirectory + File.separator + runID;
        createDirectory(runDirectory);

        createDirectory(getScreenshotsDirectory());
        createDirectory(getRecordingsDirectory());
        createDirectory(getDownloadsDirectory());

        return runDirectory;
    }

    // Get target timezone
    private static String getTargetTimezone() {
        // Todo: Default to UTC if timezone not found
        String targetTimezone = Config.ReportTimezone;

        if (!targetTimezone.startsWith("GMT") && (targetTimezone.startsWith("UTC+") || targetTimezone.startsWith("UTC-"))) {
            targetTimezone = targetTimezone.replace("UTC", "GMT");
        }
        return targetTimezone;
    }

    // Generates a unique run ID based on timestamp and timezone
    private static String generateRunId(String timezone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_E_HH-mm-ss");
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        String timestamp = sdf.format(new Date());

        TimeZone tz = TimeZone.getTimeZone(timezone);
        int offsetMillis = tz.getOffset(System.currentTimeMillis());
        int hours = offsetMillis / (1000 * 60 * 60);
        int minutes = Math.abs(offsetMillis / (1000 * 60) % 60);
        String offset = String.format("UTC%s%d-%02d", hours >= 0 ? "+" : "-", Math.abs(hours), minutes);
        return timestamp + "_" + offset;
    }

    // Creates a directory if it doesn't exist
    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Returns the run ID
    public static String getRunID() {
        if (runID == null) {
            throw new IllegalStateException("RunManager is not initialized. Call initializeRun() first.");
        }
        return runID;
    }

    // Returns the run directory
    public static String getRunDirectory() {
        if (runDirectory == null) {
            throw new IllegalStateException("Run directory is not initialized. Call initializeRunDirectory() first.");
        }
        return runDirectory;
    }

    // Returns subdirectories for specific purposes
    public static String getScreenshotsDirectory() {
        return getRunDirectory() + File.separator + "screenshots";
    }

    public static String getRecordingsDirectory() {
        return getRunDirectory() + File.separator + "recordings";
    }

    public static String getDownloadsDirectory() {
        return runDirectory + File.separator + "downloads";
    }

    // set ang get environment
    public static void setEnvironment() {
        environment = Config.ReportEnvironment;
    }

    public static String getEnvironment() {
        environment = Config.ReportEnvironment;
        return environment;
    }

    // set and get suiteName
    public static void setSuiteName(String Name) {
        suiteName = Config.ReportSuiteName;
    }

    public static String getSuiteName() {
        return suiteName;
    }

    public static void main(String[] args) {
        String runId = generateRunId(getTargetTimezone());
        System.out.println(runId);
        System.out.println(getEnvironment());
    }
}

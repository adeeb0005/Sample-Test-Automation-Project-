package com.TEST.utils;

public class Config {

    // Helper method to check primary key and fallback to variables.config
    private static String getConfigValue(String section, String primaryKey) {
        String value = ConfigManager.getProperty(section + "." + primaryKey);
        if (value == null) {
            value = System.getenv((section + "_" + primaryKey).toUpperCase());
            System.out.println("Property of " + section + "_" + primaryKey + " : " + value + " type: " + value.getClass().getName());
        }
        return value;
    }

    public static final String ReportEnvironment = getConfigValue("report", "environment");
    public static final String ReportDocumentTitle = getConfigValue("report", "documentTitle");
    public static final String ReportReportName = getConfigValue("report", "reportName");
    public static final String ReportSuiteName = getConfigValue("report", "suiteName");
    public static final String ReportSprintName = getConfigValue("report", "sprintName");
    public static final String ReportTimezone = getConfigValue("report", "timezone");
    public static final String ReportSendReportToTeams = getConfigValue("report", "sendReportToTeams");
    public static final String ReportTeamsWebhookUrl = getConfigValue("report", "teamsWebhookUrl");

    public static final String UserTester = getConfigValue("user", "tester");
    public static final String UserEmail = getConfigValue("user", "email");
    public static final String UserPassword = getConfigValue("user", "password");

    public static final String UIBrowser = getConfigValue("ui", "browser");
    public static final String UIHeadless = getConfigValue("ui", "headless");
    public static final String UIWindowSize = getConfigValue("ui", "windowSize");
    public static final String UIImplicitWait = getConfigValue("ui", "implicitWait");
    public static final String UIExplicitWait = getConfigValue("ui", "explicitWait");
    public static final String UIChromeDriverPath = getConfigValue("ui", "chromeDriverPath");
    public static final String UIFirefoxDriverPath = getConfigValue("ui", "firefoxDriverPath");
    public static final String UIUrl = getConfigValue("ui", "url");

    public static final String APIBaseUrl = getConfigValue("api", "baseUrl");
    public static final String APIAccessToken = getConfigValue("api", "accessToken");
    public static final String APITokenUrl = getConfigValue("api", "tokenUrl");
    public static final String APIClientId = getConfigValue("api", "clientId");
    public static final String APIClientSecret = getConfigValue("api", "clientSecret");
    public static final String APIOrgId = getConfigValue("api", "orgId");

    public static final String DBHost = getConfigValue("db", "host");
    public static final String DBPort = getConfigValue("db", "port");
    public static final String DBDatabase = getConfigValue("db", "database");
    public static final String DBUrl = getConfigValue("db", "url");
    public static final String DBUser = getConfigValue("db", "user");
    public static final String DBPassword = getConfigValue("db", "password");

    public static final String FrigaUrl = getConfigValue("friga", "frigaUrl");
    public static final String FrigaPushLocationDataUrl = getConfigValue("friga", "frigaPushLocationDataUrl");
    public static final String FrigaPushLocationDataEndpoint = getConfigValue("friga", "frigaPushLocationDataEndpoint");

    public static void main(String[] args) {
        try {
            System.out.println("Report environment: " + ReportEnvironment);
            System.out.println("User tester: " + UserTester);
            System.out.println("UI url: " + UIUrl);
            System.out.println("API base url: " + APIBaseUrl);
            System.out.println("DB url: " + DBUrl);
            System.out.println("DB user: " + DBUser);
            System.out.println("DB password: " + DBPassword);
            System.out.println("Friga url: " + FrigaUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

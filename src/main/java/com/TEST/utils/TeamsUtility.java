package com.TEST.utils;

import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeamsUtility {

    private static String extractShortErrorMessage(String fullErrorMessage) {
        if (fullErrorMessage == null || fullErrorMessage.isEmpty()) {
            return "UnknownError";
        }
        String shortErrorMessage = fullErrorMessage.split(":")[0].trim();
        String[] parts = shortErrorMessage.split("\\.");
        return parts[parts.length - 1];
    }

    public static void sendToTeams(JSONObject teamsMessage, String webhookUrl) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookUrl);
            post.setHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(teamsMessage.toString());
            post.setEntity(entity);

            client.execute(post);
            System.out.println("Message sent to Teams successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject formatDataForTeams(JSONObject jsonReport) {
        JSONObject message = new JSONObject();
        message.put("@type", "MessageCard");
        message.put("@context", "http://schema.org/extensions");
        message.put("summary", "Test Execution Report");
        message.put("themeColor", "0076D7");

        message.put("title", "🧪 Test Execution Report - Run ID: " + jsonReport.getString("runId"));

        // Execution Details
        JSONObject executionDetailsSection = new JSONObject();
        executionDetailsSection.put("text", "🚀 **Execution Details**\n" +
                "- **Environment**: " + jsonReport.getString("environment") + "\n" +
                "- **Tester**: " + jsonReport.getString("tester") + "\n" +
                "- **Total Tests**: " + jsonReport.getInt("total_tests") + "\n" +
                "- **Passed ✅**: " + jsonReport.getInt("passed_tests") + "\n" +
                "- **Failed ❌**: " + jsonReport.getInt("failed_tests") + "\n" +
                "- **Skipped ⏭**: " + jsonReport.getInt("skipped_tests") + "\n" +
                "- **Execution Time ⏱**: " + jsonReport.getString("total_execution_time") + " \n" +
                "- **Start Time ⏳**: " + jsonReport.getString("start_time") + "\n" +
                "- **End Time ⌛**: " + jsonReport.getString("end_time"));

        StringBuilder passedTests = new StringBuilder();
        StringBuilder failedTests = new StringBuilder();

        JSONArray testArray = jsonReport.getJSONArray("tests");
        for (int i = 0; i < testArray.length(); i++) {
            JSONObject test = testArray.getJSONObject(i);
            String status = test.getString("status");

            if ("pass".equalsIgnoreCase(status)) {
                passedTests.append("- **").append(test.getString("name")).append("** (")
                        .append(test.getString("duration")).append(" )\n");
            } else if ("fail".equalsIgnoreCase(status)) {
                failedTests.append("- **").append(test.getString("name")).append("**\n")
                        .append("  - **Duration**: ").append(test.getString("duration")).append(" \n")
                        .append("  - **Error**: ").append(extractShortErrorMessage(test.optString("exception", "None"))).append("\n");
//                        .append("  - **Stacktrace**: ").append(test.optString("exception_stacktrace", "None")).append("\n");
            }
        }

        JSONObject passedSection = new JSONObject();
        passedSection.put("activityTitle", "✅ **Passed Tests (" + jsonReport.getInt("passed_tests") + ")**");
        passedSection.put("activityText", passedTests.toString().trim());

        JSONObject failedSection = new JSONObject();
        failedSection.put("activityTitle", "❌ **Failed Tests (" + jsonReport.getInt("failed_tests") + ")**");
        failedSection.put("activityText", failedTests.toString().trim());

        // Combine all Sections
        JSONArray sections = new JSONArray();
        sections.put(executionDetailsSection);
        sections.put(failedSection);
        sections.put(passedSection);
        message.put("sections", sections);

        return message;
    }

    public static void sendReportToTeams(JSONObject jsonReport, String teamsWebhookURL) {
        try {
            System.out.println("Sending report to Microsoft Teams...");
            JSONObject teamsMessage = formatDataForTeams(jsonReport);
            sendToTeams(teamsMessage, teamsWebhookURL);
        } catch (Exception e) {
            System.err.println("Error while sending report to Teams: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

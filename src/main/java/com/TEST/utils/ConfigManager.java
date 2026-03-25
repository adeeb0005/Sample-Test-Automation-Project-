package com.TEST.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.yaml.snakeyaml.Yaml;

public class ConfigManager {
    private static final Map<String, Properties> propertiesMap = new HashMap<>();
    private static final Map<String, Map<String, Object>> yamlDataMap = new HashMap<>();

    // Method to load a YAML file dynamically
    public static void loadYamlFile(String filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);
            if (yamlData != null) {
                yamlDataMap.put(filePath, yamlData);
                System.out.println("Loaded YAML: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to load YAML file: " + filePath);
        }
    }

    static {
        loadYamlFile("config.yaml");
        // loadYamlFile("azure-nightly.yaml");
    }

    // Helper method to fetch nested values using dot notation (e.g., "api.base_url")
    @SuppressWarnings("unchecked")
    private static String getNestedValue(Map<String, Object> yamlData, String key) {
        String[] keys = key.split("\\.");
        Object value = yamlData;

        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<String, Object>) value).get(k);
            } else {
                return null; // Key path does not exist
            }
        }
        return value != null ? value.toString() : null;
    }

    // Method to retrieve a property by key (supports nested keys using dot notation)
    public static String getProperty(String key) {
        for (Map<String, Object> yamlData : yamlDataMap.values()) {
            String value = getNestedValue(yamlData, key);
            if (value != null) return value;
        }
        System.err.println("Property not found in yaml: " + key);
        return null;
    }

    // Retrieve a property from a specific YAML file
    public static String getProperty(String filePath, String key) {
        Map<String, Object> yamlData = yamlDataMap.get(filePath);
        if (yamlData != null) {
            return getNestedValue(yamlData, key);
        }
        return null; // Return null if the key is not found
    }

    // Method to clear all loaded YAML data
    public static void clearProperties() {
        yamlDataMap.clear();
    }

    public static void main(String[] args) {
        try {
            System.out.println("Report environment from config.yaml: " + getProperty("report.environment"));
            System.out.println("Report environment from azure-nightly.yaml: " + getProperty("variables.report_environment"));
            System.out.println("User tester from config.yaml: " + getProperty("user.tester"));
            System.out.println("User tester from azure-nightly.yaml: " + getProperty("variables.user_tester"));
            System.out.println("UI url from config.yaml: " + getProperty("ui.url"));
            System.out.println("UI url from azure-nightly.yaml: " + getProperty("variables.ui_url"));
            System.out.println("API base url from config.yaml: " + getProperty("api.baseUrl"));
            System.out.println("API base url from azure-nightly.yaml: " + getProperty("variables.api_baseUrl"));
            System.out.println("DB url from config.yaml: " + getProperty("db.url"));
            System.out.println("DB url from azure-nightly.yaml: " + getProperty("variables.db_url"));
            System.out.println("DB user from config.yaml: " + getProperty("db.user"));
            System.out.println("DB user from azure-nightly.yaml: " + getProperty("variables.db_user"));
            System.out.println("DB password from config.yaml: " + getProperty("db.password"));
            System.out.println("DB password from azure-nightly.yaml: " + getProperty("variables.db_password"));
            System.out.println("Friga url from config.yaml: " + getProperty("friga.frigaUrl"));
            System.out.println("Friga url from azure-nightly.yaml: " + getProperty("variables.friga_frigaUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

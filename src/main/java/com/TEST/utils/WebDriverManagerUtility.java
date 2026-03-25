package com.TEST.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.util.HashMap;
import java.util.Map;

public class WebDriverManagerUtility {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static String headless = Config.UIHeadless;
    private static String windowSize = Config.UIWindowSize;
    private static String downloadDir = RunManager.getDownloadsDirectory();

    public static String isHeadless () {
        return headless;
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = Config.UIBrowser;
            String headless = Config.UIHeadless;

            if (browser == null || browser.isEmpty()) {
                throw new IllegalArgumentException("Browser property is not set in configuration!");
            }

//            switch (browser.toLowerCase()) {
//                case "chrome":
//                    WebDriverManager.chromedriver().setup();
//                    driver.set(new ChromeDriver());
//                    break;
//                case "firefox":
//                    WebDriverManager.firefoxdriver().setup();
//                    driver.set(new FirefoxDriver());
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unsupported browser: " + browser);
//            }

            if ("chrome".equalsIgnoreCase(browser)) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();

                // Set custom download directory
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", downloadDir);
//                prefs.put("download.prompt_for_download", false);
//                prefs.put("download.directory_upgrade", true);
//                prefs.put("plugins.always_open_pdf_externally", true); // Avoid opening PDFs in browser
//                prefs.put("profile.default_content_settings.popups", 0);
//                prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
                options.setExperimentalOption("prefs", prefs);

                // Enable logging for debugging
                System.out.println("Chrome Download Directory: " + downloadDir);

                // Apply headless mode if configured
                if ("true".equalsIgnoreCase(headless)) {
                    options.addArguments("--headless");
                }

//                // Apply window size if configured
//                if (windowSize != null && !windowSize.isEmpty()) {
//                    options.addArguments("--window-size=" + windowSize);
//                }
//
//                // Apply additional options from config
//                if (additionalOptions != null) {
//                    for (String option : additionalOptions) {
//                        options.addArguments(option);
//                    }
//                }

                driver.set(new ChromeDriver(options));
            } else if ("firefox".equalsIgnoreCase(browser)) {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();

                // Set custom download directory
                options.addPreference("browser.download.folderList", 2);
                options.addPreference("browser.download.dir", downloadDir);
                options.addPreference("browser.download.useDownloadDir", true);
                options.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip, application/pdf");
                options.addPreference("pdfjs.disabled", true); // Download PDF instead of opening

                // Apply headless mode if configured
                if ("true".equalsIgnoreCase(headless)) {
                    options.addArguments("--headless");
                }

//                // Apply window size if configured
//                if (windowSize != null && !windowSize.isEmpty()) {
//                    options.addArguments("--width=" + windowSize.split("x")[0]);
//                    options.addArguments("--height=" + windowSize.split("x")[1]);
//                }
//
//                // Apply additional options from config
//                if (additionalOptions != null) {
//                    for (String option : additionalOptions) {
//                        options.addArguments(option);
//                    }
//                }

                driver.set(new FirefoxDriver(options));
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }
        driver.get().manage().window().maximize();
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}

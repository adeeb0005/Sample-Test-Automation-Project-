package com.TEST.pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public abstract class Page {
    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(Page.class);

    // Centralized wait time constants
    protected static final int DEFAULT_WAIT_TIME = 30;
    protected static final int PAGE_LOAD_TIMEOUT = 30;
    protected static final int IMPLICIT_WAIT = 10;
    protected static final int DEFAULT_SLEEP_TIME_MILLISECONDS = 1000;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    // Todo: Log actions for better traceability
    public void logAction(String action) {
        logger.info(action); // Logs the action using the logger
        System.out.println(action); // Prints the action to the console
    }

    // Todo 1: Take a screenshot
    public void takeScreenshot(String fileName) {
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        try {
//            FileUtils.copyFile(screenshot, new File("screenshots/" + filename + ".png"));
//        } catch (IOException e) {
//            logger.error("Error taking screenshot: " + e.getMessage());
//        }
    }

    // Todo 2: Screen Recording
    public void screenRecord(String fileName) {}

    // Navigate to a specific URL
    public void goTo(String url){
        driver.get(url);
        logAction("Navigate to: " + url);
    }

    // Method to maximize the browser window
    protected void maximizeWindow() {
        driver.manage().window().maximize();
        logAction("Maximized browser window");
    }

    public String getPageTitle() {
        String title = driver.getTitle();
        logAction("Retrieved page title: " + title);
        return title;
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logAction("Retrieved current url: " + url);
        return url;
    }

    public void refreshPage() {
        driver.navigate().refresh();
        logAction("Page refreshed.");
    }

    public void navigateBack() {
        driver.navigate().back();
        logAction("Navigated back.");
    }

    public void navigateForward() {
        driver.navigate().forward();
        logAction("Navigated forward.");
    }

    public void setImplicitWait(Duration time) {
        driver.manage().timeouts().implicitlyWait(time);
        logAction("Set implicit wait to: " + time.getSeconds() + " seconds.");
    }

    public void closeWindow() {
        driver.close();
        logAction("Closed the current window.");
    }

    public void quitDriver() {
        driver.quit();
        logAction("Quit the driver and closed all windows.");
    }
}

package com.TEST.pages;

import com.TEST.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By emailField = By.id("signInName");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("next");
    private final By errorMsg = By.xpath("//*[@class='error pageLevel']");
    private final By emailErrorMsg = By.xpath("//*[@id='signInName']/preceding-sibling::div/p");
    private final By passwordErrorMsg = By.xpath("//*[@id='password']/preceding-sibling::div/p");

    public String email = Config.UserEmail;
    public String password = Config.UserPassword;
    public String invalidEmail = "test@example.com";
    public String invalidPassword = "password";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage login(String email, String password) {
        clearAndEnterText(emailField, email);
        clearAndEnterText(passwordField, password);
        click(loginButton);
        logAction("Attempted login with username: " + email);

        return new DashboardPage(driver);
    }

    public boolean isPageErrorMessageDisplayed() {
        return driver.findElement(errorMsg).isDisplayed();
    }

    public boolean isEmailErrorMessageDisplayed() {
        return driver.findElement(emailErrorMsg).isDisplayed();
    }

    public boolean isPasswordErrorMessageDisplayed() {
        return driver.findElement(passwordErrorMsg).isDisplayed();
    }

    public String getPageErrorMessageText() {
        return driver.findElement(errorMsg).getText();
    }

    public String getEmailErrorMessageText() {
        return driver.findElement(emailErrorMsg).getText();
    }

    public String getPasswordErrorMessageText() {
        return driver.findElement(passwordErrorMsg).getText();
    }

    public void waitForPageErrorToBeDisplayed() {
        isDisplayed(errorMsg);
        logAction("Waited for page error to appear");
    }

    public void waitForEmailErrorToBeDisplayed() {
        isDisplayed(emailErrorMsg);
        logAction("Waited for Email error to appear");
    }

    public void waitForPasswordErrorToBeDisplayed() {
        isDisplayed(passwordErrorMsg);
        logAction("Waited for Email error to appear");
    }
}

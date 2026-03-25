package com.TEST.tests;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{

    DashboardPage dashboardPage;

    String loginPageTitle = "doglapan Shipment Visibility";

    @Test(groups = {"regression"})
    public void L001_LoginWithEmptyField_TC0000() {
        test.info("Start of Login with empty field.");
        String errorEmailMsg = "Please enter your Email Address";
        String errorPasswordMsg = "Please enter your password";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.logAction("Starting login test with invalid credentials.");

        String email = "";
        String password = "";

        loginPage.login(email, password);
        loginPage.waitForEmailErrorToBeDisplayed();
        Assert.assertTrue(loginPage.isEmailErrorMessageDisplayed(), "Please enter email");
        Assert.assertEquals(loginPage.getEmailErrorMessageText(), errorEmailMsg);
        loginPage.waitForPasswordErrorToBeDisplayed();
        Assert.assertTrue(loginPage.isPasswordErrorMessageDisplayed(), "Please enter password");
        Assert.assertEquals(loginPage.getPasswordErrorMessageText(), errorPasswordMsg);
        test.pass("Login with empty field passed successfully.");
    }

    @Test(groups = {"regression"})
    public void L002_LoginWithUnRegisteredCredential_TC0000() {
        test.info("Start of Login with un registered credentials.");
        String errorPageMsg = "We can't seem to find your account.";
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logAction("Starting login test with invalid credentials.");

        String email = loginPage.invalidEmail;
//        String email = "test@example.com";
        String password = loginPage.password;

        loginPage.login(email, password);
        loginPage.waitForPageErrorToBeDisplayed();
        Assert.assertTrue(loginPage.isPageErrorMessageDisplayed(), "Error message for unregistered email");
        Assert.assertEquals(loginPage.getPageErrorMessageText(), errorPageMsg);
        test.pass("Login with un registered credentials passed successfully.");
    }

    @Test(groups = {"regression"})
    public void L003_LoginWithInvalidPassword_TC0000() {
        test.info("Start of Login with Invalid Password.");
        String errorPageMsg = "Your password is incorrect.";
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logAction("Starting login test with invalid credentials.");

        String email = loginPage.email;
        String password = loginPage.invalidPassword;

        loginPage.login(email, password);
        loginPage.waitForPasswordErrorToBeDisplayed();
        Assert.assertTrue(loginPage.isPageErrorMessageDisplayed(), "Error message should display for invalid password!");
        Assert.assertEquals(loginPage.getPageErrorMessageText(), errorPageMsg);
        test.pass("Login with Invalid Password passed successfully.");
    }

    @Test(groups = {"regression"})
    public void L004_LoginWithValidCredentials_TC0000() {
        test.info("Start of Login with valid credentials.");
        LoginPage loginPage = new LoginPage(driver);
        String email = loginPage.email;
        String password = loginPage.password;

        DashboardPage dashboardPage = loginPage.login(email, password);
        dashboardPage.waitForProfileNameToAppear();
        Assert.assertEquals(loginPageTitle, dashboardPage.getPageTitle());
        test.pass("Login with valid credentials passed successfully.");
    }
}

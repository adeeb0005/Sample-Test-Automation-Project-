package com.TEST.tests;

import com.TEST.pages.AreaPage;
import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.TEST.pages.BasePage.sleep;
import static org.testng.Assert.assertTrue;

public class AreaTest extends BaseTest{

    private AreaPage areaPage;

    public AreaPage loginAndGoToAreaPage() {
        LoginPage loginPage = new LoginPage(driver);
        test.info("Retrieving the email...");
        String email = loginPage.email;
        test.info("Retrieved the email");
        test.info("Retrieving the password...");
        String password = loginPage.password;
        test.info("Retrieved the password");
        test.info("Logging into TEST...");
        DashboardPage dashboardPage = loginPage.login(email, password);
        test.info("Logged into TEST");
        test.info("Verifying the Profile Name...");
        dashboardPage.waitForProfileNameToAppear();
        test.info("Verified the Profile Name");
        test.info("Going to Area Page...");
        areaPage = dashboardPage.goToAreaPage();
        test.info("At the Areas Page Now");

        return areaPage;
    }

    @Test(groups = {"regression"})
    public void AR001_verifyAreaCreation_TC0000()  throws InterruptedException {
        areaPage = loginAndGoToAreaPage();

        test.info("Creating New Area...");
        String areaName = areaPage.createAndValidateArea().get("areaName");
        areaPage.refreshPage();
        areaPage.refreshPage();
        sleep(2000);
        areaPage.deleteAreaByName(areaName);
        test.info("Created New Area");
        Thread.sleep(2000);
    }

    @Test(groups = {"regression"})
    public void AR002_areaCreateUpdateSummaryAreaDetailsGeneralInfo_TC0000()  throws InterruptedException {
        areaPage = loginAndGoToAreaPage();
        test.info("Creating New Area...");
        Map<String, String> areaData = areaPage.createAndValidateArea();
        areaPage.refreshPage();
        areaPage.refreshPage();
        for(int i = 0; i < 2; i++) {
            sleep(2000);
            areaPage.searchAreaByName(areaData.get("areaName"));
            sleep(1000);
            areaPage.clickOnAreaName(areaData.get("areaName"));
            sleep(1000);
            Map<String, String> dataFromAreaSummary = areaPage.getDataFromAreaSummary(true);
            Assert.assertTrue(areaPage.checkSummaryDataWithGivenData(dataFromAreaSummary, areaData), "Data is not match");
            if(i == 0)areaPage.clickFullDetails();
            else {
                areaPage.clickCrossButton();
                areaPage.clickFullDetailsIconByName(areaData.get("areaName"));
            }
            sleep(2000);
            Assert.assertTrue(areaPage.validateAreaSnapShot(areaData), "Area Data is not match with Details page");
            areaPage.clickAreaDetailsPageToAreaPage();
            sleep(5000);
            if(i == 0) {
                areaPage.searchAreaByName(areaData.get("areaName"));
                sleep(2000);
                areaData = areaPage.updateAreaByName(areaData);
            }
            else{
                areaPage.deleteAreaByName(areaData.get("areaName"));
            }
        }
    }
}

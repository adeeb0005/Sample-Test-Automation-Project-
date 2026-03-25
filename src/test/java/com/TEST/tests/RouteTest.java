package com.TEST.tests;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import com.TEST.pages.RoutePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.TEST.pages.BasePage.sleep;

public class RouteTest extends BaseTest {
    private RoutePage routePage;

    public RoutePage loginAndGoToRoutePage() {
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
        test.info("Going to Route Page...");
        routePage = dashboardPage.goToRoutePage();
        test.info("At the Route Page Now");

        return routePage;
    }

    @Test(groups = {"regression"})
    public void AR001_verifyRouteCreation_TC0000()  throws InterruptedException {
        routePage = loginAndGoToRoutePage();

        // Todo: test.info or logAction

        test.info("Creating New Route...");
        Map<String, String> routeData = routePage.createRouteByFillingUpMandatoryFields();
        test.info("Created New Route");
        routePage.refreshPage();
        routePage.refreshPage();
        sleep(2000);
        routePage.deleteRouteByName(routeData.get("routeName"));
        Thread.sleep(2000);
    }

    @Test(groups = {"regression"})
    public void AR002_routeCreateUpdateSummaryRouteDetailsGeneralInfo_TC0000() throws InterruptedException{
        routePage = loginAndGoToRoutePage();
        test.info("Creating New Route...");
        Map<String, String> routeData = routePage.createRouteByFillingUpMandatoryFields();
        test.info("Created New Route");
        routePage.refreshPage();
        routePage.refreshPage();
        for(int i = 0; i < 2; i++) {
            sleep(2000);
            System.out.println(routeData);
            routePage.searchRouteByName(routeData.get("routeName"));
            System.out.println(2000);
            routePage.clickOnRouteName(routeData.get("routeName"));
            sleep(1000);
            Map<String, String> dataFromRouteSummary = routePage.getDataFromRouteSummary();
            Assert.assertTrue(routePage.checkSummaryDataWithGivenData(dataFromRouteSummary, routeData), "Data is not match");
            if(i == 0){
                routePage.clickFullDetails();
            }
            else{
                routePage.clickCrossButton();
                routePage.clickFullDetailsIconByName(routeData.get("routeName"));
            }
            sleep(2000);
            Assert.assertTrue(routePage.validateAreaSnapShot(routeData), "Data is not matched");
            routePage.clickAreaDetailsPageToRoutePage();
            sleep(2000);

            if(i == 0){
                routePage.searchRouteByName(routeData.get("routeName"));
                sleep(2000);
                routeData = routePage.updateRouteByName(routeData);
            }
            else {
                routePage.deleteRouteByName(routeData.get("routeName"));
            }
        }
    }
}

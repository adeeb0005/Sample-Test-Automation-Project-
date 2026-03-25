package com.TEST.tests;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    public DashboardPage loginAndGoToDashboardPage() {
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
        return dashboardPage;
    }

    @Test(groups = {"regression"})
    public void D001_VerifySideBarLayout_TC6836(){
        dashboardPage = loginAndGoToDashboardPage();
        Set<String> expectedHrefWithCollapsedButton = Set.of("", "en-us", "CustomerPortalBackoffice", "CreateRequest",
                "ProductSelector", "requests", "shipments", "homepage", "RequestSummary?ActiveTab=0");
        Set<String> expectedHrefWithExpandButton = Set.of("", "en-us", "CustomerPortalBackoffice", "CreateRequest",
                "DecryptLogFiles", "ServiceNetworkMap", "Reports", "requests", "shipments", "RequestSummary?ActiveTab=0",
                "LongTermLeaseForecast", "ProductSelector", "CargoPlanner", "ControlBoxPasswords", "ContainerDocuments",
                "homepage");
        Set<String> expectedExpandTests = Set.of("Request Order", "Dashboard", "Product Selector", "Track Shipments",
                "Manage Shipments", "Request Summary", "Knowledge Base", "Support", "Back Office");
        Set<String> expectedExpandWithExpandButton = Set.of("Manage Shipments", "Track Shipments", "Control Box Passwords",
                "Service Network Map", "Support", "Request Order", "Dashboard", "Reports", "Product Selector",
                "Long Term Lease Forecast", "Decrypt Log Files", "Cargo Planner", "Back Office", "Container Documents",
                "Knowledge Base", "Request Summary");
        int expectedCountOfSidebarItem = 9;
        int expectedCountWithExpandOfSidebarItem = 16;
        int numberOfCollapsedButtons = 2;
        if(!dashboardPage.isSideBarExpanded()){
            if(dashboardPage.numOfCollapsedButtonInSideBar()==numberOfCollapsedButtons){
                Assert.assertEquals(dashboardPage.numberOfItemsInSidebar(), expectedCountOfSidebarItem, "Item count does not match!");
                List<String> texts = dashboardPage.getTextOfSidebarItems();
                for (String text : texts) {
                    Assert.assertEquals(text.trim().length(), 0);
                }
                Set<String> eHrefs = new HashSet<>(dashboardPage.getLastPartsFromHrefsOfSidebar());
                Assert.assertEquals(eHrefs, expectedHrefWithCollapsedButton);
                dashboardPage.clickOnSidebarReportCenterButton();
                dashboardPage.clickOnSidebarToolsButton();
                eHrefs = new HashSet<>(dashboardPage.getLastPartsFromHrefsOfSidebar());
                Assert.assertEquals(eHrefs, expectedHrefWithExpandButton);
                Assert.assertEquals(dashboardPage.numberOfItemsInSidebar(), expectedCountWithExpandOfSidebarItem, "Item count does not match!");
                dashboardPage.clickOnSidebarReportCenterButton();
                dashboardPage.clickOnSidebarToolsButton();
                eHrefs = new HashSet<>(dashboardPage.getLastPartsFromHrefsOfSidebar());
                Assert.assertEquals(eHrefs, expectedHrefWithCollapsedButton);
            }
            dashboardPage.clickOnSidebarCollapseButton();
            if(dashboardPage.isSideBarExpanded()){
                Assert.assertEquals(dashboardPage.numberOfItemsInSidebar(), expectedCountOfSidebarItem, "Item count does not match!");
                Set<String> texts = new HashSet<>(dashboardPage.getTextOfSidebarItems());
                Assert.assertEquals(texts, expectedExpandTests);
                Set<String> eHrefs = new HashSet<>(dashboardPage.getLastPartsFromHrefsOfSidebar());
                Assert.assertEquals(eHrefs, expectedHrefWithCollapsedButton);
                dashboardPage.clickOnSidebarReportCenterButton();
                dashboardPage.clickOnSidebarToolsButton();
                texts = new HashSet<>(dashboardPage.getTextOfSidebarItems());
                Assert.assertEquals(texts, expectedExpandWithExpandButton);
                eHrefs = new HashSet<>(dashboardPage.getLastPartsFromHrefsOfSidebar());
                Assert.assertEquals(eHrefs, expectedHrefWithExpandButton);
                Assert.assertEquals(dashboardPage.numberOfItemsInSidebar(), expectedCountWithExpandOfSidebarItem, "Item count does not match!");
            }
        }
    }
}

package com.TEST.pages;

import com.TEST.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.ArrayList;

public class DashboardPage extends BasePage{

    private final String testerName = Config.UserTester;
    private final By profileName = xpath("//header//*[text()='" + testerName + "']");
//    private final By profileName = By.xpath("//*[text()='Kazi K. Mokabbir']");

    private final By dashboardPageLink = xpath("//div[@role='menubar']//*[text()='Dashboard']/ancestor::a");

    private final By shipmentsPageLink = xpath("//div[@role='menubar']//*[text()='Shipments']/ancestor::button");

    private final By assetsPageLink = xpath("//div[@role='menubar']//*[text()='Assets']/ancestor::button");

    private final By deviceDropdownLink = xpath("(//*[text()='Devices']/ancestor::button)[1]");

    private final By dataLoggersLink = xpath("//button[text()='Data Loggers']");

    private final By dataLoggersProfileLink = xpath("//button[text()='Data Logger Profiles']");

    private final By locationsDropdownLink = xpath("(//*[text()='Locations']/ancestor::button)[1]");

    private final By areasLink = xpath("//button[text()='Areas']");

    private final By routePageLink = xpath("//button[text()='Routes']");

    private final By rulesPageLink = xpath("//div[@role='menubar']//*[text()='Rules']/ancestor::button");


    private final By fullSideBar = By.tagName("aside");
    private final String collapsedClass = "w-sidebar-collapsed-width";
    private final By sidebarItems = By.xpath("//aside//a[@class=\"flex w-full justify-center\"]");
    private final By sidebarCollapseButton = By.xpath("(//aside//button)[1]");
    private final By sidebarReportCenterButton = By.xpath("(//aside//button)[2]");
    private final By sidebarToolsButton = By.xpath("(//aside//button)[3]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void waitForProfileNameToAppear() {
        waitForElementToAppear(xpath("//*[text()='Dashboard']//ancestor::button"));
        //waitForElementToAppear(profileName);
        //logAction("Waited for Profile name to show up.");
    }

    public AssetsPage goToAssetPage() {
        waitForElementToAppear(assetsPageLink);
        click(assetsPageLink);
        logAction("Clicked on Assets page link.");

        return new AssetsPage(driver);
    }

    public ShipmentsPage goToShipmentsPage() {
        waitForElementToAppear(shipmentsPageLink);
        click(shipmentsPageLink);
        logAction("Clicked on Shipments page link");

        return new ShipmentsPage(driver);
    }
    public DataLoggersPage goToDataLoggerPage() {
        waitForVisibility(deviceDropdownLink).click();
        click(dataLoggersLink);
        logAction("Clicked Data Loggers page link.");

        return new DataLoggersPage(driver);
    }
    public RulesPage goToRulesPage() {
        waitForVisibility(rulesPageLink).click();
        logAction("Clicked Rules page link.");
        return new RulesPage(driver);
    }

    public AreaPage goToAreaPage() {
        waitForVisibility(locationsDropdownLink).click();
        waitForVisibility(areasLink).click();
        logAction("Clicked Area page link.");
        return new AreaPage(driver);
    }

    public RoutePage goToRoutePage() {
        waitForVisibility(locationsDropdownLink).click();
        waitForVisibility(routePageLink).click();
        logAction("Clicked Route page link.");
        return new RoutePage(driver);
    }

    public DataLoggersProfilePage goToDataLoggerProfilePage() {
        waitForVisibility(deviceDropdownLink).click();
        click(dataLoggersProfileLink);
        logAction("Clicked Data Loggers page link.");

        return new DataLoggersProfilePage(driver);
    }
    public Boolean isSideBarCollapsed() {
        String attributeValue = getAttributeValue(fullSideBar, "class");
        System.out.println(attributeValue);

        return attributeValue.contains(collapsedClass);
    }
    public Boolean isSideBarExpanded(){
        return isDisplayed(xpath("//aside[contains(@class, 'sidebar-expanded-width')]"));
    }
    public int numberOfItemsInSidebar() {
        return getItemCount(sidebarItems);
    }
    public int numOfCollapsedButtonInSideBar(){
        return getItemCount(xpath("//aside//button[@data-state='closed']"));
    }
    public List<String> getHrefsOfSidebar() {
        return getAttributeValuesInList(sidebarItems, "href");
    }

    public List<String> getLastPartsFromHrefsOfSidebar() {
        List<String> lastParts = new ArrayList<>();
        List<String> hrefs = new ArrayList<>(getHrefsOfSidebar());
        for (String href : hrefs) {
            if (href.contains("/")) {
                String lastPart = href.substring(href.lastIndexOf("/") + 1);
                lastParts.add(lastPart);
            }
        }
        return lastParts;
    }

    public void clickOnSidebarCollapseButton() {
        click(sidebarCollapseButton);
    }

    public void clickOnSidebarReportCenterButton() {
        click(sidebarReportCenterButton);
    }

    public void clickOnSidebarToolsButton() {
        click(sidebarToolsButton);
    }

    public List<String> getTextOfSidebarItems() {
        return getAttributeValuesInList(sidebarItems, "text");
    }

}

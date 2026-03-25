package com.TEST.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Instant;
import java.util.*;

public class RoutePage extends BasePage {
    private final By Datagrid = By.xpath("//tbody//tr");
    private final By columnHeaders = By.xpath("//div//th");
    private final By routeNameField = xpath("//input[@placeholder='Set a Name']");
    private final By addNewRouteButton = By.xpath("//*[text()='New Route']/ancestor::button");
    private final By organizationButton = By.xpath("(//*[text()='Organization' and //*[text()='*']])[1]//following-sibling::button");
    private final By originButton = xpath("//*[text()='Origin' and //*[text()='*']]/following-sibling::button");
    private final By travelModeButton = xpath("//*[text()='Travel Mode' and //text()='*']/following-sibling::button");
    private final By destinationButton = xpath("//*[text()='Destination' and //*[text()='*']]/following-sibling::button");
    private final By createButton = xpath("//*[text()='Create']//ancestor::button");
    private final By regularDropdown = xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By routeCreationSuccessMessage = By.xpath("//*[text()='Route Created Successfully']");

    public RoutePage(WebDriver driver) {
        super(driver);
    }
    public Map<String, String> fillMandatoryFields() throws InterruptedException {
        Map<String, String> filledValues = new HashMap<>();

        String routeName = generateRandomString(16, "AutoRoute");
        clearAndEnterText(routeNameField, routeName);
        filledValues.put("routeName", routeName);
        click(organizationButton);
        String organization = "doglapan Training - Air Cargo";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        selectDropdownWithSearchByText(organization, organizationId);
        filledValues.put("organization", organization);
        //click
        click(originButton);
        String origin = "AutoHZSIA001";
        String originId = "0fb1209b-8f15-4af1-97ba-af2cc6e5d28c";
        filledValues.put("origin", origin);
        selectDropdownWithSearchByText(origin, originId);
        click(travelModeButton);
        sleep(500);
        selectDropdownByText(regularDropdown, "Road");
        click(destinationButton);
        String destination = "Cox's Bazar WA";
        String destinationId = "d9a638f2-ab9f-4c77-a0af-3687d49c94e8";
        filledValues.put("destination", destination);
        selectDropdownWithSearchByText(destination,destinationId);
        click(createButton);
        return filledValues;
    }

    public Map<String, String> createRouteByFillingUpMandatoryFields() throws InterruptedException {
        click(addNewRouteButton);
        Map<String, String> areaData = fillMandatoryFields();
        boolean status = waitForTextToBePresent(routeCreationSuccessMessage, "Route Created Successfully");
        if(!status)return null;
        return areaData;
    }
    public int getColumnIndex(String columnName) {
        List<WebElement> headers = getElements(columnHeaders);
        for (int i = 0; i < headers.size(); i++) {
            WebElement header = headers.get(i);
            String headerText = header.getText();
            if (headerText.contains(columnName)) {
                return i;
            }
        }
        return -1;
    }
    public int getRowIndex(String Name){
        int columnIndex = getColumnIndex("Name");
        List<WebElement> rows = getElements(Datagrid);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).findElement(By.xpath(".//td["+(columnIndex + 1) + "]")).getText().equals(Name))return i;
        }
        return 0;
    }
    public void deleteRouteByName(String routeName){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(routeName);
        waitForVisibility(By.xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]/parent::button")).click();
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
    }
    public void searchRouteByName(String name) {
        getElement(xpath("//*[text()='Name']/following-sibling::div/button[1]")).click();
        clearAndEnterText(xpath("//input[@data-testid='searchInput']"), name);
        keyboardKeyPress(Keys.ENTER);
    }
    public void clickOnRouteName(String routeName){
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(routeName);
        click(xpath("//table//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]"));
    }
    public Map<String, String> getDataFromRouteSummary(){
        Map<String, String> routeData = new HashMap<>();
        String routeName = getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        routeData.put("routeName", routeName);
        Set<String> gettingOptions = new HashSet<>();
        List<WebElement> optionsHeaders = getElements(xpath("//*[@class='my-3 grid w-[460px] grid-cols-[120px,calc(460px-132px)] gap-3 text-xs']//*[contains(@class,'font-semibold')]"));
        System.out.println(optionsHeaders.size());
        for(WebElement headers : optionsHeaders){
            gettingOptions.add(headers.getText());
        }
        optionsHeaders = getElements(xpath("//*[@class='whitespace-pre-wrap text-xs font-semibold text-doglapan-grey-80']"));
        for(WebElement headers : optionsHeaders){
            gettingOptions.add(headers.getText());
        }
        System.out.println(gettingOptions);
        Set<String> expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList(
                "Travel Mode", "Origin", "Last Modified By", "Last Modified At",
                "Created By", "Created At", "Destination"
        ));
        System.out.println(expectedOptions);
        System.out.println(expectedOptions.equals(gettingOptions));
        for(String expectedOption : expectedOptions){
            String str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[2]")).getText();
            if(expectedOption.equals("Origin")) str = getElement(xpath("((//*[text()='Origin']//following-sibling::*)[2]/*)[1]")).getText();
            else if(expectedOption.equals("Destination"))str = getElement(xpath("((//*[text()='Destination']//following-sibling::*)[2]/*)[1]")).getText();
            routeData.put(expectedOption, str);
        }
        if(!expectedOptions.equals(gettingOptions))return null;
        return routeData;
    }
    public boolean checkSummaryDataWithGivenData(Map<String, String> summary, Map<String, String> givenData){
        if(!summary.get("routeName").equals(givenData.get("routeName")))return false;
        if(!summary.get("Origin").equals(givenData.get("origin")))return false;
        return summary.get("Destination").equals(givenData.get("destination"));
    }
    public void clickFullDetails(){
        click(xpath("//*[text()='View Full Detail']//parent::a"));
    }
    public boolean checkBanner(String routeName){
        return isDisplayed(xpath("//*[contains(@class, 'lucide-route')]//following-sibling::*[text()='Routes']/parent::*//following-sibling::*[contains(@class, 'lucide-chevron-right')]//following-sibling::*[text()='"+routeName+"']"));
    }
    public boolean validateAreaSnapShot(Map<String, String> routeData){
        boolean bannerStatus = checkBanner(routeData.get("routeName"));
        if(!bannerStatus)return false;
        if(!isDisplayed(xpath("//*[text()='In Map']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='General Info']")))return false;
        List<WebElement> gettingHeadersWebElement = getElements(xpath("//*[@class='min-w-[120px] text-xs font-semibold text-doglapan-grey-80']"));
        Set<String> gettingHeaders = new HashSet<>();
        for(WebElement element : gettingHeadersWebElement){
            gettingHeaders.add(element.getText());
        }
        Set<String>expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList("Last Modified By", "Route Name", "Organization", "Travel Model", "Last Modified At", "Origin", "Destination",
                "Created By", "Created At"));
        if(!gettingHeaders.equals(expectedOptions))return false;
        for(String header : gettingHeaders){
            String str = getElement(xpath("//*[text()='"+header+"']/following-sibling::*")).getText();
            System.out.println(header);
            if(header.equals("Route Name")){
                if(!str.equals(routeData.get("routeName")))return false;
            }
            else if(header.equals("Origin")){
                if(!str.equals(routeData.get("origin")))return false;
            }
            else if(header.equals("Organization")){
                if(!str.equals(routeData.get("organization")))return false;
            }
            else if(header.equals("Destination")){
                if(!str.equals(routeData.get("destination")))return false;
            }
        }
        return true;
    }
    public void clickAreaDetailsPageToRoutePage(){
        click(xpath("//*[contains(@class, 'lucide-route')]//following-sibling::*[text()='Routes']"));
    }
    public void clickEditButtonByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//*[contains(@class, 'lucide-pen-line')]/parent::button"));
    }
    public Map<String, String> updateRouteByName(Map<String, String> routeData){
        clickEditButtonByName(routeData.get("routeName"));
        sleep(1000);
        long time = Instant.now().getEpochSecond();
        String newRouteName = "AutoRoute" + time;
        clearAndEnterText(routeNameField, newRouteName);
        Map<String, String> updatedRouteData = new HashMap<>();
        updatedRouteData.put("routeName", newRouteName);
        List<String> lists = new ArrayList<String>(routeData.keySet());
        for(String list :  lists){
            if(list.equals("routeName"))continue;
            updatedRouteData.put(list, routeData.get(list));
        }
        sleep(2000);
        click(xpath("//*[contains(@class, 'lucide-check')]//following-sibling::*[text()='Update']/ancestor::button"));
        return updatedRouteData;
    }
    public void clickCrossButton(){
        click(xpath("(//*[contains(@class, 'lucide-x')]//parent::button)[2]"));
    }
    public void clickFullDetailsIconByName(String name){
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//*[contains(@class, 'lucide-expand')]/parent::a"));
    }
}

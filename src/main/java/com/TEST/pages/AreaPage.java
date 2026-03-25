package com.TEST.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Instant;
import java.util.*;

public class AreaPage extends BasePage{
    private final By Datagrid = By.xpath("//tbody//tr");
    private final By columnHeaders = By.xpath("//div//th");
    private final By addNewAreaButton = By.xpath("//button[@data-testid='addNewButton']");
    private final By areaNameField = By.xpath("//input[@name='name']");
    //private final By organizationButton = By.xpath("(//*[@data-testid='organization-select']/button)[1]");
    private final By organizationButton = By.xpath("(//*[text()='Organization' and //*[text()='*']])[1]//following-sibling::button");
    //    private final By organizationSelect = By.xpath("(//*[@data-testid='organization-select']/select)[1]");
    private final By organizationSelect = By.xpath("//label[text()='Organization']/following-sibling::div/select");
    //private final By areaTypeButton = By.xpath("(//*[@data-testid='organization-select']/button)[2]");
    private final By areaTypeButton = By.xpath("//label[text()='Area Type']/following-sibling::div/button");
    //    private final By areaTypeSelect = By.xpath("(//*[@data-testid='organization-select']/select)[2]");
    private final By areaTypeSelect = By.xpath("//label[text()='Area Type']/following-sibling::div/select");
    //    private final By airportCode = By.xpath("");
    private final By airportCode = By.xpath("//label[text()='Airport Code']/following-sibling::div/input");
    private final By visibilityLevelButton = By.xpath("(//*[@data-testid='organization-select']/button)[3]");
    //    private final By visibilityLevelSelect = By.xpath("(//*[@data-testid='organization-select']/select)[3]");
    private final By visibilityLevelSelect = By.xpath("//label[text()='Visibility Level']/following-sibling::div/select");
    private final By addressField = By.xpath("//input[@name='address']");
    private final By firstSuggestionFromAddressSearch = By.xpath("//*[@aria-label='Suggestions']/div/div[1]");
    private final By createButton = By.xpath("//*[text()='Create']/ancestor::button");
    private final By areaCreationSuccessMessage = By.xpath("//*[text()='Area Created Successfully']");
    private final By dropDownLocator = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");

    public AreaPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnAddNewArea() {
        click(addNewAreaButton);
    }

    public String selectRandomFromDropdownBox(By Button, By SelectOption, String StartsWith){
        click(Button);
        List<WebElement> listOfOrganizationName = getElement(SelectOption).findElements(xpath(".//p"));
        String organizationName = listOfOrganizationName.get(generateRandomInt(listOfOrganizationName.size())).getText().trim();
        if(!StartsWith.isEmpty()){
            StartsWith = StartsWith.toLowerCase();
            for(WebElement orgName: listOfOrganizationName){
                String str = orgName.getText().trim().toLowerCase();
                if(str.startsWith(StartsWith)){
                    organizationName = orgName.getText().trim();
                    break;
                }
            }
        }
        while(organizationName.isEmpty()){
            organizationName = listOfOrganizationName.get(generateRandomInt(listOfOrganizationName.size())).getText().trim();
        }
        selectDropdownByText(SelectOption, organizationName);
        return organizationName;
    }

    public Map<String, String> fillMandatoryFields() throws InterruptedException {
        Map<String, String> filledValues = new HashMap<>();

        String areaName = generateRandomString(16, "AutoArea");
        clearAndEnterText(areaNameField, areaName);
        filledValues.put("areaName", areaName);
        click(organizationButton);
        String organization = "doglapan Training - Air Cargo";
        getElement(xpath("//*[@placeholder='Search...']")).sendKeys(organization);
        sleep(2000);
        Set<String> seenOptions = new HashSet<>();
        int cnt = 0;
        boolean newOptionsLoaded = true;
        boolean clicked = false;
        while (newOptionsLoaded && !clicked) {
            newOptionsLoaded = false;
            List<WebElement> options = driver.findElements(By.xpath("//div[@cmdk-list-sizer]//div[@cmdk-item]"));
            for (WebElement option : options) {
                String optionText = option.getAttribute("data-value");
                if (!seenOptions.contains(optionText)) {
                    seenOptions.add(optionText);
                    newOptionsLoaded = true;
                }
                scrollToElement(option);
                sleep(500);
                if (optionText.equals("49194360-27ab-4434-991f-88116dc775bb")) {
                    option.click();
                    clicked = true;
                    break;
                }
            }
        }

        //String organization = selectRandomFromDropdownBox(organizationButton, dropDownLocator, "");
        filledValues.put("organization", organization);
//        String areaType = "Airport";
//        selectDropdownByText(areaTypeSelect, areaType);
        String areaType = selectRandomFromDropdownBox(areaTypeButton, dropDownLocator, "airport");
        filledValues.put("areaType", areaType);

        String airportCodeValue = "DAC";
        if(areaType.equals("Airport"))clearAndEnterText(airportCode, airportCodeValue);
        filledValues.put("airportCode", airportCodeValue);

//        String organization1 = "Air Canada HQ";
//        String organization2 = "doglapan Training - Air Cargo";
//        selectDropdownByText(organizationSelect, organization1);
//        selectDropdownByText(organizationSelect, organization2);
//        filledValues.put("organization1", organization1);
//        filledValues.put("organization2", organization2);


        String visibilityLevel = "Organization Only";
        selectDropdownByText(visibilityLevelSelect, visibilityLevel);
        filledValues.put("visibilityLevel", visibilityLevel);

        String address = "Hazrat Shahjalal International Airport";
        clearAndEnterText(addressField, address);
        Thread.sleep(2000); // Consider replacing with explicit waits for better reliability
        click(addressField);
        click(firstSuggestionFromAddressSearch);
        filledValues.put("address", address);

        return filledValues;
    }

    public Map<String, String> createAndValidateArea() throws InterruptedException {
        clickOnAddNewArea();
        // Call fillMandatoryFields to populate the fields
        Map<String, String> areaData = fillMandatoryFields();

        // Perform the "Create" action
        click(createButton);
        boolean status = waitForTextToBePresent(areaCreationSuccessMessage, "Area Created Successfully");
        if(!status)return null;
        // Validate the success message
        return areaData;

        // Todo: validate from datagrid and db
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
    public void deleteAreaByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(name);
        waitForVisibility(By.xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]/parent::button")).click();
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
    }
    public boolean createAreaByFillingUpMandatoryFields() throws InterruptedException {
        click(addNewAreaButton);
        String areaName = generateRandomString(16, "AutoArea");
        clearAndEnterText(areaNameField, areaName);
        String organization = selectRandomFromDropdownBox(organizationButton, dropDownLocator, "");
        String areaType = selectRandomFromDropdownBox(areaTypeButton, dropDownLocator, "airport");
        String airportcode = "DAC";
        if(areaType.equals("Airport"))clearAndEnterText(airportCode, airportcode);
        selectDropdownByText(visibilityLevelSelect, "Organization Only");
        clearAndEnterText(addressField, "Hazrat Shahjalal International Airport");
        Thread.sleep(2000);
        click(addressField);
        click(firstSuggestionFromAddressSearch);
        click(createButton);
        return waitForTextToBePresent(areaCreationSuccessMessage, "Area Created Successfully");
    }
    public void searchAreaByName(String name) {
        getElement(xpath("//*[text()='Name']/following-sibling::div/button[1]")).click();
        clearAndEnterText(xpath("//input[@data-testid='searchInput']"), name);
        keyboardKeyPress(Keys.ENTER);
    }
    public void clickOnAreaName(String areaName){
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(areaName);
        click(xpath("//table//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]"));
    }
    public Map<String, String> getDataFromAreaSummary(boolean airport){
        Map<String, String> areaData = new HashMap<>();
        String areaName = getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        areaData.put("areaName", areaName);
        Set<String> gettingOptions = new HashSet<>();
        List<WebElement> optionsHeaders = getElements(xpath("//*[contains(@class,'[125px] font-semibold')]"));
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
                "Address", "Area Type", "Last Modified By", "Last Modified At",
                "Created By", "Created At"
        ));
        if (airport)expectedOptions.add("Airport Code");
        System.out.println(expectedOptions);
        System.out.println(expectedOptions.equals(gettingOptions));
        for(String expectedOption : expectedOptions){
            String str = "";
            if(expectedOption.equals("Airport Code"))str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[1]")).getText();
            else str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[2]")).getText();
            areaData.put(expectedOption, str);
        }
        if(!expectedOptions.equals(gettingOptions))return null;
        return areaData;
    }
    public boolean checkSummaryDataWithGivenData(Map<String, String> summary, Map<String, String> givenData){
        boolean status = true;
        if(!summary.get("areaName").equals(givenData.get("areaName")))return false;
        if(givenData.containsKey("airportCode")){
            if(!givenData.get("airportCode").equals(summary.get("Airport Code")))return false;
        }
        return status;
    }
    public void clickCrossButton(){
        click(xpath("(//*[contains(@class, 'lucide-x')]//parent::button)[2]"));
    }
    public void clickFullDetails(){
        click(xpath("//*[text()='View Full Detail']//parent::a"));
    }
    public boolean checkBanner(String areaName){
        return isDisplayed(xpath("//*[contains(@class, 'lucide-circle-dot')]//following-sibling::*[text()='Areas']/parent::*//following-sibling::*[contains(@class, 'lucide-chevron-right')]//following-sibling::*[text()='"+areaName+"']"));
    }
    public boolean validateAreaSnapShot(Map<String, String> areaData){
        boolean bannerStatus = checkBanner(areaData.get("areaName"));
        if(!bannerStatus)return false;
        if(!isDisplayed(xpath("//*[text()='In Map']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='General Info']")))return false;
        List<WebElement> gettingHeadersWebElement = getElements(xpath("//*[@class='min-w-[120px] text-xs font-semibold text-doglapan-grey-80']"));
        Set<String> gettingHeaders = new HashSet<>();
        for(WebElement element : gettingHeadersWebElement){
            gettingHeaders.add(element.getText());
        }
        Set<String>expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList("Last Modified By", "Area Type", "Organization", "Address", "Last Modified At", "Area Name", "Geofence (Meter)",
                "Created By", "Created At"));
        if(areaData.containsKey("airportCode"))expectedOptions.add("Airport Code");
        if(!gettingHeaders.equals(expectedOptions))return false;
        for(String header : gettingHeaders){
            String str = getElement(xpath("//*[text()='"+header+"']/following-sibling::*")).getText();
            System.out.println(header);
            if(header.equals("Area Name")){
                if(!str.equals(areaData.get("areaName")))return false;
            }
            else if(header.equals("Area Type")){
                if(!str.equals(areaData.get("areaType")))return false;
            }
            else if(header.equals("Organization")){
                if(!str.equals(areaData.get("organization")))return false;
            }
            else if(header.equals("airportCode")){
                if(!str.equals(areaData.get("Airport Code")))return false;
            }
        }
        return true;
    }
    public void clickAreaDetailsPageToAreaPage(){
        click(xpath("//*[contains(@class, 'lucide-circle-dot')]//following-sibling::*[text()='Areas']"));
    }
    public void clickEditButtonByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//*[contains(@class, 'lucide-pen-line')]/parent::button"));
    }
    public Map<String, String> updateAreaByName(Map<String, String> areaData){
        clickEditButtonByName(areaData.get("areaName"));
        sleep(1000);
        long time = Instant.now().getEpochSecond();
        String newAreaName = "AutoArea" + time;
        clearAndEnterText(areaNameField, newAreaName);
        Map<String, String> updatedArea = new HashMap<>();
        updatedArea.put("areaName", newAreaName);
        List<String> lists = new ArrayList<String>(areaData.keySet());
        for(String list :  lists){
            if(list.equals("areaName"))continue;
            updatedArea.put(list, areaData.get(list));
        }
        sleep(2000);
        click(xpath("//*[contains(@class, 'lucide-check')]//following-sibling::*[text()='Update']/ancestor::button"));
        return updatedArea;
    }
    public void clickFullDetailsIconByName(String name){
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//*[contains(@class, 'lucide-expand')]/parent::a"));
    }
}

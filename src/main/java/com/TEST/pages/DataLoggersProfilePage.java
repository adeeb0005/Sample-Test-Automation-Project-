package com.TEST.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class DataLoggersProfilePage extends BasePage{
    private final By profileNameInput = xpath("//*[@placeholder='Set a Name']");
    private final By tableHeaders = xpath("//table//thead//tr//th");
    private final By tableBodyRows = xpath("//table//tbody//tr");
    String newProfile = "//*[text()='New Profile']/ancestor::button";
    String popupCardXpath = "//div[contains(@class, 'popover-foreground')]";
    String popupHeader = "//*[text()='New Data Logger Profile']";
    String popupProfileName = "//*[text()='Profile Name']//*[text()='*']";
    String popupProfileNameInput = "/ancestor::*//following-sibling::*//input[@name='name']/parent::div";
    String organizationXpath = "//*[text()='Organization' and //*[text()='*']]";
    String dataLoggerTypeXpath = "//*[text()='Data Logger Type' and //*[text()='*']]";
    private final By Datagrid = By.xpath("//tbody//tr");
    String dropDownXpath = "//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]";
    String popupCardDropdownSelectXpath = "/ancestor::label/following-sibling::div//select";
    String popupCardDropdownButtonXpath = "/following-sibling::button";
    String popupCardDropdownButtonXpath2 = "/following-sibling::*/button";
    String configurationHeaderXpath = "//*[text()='Configuration']";
    String CreateButtonXpath = "//*[text()='Create']/ancestor::button";
    String TemperatureThresholdButton = "/ancestor::div//*[text()='Temperature Threshold']/preceding-sibling::button[@role='switch']";
    String TemperatureThresholdFromWithCInput = "/parent::div/following-sibling::div/*[text()='Below']/following-sibling::*[text()='°C']/preceding-sibling::div/input[@name='minTemperatureThreshold']";
    String TemperatureThresholdToWithCInput = "/parent::div/following-sibling::div//*[text()='Above']/following-sibling::*[text()='°C']/preceding-sibling::div/input[@name='maxTemperatureThreshold']";
    String RecordingIntervalInputXpath = "/following-sibling::*//*[text()='Recording Interval']//*[text()='*']/ancestor::label//following-sibling::*[text()='Minutes']/preceding-sibling::input";
    String ReportingIntervalInputXpath = "/following-sibling::*//*[text()='Reporting Interval']//*[text()='*']/ancestor::label//following-sibling::*[text()='Minutes']/preceding-sibling::input";
    String visibilityLevelXpath = "//*[text()='Visibility Level' and //*[text()='*']]";

    private final By newProfileButton = xpath(newProfile);

    List<String> HeadersWithDoubleButton = Arrays.asList("Name", "Organization", "Data Logger Type", "Created By", "Last Modified By");
    List<String> HeadersWithSingleButtonButSort = Arrays.asList("Created At", "Last Modified At");
    List<String> HeadersWithoutButton = Arrays.asList("Actions");
    public DataLoggersProfilePage(WebDriver driver){
        super(driver);
    }
    public int getColumnIndex(String columnName) {
        List<WebElement> headers = getElements(tableHeaders);
        for (int i = 0; i < headers.size(); i++) {
            WebElement header = headers.get(i);
            String headerText = header.getText();
            if (headerText.equals(columnName)) {
                return i;
            }
        }
        return -1;
    }
    public void clickSort(String columnName) {
        columnName = columnName.replace("'", "");
        int columnIndex = getColumnIndex(columnName);
        //System.out.println(columnIndex);
        List<String> onlySortButtonContains = Arrays.asList("Created At", "Last Modified At");
        String button;
        boolean found = false;
        for (String item : onlySortButtonContains) {
            //System.out.println(item);
            if (item.equals(columnName)) {
                found = true;
                break;
            }
        }
        if (found)button = "button";
        else button = "button[2]";
        By sortButtonBy = xpath("//table/thead/tr/th[" + (columnIndex + 1) + "]//"+button);
        //System.out.println("//table/thead/tr/th[" + (columnIndex + 1) + "]//"+button);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(sortButtonBy));

        // Scroll to the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortButton);
        sleep(1000);
        // Click the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortButton);
    }
    public List<String> getHeaders(){
        List<String> headers = new ArrayList<>();
        List<WebElement> webElementsHeaders = getElements(tableHeaders);
        for(int i = 0; i < webElementsHeaders.size(); i++){
            String str = webElementsHeaders.get(i).getText().trim();
            if(str.isEmpty())continue;
            if(str.equals("Actions"))continue;
            headers.add(str);
        }
        return headers;
    }
    public List<List<String>> getBody() {
        List<List<String>> body = new ArrayList<>();
        List<WebElement> rows = getElements(tableBodyRows);
        if (rows.isEmpty()) return body;

        int actionColumnIndex = getColumnIndex("Actions");
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cellInRow = rows.get(i).findElements(xpath(".//td"));
            if(cellInRow.size() == 1){
                boolean x = cellInRow.get(0).getText().equals("No results.");
                if(x)return body;
                List<String> onlySortButtonContains = Arrays.asList("Battery Level" , "Created At", "Last Modified At");
                body.add(onlySortButtonContains);
                return body;
            }
            List<String> row = new ArrayList<>();
            for (int j = 1; j < cellInRow.size(); j++) {
                if (j == actionColumnIndex){
                    continue;
                }
                row.add(cellInRow.get(j).getText().trim());
            }
            body.add(row);
        }
        return body;
    }
    public List<List<String>> getTable(){
        List<String> headers = getHeaders();
        List<List<String>> rows = getBody();
        List<List<String>> Table = new ArrayList<>();
        Table.add(headers);
        Table.addAll(rows);
        return Table;
    }
    public void findAndClickSearchButton(By sortButtonBy){
        WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(sortButtonBy));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sortButton);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortButton);
        } catch (JavascriptException e) {
            Actions actions = new Actions(driver);
            actions.moveToElement(sortButton).click().perform();
        }
    }
    public int setHeaderIntoXpathForSearchButtonClick(String header){
        try {
            System.out.println(header);
            if (HeadersWithDoubleButton.contains(header)) {
                findAndClickSearchButton(xpath("//thead//*[text()='" + header + "']//ancestor::th//button[1]"));
                boolean situation = isDisplayed(xpath("//*[@placeholder='Search']"));
                if (!situation) return -1;
                situation = isDisplayed(xpath("//*[@placeholder='Search']//following-sibling::span/*[1]"));
                if(!situation) return 0;
                else return 1;
            } else if (HeadersWithSingleButtonButSort.contains(header)) {
                findAndClickSearchButton(xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                boolean situation = isDisplayed(xpath("//*[@placeholder='Search']"));
                findAndClickSearchButton(xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                findAndClickSearchButton(xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                if (situation) return -1;
            } else if (HeadersWithoutButton.contains(header)) {
                boolean situation = isDisplayed(xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                if (situation) return -1;
            }
        } catch (NoSuchElementException e) {
            // If the element is not found, simply log it and continue
            System.out.println("Element not found for header: " + header);
        } catch (TimeoutException e) {
            // Handle timeout specifically
            System.out.println("Timeout occurred while checking header: " + header);
        }
        return 2;
    }
    public boolean checkSearchButton() {
        List<WebElement> headers = getElements(tableHeaders);
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).getText().trim();
            if (header.isEmpty()) continue;
            int status = setHeaderIntoXpathForSearchButtonClick(header);
            //status:-1:: button not found
            //status:0:: button found but, not search button
            //status:1:: search box open
            //status:2:: what happened, don't know
            if(status >= 1){
                if(status == 1)waitForVisibility(xpath("//*[@placeholder='Search']//following-sibling::span/*[1]")).click();
            }
            else return false;
            sleep(1000);
        }
        return true;
    }
    public boolean checkSearchOnColumn(String ColumnName, String demo_test, List<List<String>> table){
        int status = setHeaderIntoXpathForSearchButtonClick(ColumnName);
        if(status == 1){
            clearAndEnterText(xpath("//input[@placeholder='Search']"), demo_test);
            sleep(2000);
            getElement(xpath("//input[@placeholder='Search']//following-sibling::span//*[@stroke-linecap='round'][2]")).click();
            sleep(2000);
            List<List<String>> tableData = getTable();
            System.out.println(tableData);
            System.out.println(table);
            getElement(xpath("//span[text()='"+ColumnName+" | "+demo_test+"']/following-sibling::*[contains(@class, 'lucide-circle-x')]")).click();
            return tableData.equals(table);
        }
        else return false;
    }
    void clickNewProfile(){
        waitForVisibility(newProfileButton).click();
    }
    public boolean checkRowByColumn(String ColumnName, int rowNumber, String cellData){
        int columnIndex = getColumnIndex(ColumnName);
        return cellData.equals(getElement(xpath("//tbody//tr["+(rowNumber)+"]//td["+(columnIndex+1)+"]")).getText());
    }
    boolean checkFunctionalityConfig(){
        boolean disabledTemperatureThreshold = getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton)).getAttribute("data-state").equals("unchecked");
        if(!disabledTemperatureThreshold)return false;
        boolean isDisabled = !(getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdFromWithCInput)).isEnabled());
        if(!isDisabled)return false;
        isDisabled = !(getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdToWithCInput)).isEnabled());
        if(!isDisabled)return false;
        getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton)).click();
        boolean enabledTemperatureThreshold = getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton)).getAttribute("data-state").equals("checked");
        if(!enabledTemperatureThreshold)return false;
        boolean isEnabled = getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdFromWithCInput)).isEnabled();
        if(!isEnabled)return false;
        isEnabled = getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdToWithCInput)).isEnabled();
        if(!isEnabled)return false;
        getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton)).click();
        return true;
    }
    boolean checkConfiguration(){
        boolean header = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath));
        boolean temperatureThreshold = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton));
        boolean configFrom = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdFromWithCInput));
        boolean configTo = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdToWithCInput));
        if(temperatureThreshold&& configFrom && configTo){
            boolean functionalityIsOK = checkFunctionalityConfig();
            if(!functionalityIsOK)return false;
        }
        boolean recordingInterval = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath+RecordingIntervalInputXpath));
        boolean reportingInterval = isDisplayed(xpath(popupCardXpath+configurationHeaderXpath+ReportingIntervalInputXpath));
        boolean visibilityLevelButton = isDisplayed(xpath(popupCardXpath+visibilityLevelXpath+popupCardDropdownButtonXpath2));
        return header && temperatureThreshold && recordingInterval && reportingInterval && visibilityLevelButton;
    }
    boolean checkCard(){
        boolean popupWithHeader = isDisplayed(xpath(popupCardXpath+popupHeader));
        boolean profileName = isDisplayed(xpath(popupCardXpath+popupProfileName));
        boolean profileNameInput = isDisplayed(xpath(popupCardXpath+popupProfileName+popupProfileNameInput));
        boolean organization = isDisplayed(xpath(popupCardXpath+organizationXpath));
        boolean organizationButton = isDisplayed(xpath(popupCardXpath+organizationXpath+popupCardDropdownButtonXpath));
        boolean dataLoggerType = isDisplayed(xpath(popupCardXpath+dataLoggerTypeXpath));
        boolean dataLoggerTypeButton = isDisplayed(xpath(popupCardXpath+dataLoggerTypeXpath+popupCardDropdownButtonXpath));
        boolean configuration = checkConfiguration();
        return popupWithHeader && profileName && profileNameInput && organization && organizationButton
                && dataLoggerType && dataLoggerTypeButton && configuration;
    }
    String setProfileName(){
        String name = generateRandomString(5, "Auto_");
        clearAndEnterText(profileNameInput, name);
        //getElement(xpath(popupCardXpath+popupProfileName+popupProfileNameInput)).sendKeys(name);
        return name;
    }
    String selectRandomDropdownFromCard(String xPath){
        List<WebElement> texts = getElement(By.xpath(xPath)).findElements(By.xpath(".//option"));
        String selectedOption = texts.get(generateRandomInt(texts.size())).getText();
        if(selectedOption.isEmpty()){
            while (true){
                selectedOption = texts.get(generateRandomInt(texts.size())).getText();
                if(!selectedOption.isEmpty())break;
            }
        }
        String valueOfDataLoggerProfile = getElement(By.xpath(xPath+"//option[text()='"+selectedOption+"']")).getAttribute("value");
        selectDropdownByValue(By.xpath(xPath), valueOfDataLoggerProfile);
        return selectedOption;
    }
    int SetRandomNumber(String xPath){
        getElement(xpath(xPath)).clear();
        int time = generateRandomInt(30) + 1;
        waitForVisibility(xpath(xPath)).sendKeys(Keys.BACK_SPACE);
        waitForVisibility(xpath(xPath)).sendKeys(String.valueOf(time));
        return time;
    }
    int SetRandomNumber(String xPath, int x){
        getElement(xpath(xPath)).clear();
        int time = generateRandomInt(30) + 1 + x;
        waitForVisibility(xpath(xPath)).sendKeys(Keys.BACK_SPACE);
        waitForVisibility(xpath(xPath)).sendKeys(String.valueOf(time));
        return time;
    }
    public Map<String, String> setCompulsoryField(){
        Map<String, String> dataLoggerProfileData = new HashMap<>();
        String profileName = setProfileName();
        dataLoggerProfileData.put("Name", profileName);
        String organization = "EFASC Training - Air Cargo";
        dataLoggerProfileData.put("organization", organization);
        click(xpath(popupCardXpath+organizationXpath+popupCardDropdownButtonXpath));
        selectDropdownWithSearchByText(organization, "49194360-27ab-4434-991f-88116dc775bb");
        String dataLoggerType = "TraEFASC Pro";
        dataLoggerProfileData.put("dataLoggerType", dataLoggerType);
        click(xpath(popupCardXpath+dataLoggerTypeXpath+popupCardDropdownButtonXpath));
        selectDropdownWithSearchByText(dataLoggerType);
        String recordingInterval = Integer.toString(SetRandomNumber(popupCardXpath+configurationHeaderXpath+RecordingIntervalInputXpath));
        String reportingInterval = Integer.toString(SetRandomNumber(popupCardXpath+configurationHeaderXpath+ReportingIntervalInputXpath));
        dataLoggerProfileData.put("recordingInterval", recordingInterval);
        dataLoggerProfileData.put("reportingInterval", reportingInterval);
        return dataLoggerProfileData;
    }
    public Map<String, String> setOtherDataField(){
        getElement(xpath(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton)).click();
        int tFrom = SetRandomNumber(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdFromWithCInput);
        int tTo = SetRandomNumber(popupCardXpath+configurationHeaderXpath
                +TemperatureThresholdButton+TemperatureThresholdToWithCInput);
        while (tFrom >= tTo){
            tTo = SetRandomNumber(popupCardXpath+configurationHeaderXpath
                    +TemperatureThresholdButton+TemperatureThresholdToWithCInput, tFrom);
        }
        String thresholdFrom = Integer.toString(tFrom);
        String thresholdTo = Integer.toString(tTo);
        Map<String, String> fromTo = new HashMap<>();
        fromTo.put("thresholdFrom", thresholdFrom);
        fromTo.put("thresholdTo", thresholdTo);
        return fromTo;
    }
    public Map<String, String> createNewDataLogger(boolean addOtherData){
        clickNewProfile();
        boolean status = checkCard();
        if(!status)return null;
        Map<String, String> nameOrgTypeRecRep = setCompulsoryField();
        if(addOtherData){
            Map<String, String> extraData = setOtherDataField();
            List<String> keys = new ArrayList<String>(extraData.keySet());
            for(String key : keys){
                nameOrgTypeRecRep.put(key, extraData.get(key));
            }
        };
        click(xpath(CreateButtonXpath));
        return nameOrgTypeRecRep;
    }
    public int getRowIndex(String Name){
        int columnIndex = getColumnIndex("Name");
        List<WebElement> rows = getElements(Datagrid);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).findElement(By.xpath(".//td["+(columnIndex + 1) + "]")).getText().equals(Name))return i;
        }
        return 0;
    }
    public void deleteDLPByName(String deviceProfileName){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(deviceProfileName);
        sleep(500);
        while(true) {
            try {
                hoverOverElement(xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]/ancestor::button/parent::div"));
                click(xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]/ancestor::button/parent::div"));
                break;
            }catch (Exception e){
                System.out.println(e);
            }
            sleep(  500);
        }
        sleep(500);
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
    }
    public void searchDLPByName(String name) {
        getElement(xpath("//*[text()='Name']/following-sibling::div/button[1]")).click();
        clearAndEnterText(xpath("//input[@data-testid='searchInput']"), name);
        keyboardKeyPress(Keys.ENTER);
    }
    public void clickOnDLPName(String dlpName){
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(dlpName);
        click(xpath("//table//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//button"));
    }
    public Map<String, String> getDataFromDLPSummary(boolean thresholdActive){
        Map<String, String> dlpData = new HashMap<>();
        String routeName = getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-EFASC-blue-50']")).getText();
        dlpData.put("Name", routeName);
        Set<String> gettingOptions = new HashSet<>();
        List<WebElement> optionsHeaders = getElements(xpath("//*[contains(@class, 'border-EFASC-blue-30')]//*[contains(@class,'text-xs font-semibold text-EFASC-grey-80')]"));
        for(WebElement headers : optionsHeaders){
            gettingOptions.add(headers.getText());
        }
        Set<String> expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList(
                "Recording Interval", "Reporting Interval", "Last Modified By", "Last Modified At",
                "Created By", "Created At", "Organization"
        ));
        if(!expectedOptions.equals(gettingOptions))return null;
        if(thresholdActive){
            boolean check = isDisplayed(xpath("//button[@data-state='checked']//following-sibling::*[text()='Temperature Threshold']"));
            if(!check)return null;
            dlpData.put("thresholdFrom", getElement(xpath("//*[contains(@class, 'border-EFASC-blue-30')]//*[text()='From']//following-sibling::input")).getAttribute("value"));
            dlpData.put("thresholdTo", getElement(xpath("//*[contains(@class, 'border-EFASC-blue-30')]//*[text()='To']//following-sibling::input")).getAttribute("value"));
        }
        for(String expectedOption : expectedOptions){
            String str = "";
            if(expectedOption.equals("Recording Interval"))str = getElement(xpath("//*[contains(@class, 'border-EFASC-blue-30')]//*[text()='Recording Interval']//following-sibling::*[text()='Minutes']//preceding-sibling::*//input")).getAttribute("value");
            else if(expectedOption.equals("Reporting Interval"))str = getElement(xpath("//*[contains(@class, 'border-EFASC-blue-30')]//*[text()='Reporting Interval']//following-sibling::*[text()='Minutes']//preceding-sibling::*//input")).getAttribute("value");
            else str = getElement(xpath("(//*[contains(@class, 'border-EFASC-blue-30')]//*[text()='"+expectedOption+"']//following-sibling::*)[1]")).getText();
            dlpData.put(expectedOption, str);
        }
        return dlpData;
    }
    public boolean checkSummaryDataWithGivenData(Map<String, String> summary, Map<String, String> givenData){
        if(!summary.get("Name").equals(givenData.get("Name")))return false;
        if(!summary.get("Reporting Interval").equals(givenData.get("reportingInterval")))return false;
        if(!summary.get("Recording Interval").equals(givenData.get("recordingInterval")))return false;
        return summary.get("Organization").equals(givenData.get("organization"));
    }
    public void clickFullDetails(){
        click(xpath("//*[text()='View Full Detail']//parent::a"));
    }
    public boolean checkBanner(String dlpName){
        return isDisplayed(xpath("//*[contains(@class, 'lucide-file-sliders')]//following-sibling::*[text()='Data Logger Profiles']/parent::*//following-sibling::*[contains(@class, 'lucide-chevron-right')]//following-sibling::*[text()='"+dlpName+"']"));
    }
    public boolean validateDLPSnapShot(Map<String, String> dlpData){
        boolean bannerStatus = checkBanner(dlpData.get("Name"));
        if(!bannerStatus)return false;
        if(!isDisplayed(xpath("//*[text()='General Info']/parent::button[@data-state='active']//following-sibling::*//*[text()='Configuration']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='Assigned To']/parent::button[@data-state='inactive']")))return false;
        List<WebElement> gettingHeadersWebElement = getElements(xpath("//*[@data-state='active']//*[contains(@class,'text-xs font-semibold text-EFASC-grey-80')]"));
        Set<String> gettingHeaders = new HashSet<>();
        for(WebElement element : gettingHeadersWebElement){
            gettingHeaders.add(element.getText());
        }
        Set<String>expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList("Last Modified By", "Data Logger Profile Name", "Organization", "Data Logger Type", "Last Modified At",
                "Created By", "Created At"));
        if(!gettingHeaders.equals(expectedOptions))return false;
        for(String header : gettingHeaders){
            String str = getElement(xpath("//*[text()='"+header+"']/following-sibling::*")).getText();
            System.out.println(header);
            if(header.equals("Data Logger Profile Name")){
                if(!str.equals(dlpData.get("Name")))return false;
            }
            else if(header.equals("Organization")){
                if(!str.equals(dlpData.get("organization")))return false;
            }
            else if(header.equals("Data Logger Type")){
                if(!str.equals(dlpData.get("dataLoggerType")))return false;
            }
        }
        return true;
    }
    public void clickConfigButtonInDLPDetailsPage(){
        click(xpath("//*[text()='General Info']/parent::button[@data-state='active']//following-sibling::*//*[text()='Configuration']/parent::button[@data-state='inactive']"));
    }
    public boolean configPageValidation(Map<String, String> dlpData){
        if(!isDisplayed(xpath("//*[text()='General Info']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='Configuration']/parent::button[@data-state='active']//following-sibling::*//*[text()='Assigned To']/parent::button[@data-state='inactive']")))return false;
        List<String> keys = new ArrayList<String>(dlpData.keySet());
        for(String key : keys){
            if(key.equals("reportingInterval")){
                String str = getElement(xpath("//*[text()='Reporting Interval' and //*[text()='*']]//following-sibling::*[text()='Minutes']//preceding-sibling::input")).getAttribute("value");
                System.out.println(str);;
                if(!str.equals(dlpData.get("reportingInterval")))return false;
            }
            else if(key.equals("recordingInterval")){
                String str = getElement(xpath("//*[text()='Recording Interval' and //*[text()='*']]//following-sibling::*[text()='Minutes']//preceding-sibling::input")).getAttribute("value");
                if(!str.equals(dlpData.get("recordingInterval")))return false;
            }
            else if(key.equals("thresholdFrom")){
                if(isDisplayed(xpath("//button[@data-state='unchecked']")))return false;
                //String str = getElement(xpath("//input[@name='minTemperatureThreshold']")).getAttribute("value");
                String str = getElement(By.name("minTemperatureThreshold")).getAttribute("value");
                System.out.println(str);
                System.out.println(dlpData.get("thresholdFrom"));
                if(!str.equals(dlpData.get("thresholdFrom")))return false;
            }
            else if(key.equals("thresholdTo")){
                if(isDisplayed(xpath("//button[@data-state='unchecked']")))return false;
               // String str = getElement(xpath("//input[@name='maxTemperatureThreshold']")).getAttribute("value");
                String str = getElement(By.name("maxTemperatureThreshold")).getAttribute("value");
                if(!str.equals(dlpData.get("thresholdTo")))return false;
            }
        }
        return true;
    }
    public Map<String, String> updateDataIntoConfigFile(Map<String, String> dlpData){
        Map<String, String> updatedDLPData = new HashMap<>();
        if(dlpData.containsKey("thresholdTo")){
            click(xpath("//button[@data-state='checked']"));
            List<String>keys = new ArrayList<String>(dlpData.keySet());
            for(String key : keys){
                if(key.equals("thresholdTo"))continue;
                if(key.equals("thresholdFrom"))continue;
                updatedDLPData.put(key, dlpData.get(key));

            }
            click(xpath("//*[text()='Save Changes']//ancestor::button"));
            return updatedDLPData;
        }
        else{
            click(xpath("//button[@data-state='unchecked']"));
            clearAndEnterText(xpath("//input[@name='minTemperatureThreshold']"), "2");
            clearAndEnterText(xpath("//input[@name='maxTemperatureThreshold']"), "10");
            dlpData.put("thresholdFrom", "2");
            dlpData.put("thresholdTo", "10");
            click(xpath("//*[text()='Save Changes']//ancestor::button"));
            return dlpData;
        }
    }
    public void clickForDLPPage(){
        click(xpath("//*[contains(@class, 'lucide-file-sliders')]//following-sibling::*[text()='Data Logger Profiles']"));
    }
    public void clickCrossButton(){
        click(xpath("(//*[contains(@class, 'lucide-x')]//parent::button)[2]"));
    }
    public void clickFullDetailsIconByName(String name){
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//*[contains(@class, 'lucide-expand')]/parent::a"));
    }
    public void clickEditButtonByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//*[contains(@class, 'lucide-pen-line')]/parent::button"));
    }
    public Map<String, String> updateFromGrid(Map<String, String> dlpData){
        clickEditButtonByName(dlpData.get("Name"));
        sleep(2000);
        long time = Instant.now().getEpochSecond();
        String name = "AutoDLP" + time;
        clearAndEnterText(profileNameInput, name);
        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("Name", name);
        List<String> keys = new ArrayList<String>(dlpData.keySet());
        for(String key : keys){
            if(key.equals("Name"))continue;
            updatedData.put(key, dlpData.get(key));
        }
        click(xpath("//*[text()='Update']//ancestor::button"));
        return updatedData;
    }
    public void clickAssignToInDetailsPage(){
        click(xpath("//*[text()='Assigned To']/parent::button[@data-state='inactive']"));
    }
    public boolean activeAssignTo(){
        return isDisplayed(xpath("//*[text()='General Info']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='Configuration']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='Assigned To']/parent::button[@data-state='active']"));
    }
    public boolean profileAssignedToActive(){
        return isDisplayed(xpath("//*[text()='Profile Assigned To']//following-sibling::*//a//button[@data-testid='upload-csv-button']"));
    }
    public boolean tableHeaderShownInAssignToShown(){
        return isDisplayed(xpath("//*[text()='Data Logger ID']//parent::*//following-sibling::*//*[text()='Date Assigned']"));
    }
    public boolean tableDeviceDataCheck(Set<String> devices){
        int i = 0;
        Set<String> stringGets = new HashSet<>();
        for(String str : devices){
            String pqr = getElement(xpath("(//*[@class='w-full truncate whitespace-pre'])["+((i*2) + 1)+"]")).getText();
            stringGets.add(pqr);
            i++;
        }
        return stringGets.equals(devices);
    }
}

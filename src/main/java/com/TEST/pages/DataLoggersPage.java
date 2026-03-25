package com.TEST.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;

public class DataLoggersPage extends BasePage {
    private final By Datagrid = By.xpath("//tbody//tr");
    private final By tableHeaders = By.xpath("//table//thead//tr//th");
    private final By tableBodyRows = By.xpath("//table//tbody//tr");
    String NewDataLogger = "//*[text()='New Data Logger']//ancestor::button";
    private final By newDataLoggerButton = By.xpath(NewDataLogger);
    private final By dataLoggerProfileButton = xpath("//*[text()='Data Logger Profile' and //*[text()='*']]/following-sibling::button");
    private final By dataLoggerSelect = By.xpath(NewDataLogger+"/following-sibling::select");
    private final By dropDownLocator = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By hoverData = xpath("//div[@data-radix-popper-content-wrapper and contains(@style, 'position: fixed')]");
    private final By cancelButton = xpath("//*[text()='Cancel']/ancestor::button");
    private final By organizationButton = xpath("//*[text()='Organization' and //*[text()='*']]/following-sibling::button");
    List<String> DEMO_TESTS = Arrays.asList("DL", "Test", "Asset", "ab", "ka", "ma");
    List<String> HeadersWithDoubleButton = Arrays.asList("Data Logger ID", "Data Logger Type", "Data Logger Profile", "Asset Name",
            "Organization", "Data Logger Manufacturer", "Created By", "Last Modified By");
    List<String> HeadersWithSingleButtonButSearch = Arrays.asList();
    List<String> HeadersWithSingleButtonButSort = Arrays.asList("Battery Level", "Created At", "Last Modified At");
    List<String> HeadersWithoutButton = Arrays.asList("Actions", "Status", "Bind Status");
    String dataLoggerQuery = "SELECT d.devicename as 'Data Logger ID', dt.DeviceTypeName as 'Data Logger Type', dp.DeviceProfileName as 'Data Logger Profile', " +
            "ass.assetname as 'Asset Name', orr.OrganizationName as Organization, dmf.devicemanufacturername as 'Data Logger Manufacturer', CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 108) AS 'Created At', " +
            "cu.Userdisplayname as 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 108) AS 'Last Modified At', uu.Userdisplayname AS 'Last Modified By'" +
            "FROM dbo.Devices d LEFT JOIN dbo.DeviceTypes dt ON d.DeviceTypeId = dt.DeviceTypeId LEFT JOIN dbo.assets ass ON ass.assetid = d.assetid LEFT JOIN dbo.DeviceProfiles dp ON d.DeviceProfileId = dp.DeviceProfileId " +
            "LEFT JOIN dbo.Organizations orr ON d.organizationid = orr.organizationid JOIN dbo.devicemanufacturers dmf ON dt.devicemanufacturerid = dmf.devicemanufacturerid LEFT JOIN dbo.Users cu ON d.CreatedByUserId = cu.UserId " +
            "LEFT JOIN dbo.Users uu ON d.UpdatedByUserId = uu.UserId WHERE d.isinactive = 0";
    String popupCardXpath = "//div[contains(@class, 'popover-foreground')]";
    String dataLoggerIDXpath = "//*[text()='Data Logger ID']//*[text()='*']";
    String dataLoggerIDInputXpath = "/ancestor::label/following-sibling::div//input";
    String dataLoggerProfileXpath = "//*[text()='Data Logger Profile']//*[text()='*']";
    String organizationXpath = "//*[text()='Organization']//*[text()='*']";
    String dropDownXpath = "//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]";
    String popupCardDropdownSelectXpath = "/ancestor::label/following-sibling::div//select";
    String popupCardDropdownButtonXpath = "/ancestor::label/following-sibling::div//button";
    String CreateButtonXpath = "//*[text()='Create']/ancestor::button";

    public DataLoggersPage(WebDriver driver){
        super(driver);
    }
    public static void exportToCSV(ResultSet rs, String filePath) {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write header
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.append(metaData.getColumnLabel(i));
                if (i < columnCount) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    csvWriter.append(rs.getString(i) != null ? rs.getString(i) : ""); // Handle NULL values
                    if (i < columnCount) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
            System.out.println("Data exported successfully to " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getHeaders(){
        List<String> headers = new ArrayList<>();
        List<WebElement> webElementsHeaders = getElements(tableHeaders);
        for(int i = 0; i < webElementsHeaders.size(); i++){
            String str = webElementsHeaders.get(i).getText().trim();
            if(str.isEmpty())continue;
            if(str.equals("Actions"))continue;
            if(str.equals("Status"))continue;
            if(str.equals("Bind Status"))continue;
            if(str.equals("Battery Level"))continue;
            headers.add(str);
        }
        return headers;
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
    public String getCellDataByHover(int columnIndex, int row) {
        hoverOverElement(By.xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1)+"]//div[@data-state]"));
        sleep(1000);
        String text = getText(hoverData);
        String[] lines = text.split("\n");
        return lines.length > 0 ? lines[0].trim() : "";
    }

    public List<List<String>> getBody() {
        List<List<String>> body = new ArrayList<>();
        List<WebElement> rows = getElements(tableBodyRows);
        if (rows.isEmpty()) return body;

        int statusColumnIndex = getColumnIndex("Status");
        int bindStatusColumnIndex = getColumnIndex("Bind Status");
        int batteryLevelColumnIndex = getColumnIndex("Battery Level");
        int actionColumnIndex = getColumnIndex("Actions");
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cellInRow = rows.get(i).findElements(By.xpath(".//td"));
            if(cellInRow.size() == 1){
                boolean x = cellInRow.get(0).getText().equals("No results.");
                if(x)return body;
                List<String> onlySortButtonContains = Arrays.asList("Battery Level" , "Created At", "Last Modified At");
                body.add(onlySortButtonContains);
                return body;
            }
            List<String> row = new ArrayList<>();
            for (int j = 1; j < cellInRow.size(); j++) {
                if (j == actionColumnIndex || j == statusColumnIndex || j == bindStatusColumnIndex || j == batteryLevelColumnIndex) {
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
    public static void exportToCSV(List<List<String>> table, String filePath) {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            // Iterate over the rows in the table
            for (List<String> row : table) {
                // Write each row as a line in the CSV
                csvWriter.append(String.join(",", row));
                csvWriter.append("\n");
            }
            System.out.println("CSV file generated successfully at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clickSort(String columnName) {
        columnName = columnName.replace("'", "");
        int columnIndex = getColumnIndex(columnName);
        //System.out.println(columnIndex);
        List<String> onlySortButtonContains = Arrays.asList("Battery Level" , "Created At", "Last Modified At");
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
        By sortButtonBy = By.xpath("//table/thead/tr/th[" + (columnIndex + 1) + "]//"+button);
        //System.out.println("//table/thead/tr/th[" + (columnIndex + 1) + "]//"+button);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(sortButtonBy));

        // Scroll to the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortButton);
        sleep(1000);
        // Click the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortButton);
    }

    public void findAndClickSearchButton(By sortButtonBy) {
        int attempts = 0;

        while (attempts < 3) { // Retry mechanism
            try {
                // Dynamically locate the element in each attempt
                WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(sortButtonBy));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sortButton);

                try {
                    // Attempt to click the element using JavaScript
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortButton);
                } catch (JavascriptException e) {
                    // Fallback to Actions API if JavaScript fails
                    Actions actions = new Actions(driver);
                    actions.moveToElement(sortButton).click().perform();
                }
                return; // Exit if successful
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException encountered. Retrying...");
            } catch (TimeoutException e) {
                System.out.println("TimeoutException: Element not found within the wait duration.");
            } catch (Exception e) {
                System.out.println("Unexpected exception encountered: " + e.getMessage());
            }
            attempts++;
            sleep(3000); // Wait before retrying
        }

        throw new RuntimeException("Failed to click the sort button after multiple attempts.");
    }
    public int setHeaderIntoXpathForSearchButtonClick(String header){
        try {
            System.out.println(header);
            if (HeadersWithDoubleButton.contains(header)) {
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button[1]"));
                sleep(2000);
                boolean situation = isDisplayed(By.xpath("//*[@placeholder='Search']"));
                System.out.println("ddd");
                if (!situation) return -1;
                situation = isDisplayed(By.xpath("//*[@placeholder='Search']//following-sibling::span/*[1]"));
                if(!situation) return 0;
                else return 1;
            } else if (HeadersWithSingleButtonButSearch.contains(header)) {
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                boolean situation = isDisplayed(By.xpath("//*[@placeholder='Search']"));
                if (!situation) return -1;
                situation = isDisplayed(By.xpath("//*[@placeholder='Search']//following-sibling::span/*[1]"));
                if(!situation) return 0;
                else return 1;
            } else if (HeadersWithSingleButtonButSort.contains(header)) {
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                boolean situation = isDisplayed(By.xpath("//*[@placeholder='Search']"));
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
                if (situation) return -1;
            } else if (HeadersWithoutButton.contains(header)) {
                boolean situation = isDisplayed(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button"));
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
            System.out.println(header);
            int status = setHeaderIntoXpathForSearchButtonClick(header);
            System.out.println(status);
            //status:-1:: button not found
            //status:0:: button found but, not search button
            //status:1:: search box open
            //status:2:: what happened, don't know
            if(status >= 1){
                if(status == 1)waitForVisibility(By.xpath("//*[@placeholder='Search']//following-sibling::span/*[1]")).click();
            }
            else return false;
            sleep(1000);
        }
        return true;
    }
    public boolean checkSearchOnColumn(String ColumnName, String demo_test, List<List<String>> table){
        int status = setHeaderIntoXpathForSearchButtonClick(ColumnName);
        if(status == 1){
            getElement(By.xpath("//input[@placeholder='Search']")).sendKeys(demo_test);
            sleep(2000);
            getElement(By.xpath("//input[@placeholder='Search']//following-sibling::span//*[@stroke-linecap='round'][2]")).click();
            sleep(2000);
            List<List<String>> tableData = getTable();
            getElement(By.xpath("//span[text()='"+ColumnName+" | "+demo_test+"']/following-sibling::*[contains(@class, 'lucide-circle-x')]")).click();
            System.out.println(tableData);
            System.out.println(table);
            System.out.println(tableData.equals(table));
            return tableData.equals(table);
        }
        else return true;
    }
    void clickNewDataLogger(){
        waitForVisibility(newDataLoggerButton).click();
    }
    void selectDataLoggerType(String dataLoggerType){
        selectDropdownByText(dropDownLocator, dataLoggerType);
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
    boolean checkCard(String header){
        boolean displayHeader = isDisplayed(By.xpath(popupCardXpath+" //*[text()='New' and text()='"+header+"']"));
        boolean displayDataLoggerID = isDisplayed(By.xpath(popupCardXpath+dataLoggerIDXpath));
        boolean displayDataLoggerIDInput = isDisplayed(By.xpath(popupCardXpath+dataLoggerIDXpath+dataLoggerIDInputXpath));
        boolean displayDataLoggerProfile = isDisplayed(By.xpath(popupCardXpath+dataLoggerProfileXpath));
        boolean displayOrganization = isDisplayed(By.xpath(popupCardXpath+organizationXpath));
        return displayOrganization & displayDataLoggerProfile
                & displayDataLoggerIDInput & displayDataLoggerID & displayHeader;
    }
    public Map<String, String> createNewDataLogger(String dataLoggerType){
        clickNewDataLogger();
        long time = Instant.now().getEpochSecond();
        Map<String, String> DLData = new HashMap<>();
        selectDataLoggerType(dataLoggerType);
        DLData.put("dataLoggerType", dataLoggerType);
        boolean status = checkCard(dataLoggerType);
        if(!status)return null;
        String dataLoggerID = "AutoDLID_" + time;
        DLData.put("dataLoggerID", dataLoggerID);
        getElement(By.xpath(popupCardXpath+dataLoggerIDXpath+dataLoggerIDInputXpath)).sendKeys(dataLoggerID);
        click(organizationButton);
        String organization = "doglapan Training - Air Cargo";
        DLData.put("organization", organization);
        selectDropdownWithSearchByText(organization,"49194360-27ab-4434-991f-88116dc775bb");
        click(dataLoggerProfileButton);
        String dataLoggerProfile = "";
        for(int i = 0; i < deviceProfilesData.size(); i++){
            if(deviceProfilesData.get(i).get(2).equals(dataLoggerType)){
                dataLoggerProfile = deviceProfilesData.get(i).get(1);
                selectDropdownWithSearchByText(dataLoggerProfile, deviceProfilesData.get(i).get(0));
                DLData.put("dataLoggerProfile", dataLoggerProfile);
                break;
            }
        }
        click(By.xpath(CreateButtonXpath));
        return DLData;
    }
    public boolean checkRowByColumn(String ColumnName, int rowNumber, String cellData){
        int columnIndex = getColumnIndex(ColumnName);
        return cellData.equals(getElement(By.xpath("//tbody//tr["+(rowNumber)+"]//td["+(columnIndex+1)+"]")).getText());
    }
    List<List<String>> deviceProfilesData = List.of(
            List.of("f5e54aeb-1db4-405a-bf7c-41904e8f87de", "Auto Profile Tradoglapan Pro1741086370", "Tradoglapan Pro"),
            List.of("14c3d968-6e6c-455d-a383-cbe20377762d", "Auto Profile Tradoglapan Max1741092972", "Tradoglapan Max"),
            List.of("ee86b997-2c5d-477f-9f8e-f80da25212da", "Auto Profile Tradoglapan RLT T721741093203", "Tradoglapan RLT T72"),
            List.of("cf8a422a-f9d1-41b2-ac81-b201bf882a00", "Auto Profile Tradoglapan NFC1741093322", "Tradoglapan NFC"),
            List.of("d4f0bd38-2938-455e-bf11-78b008c74b1b", "Auto Profile Sensitech Ultra1741093436", "Sensitech Ultra"),
            List.of("3fe39f83-6425-403d-a4ee-034759700aa3", "Auto Profile Sendum PT300d1741093558", "Sendum PT300d"),
            List.of("02700771-d207-4cf9-957e-0272e4d0a74b", "Auto Profile OnAsset S6001741093664", "OnAsset S600"),
            List.of("bdf341ff-069d-4476-8fd0-cb948254baf2", "Auto Profile OnAsset S1001741093764", "OnAsset S100")
    );

    public boolean validation(){
        clickNewDataLogger();
        List<WebElement> dataLoggerTypeList = getElement(dataLoggerSelect).findElements(By.xpath(".//option"));
        for(int i = 0; i < dataLoggerTypeList.size(); i++){
            if(i!=0)clickNewDataLogger();
            String dataLoggerTypeFromDevice = dataLoggerTypeList.get(i).getText();
            selectDropdownByText(dropDownLocator, dataLoggerTypeFromDevice);
            sleep(500);
            if(dataLoggerTypeFromDevice.equals("OnAsset S100"))continue;
            click(dataLoggerProfileButton);
            for(int j = 0; j < deviceProfilesData.size(); j++){
                String dataLoggerTypeFromDeviceProfile = deviceProfilesData.get(j).get(2);
                if(dataLoggerTypeFromDeviceProfile.equals(dataLoggerTypeFromDevice)){
                    boolean visible = selectDropdownWithSearchByTextForFound(deviceProfilesData.get(j).get(1), deviceProfilesData.get(j).get(0));
                    if(!visible)return false;
                }
                else{
                    boolean visible = selectDropdownWithSearchByTextForNotFound(deviceProfilesData.get(j).get(1));
                    if(!visible)return false;
                }
            }
            click(cancelButton);
            sleep(500);
        }
        return true;
    }
    public int getRowIndex(String Name){
        int columnIndex = getColumnIndex("Data Logger ID");
        List<WebElement> rows = getElements(Datagrid);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).findElement(By.xpath(".//td["+(columnIndex + 1) + "]")).getText().equals(Name))return i;
        }
        return 0;
    }
    public void deleteDLByName(String deviceName){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(deviceName);
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
    public void searchDLByName(String name) {
        getElement(xpath("//*[text()='Data Logger ID']/following-sibling::div/button[1]")).click();
        clearAndEnterText(xpath("//input[@data-testid='searchInput']"), name);
        keyboardKeyPress(Keys.ENTER);
    }
    public void clickOnDLName(String DLName){
        int columnIndex = getColumnIndex("Data Logger ID");
        int rowIndex = getRowIndex(DLName);
        click(xpath("//table//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//button"));
    }
    public Map<String, String> getDataFromDLSummary(){
        Map<String, String> DLData = new HashMap<>();
        String DLName = getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        DLData.put("Data Logger ID", DLName);
        Set<String> gettingOptions = new HashSet<>();
        List<WebElement> optionsHeaders = getElements(xpath("//*[contains(@class,'border-doglapan-blue-30')]//*[contains(@class,'font-semibold')]"));
        System.out.println(optionsHeaders.size());
        for(WebElement headers : optionsHeaders){
            gettingOptions.add(headers.getText());
        }
        Set<String> expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList(
                "Status", "Battery Level", "Last Modified By", "Last Modified At",
                "Created By", "Created At", "Data Logger Type", "Bound Asset", "Manufacturer"
        ));
        for(String expectedOption : expectedOptions){
            String str = "";
            if(expectedOption.equals("Bound Asset") || expectedOption.equals("Manufacturer")) str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[1]")).getText();
            else{
                str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[2]")).getText();
            }
            DLData.put(expectedOption, str);
        }
        if(!expectedOptions.equals(gettingOptions))return null;
        return DLData;
    }
    public boolean checkSummaryDataWithGivenData(Map<String, String> summary, Map<String, String> givenData){
        return summary.get("Data Logger ID").equals(givenData.get("dataLoggerID")) &&
                summary.get("Data Logger Type").equals(givenData.get("dataLoggerType"));
    }
    public void clickFullDetails(){
        click(xpath("//*[text()='View Full Detail']//parent::a"));
    }
    public boolean checkBanner(String DLName){
        return isDisplayed(xpath("//*[text()='Data Loggers']/parent::*//following-sibling::*[contains(@class, 'lucide-chevron-right')]//following-sibling::*[text()='"+DLName+"']"));
    }
    public boolean validateDLSnapShot(Map<String, String> DLData){
        boolean bannerStatus = checkBanner(DLData.get("dataLoggerID"));
        if(!bannerStatus)return false;
        if(!isDisplayed(xpath("//*[text()='Status and General Info']/parent::button[@data-state='active']//following-sibling::*//*[text()='In Map']/parent::button[@data-state='inactive']//following-sibling::*//*[text()='History']/parent::button[@data-state='inactive']")))return false;
        List<WebElement> gettingHeadersWebElement = getElements(xpath("//*[@data-state='active']//*[contains(@class,'text-xs font-semibold text-doglapan-grey-80')]"));
        Set<String> gettingHeaders = new HashSet<>();
        for(WebElement element : gettingHeadersWebElement){
            gettingHeaders.add(element.getText());
        }
        Set<String>expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList("Last Modified By", "Data Logger ID", "Data Logger Type", "Data Logger Manufacturer",
                "Last Modified At", "Bound Asset", "Created By", "Created At"));
        if(!gettingHeaders.equals(expectedOptions))return false;
        for(String header : gettingHeaders){
            if(header.equals("Bound Asset"))continue;
            String str = getElement(xpath("//*[text()='"+header+"']/following-sibling::*")).getText();
            System.out.println(header);
            if(header.equals("Data Logger ID")){
                if(!str.equals(DLData.get("dataLoggerID")))return false;
            }
            else if(header.equals("Data Logger Type")){
                if(!str.equals(DLData.get("dataLoggerType")))return false;
            }
        }
        return true;
    }
    public void clickDLForGridPage(){
        click(xpath("//button[text()='Data Loggers']"));
    }
    public void clickEditButtonByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//*[contains(@class, 'lucide-pen-line')]/parent::button"));
    }
    public Map<String, String> updateDLByName(Map<String, String> DLData){
        clickEditButtonByName(DLData.get("dataLoggerID"));
        sleep(1000);
        Map<String, String> updateDLData = new HashMap<>();
        click(organizationButton);
        String organization = "SV Test - Child 01";
        updateDLData.put("organization", organization);
        selectDropdownWithSearchByText(organization,"9f468e3a-a540-4713-ba6d-e7f06722dcc7");
        List<String> lists = new ArrayList<String>(DLData.keySet());
        for(String list :  lists){
            if(list.equals("organization"))continue;
            updateDLData.put(list, DLData.get(list));
        }
        sleep(1000);
        click(dataLoggerProfileButton);
        String dataLoggerProfile = "Profile_Tradoglapan Max";
        selectDropdownWithSearchByText(dataLoggerProfile, "45b4f901-b2c2-40b8-af87-6d49617e23ed");
        updateDLData.put("dataLoggerProfile", dataLoggerProfile);
        for(String list :  lists){
            if(list.equals("organization"))continue;
            if(list.equals("dataLoggerProfile"))continue;
            updateDLData.put(list, DLData.get(list));
        }
        sleep(1000);
        click(xpath("//*[contains(@class, 'lucide-check')]//following-sibling::*[text()='Update']/ancestor::button"));
        return updateDLData;
    }
    public boolean validateColumnDataByName(String columnName, String DLID, String expectedString){
        int columnIndex = getColumnIndex(columnName);
        int rowIndex= getRowIndex(DLID);
        String str = getElement(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//p")).getText();
        return expectedString.equals(str);
    }
    public void clickOnInMap(){
        click(xpath("//*[text()='In Map']/parent::button[@data-state='inactive']"));
    }
    public boolean ValidatePushedLocationData(String longT, String latT){
        String lat = getElement(xpath("(//p)[1]")).getText();
        String lon = getElement(xpath("(//p)[2]")).getText();
        System.out.println(lat);
        System.out.println(lon);
        if(!lat.equals("Latitude" + latT))return false;
        return lon.equals("Longitude" + longT);
    }
}

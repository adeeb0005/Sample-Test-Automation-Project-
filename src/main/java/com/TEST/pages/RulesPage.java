package com.TEST.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class RulesPage extends BasePage{
//    private final By addNewRuleButton = By.xpath("//button[@data-testid='addNewButton']");
    private final By Datagrid = By.xpath("//tbody//tr");
    private final By columnHeaders = By.xpath("//div//th");
    private final By addNewRuleButton = By.xpath("//*[text()='New Rule']/ancestor::button[position()=1]");
//    private final By ruleNameField = By.xpath("//input[@name='name']");
    private final By organizationButton = By.xpath("(//*[text()='Organization' and //*[text()='*']])[1]//following-sibling::button");
    private final By ruleNameField = By.xpath("//*[text()='Rule Name']/following-sibling::div/input[position()=1]");
//    private final By organizationSelect = By.xpath("//select[@data-testid='organization']");
    private final By organizationSelect = By.xpath("//label[text()='Organization']/following-sibling::div/select");
//    private final By cargoTemperatureRadioButton = By.xpath("//button[@data-testid='cargo temp radio']");
    private final By cargoTemperatureRadioButton = By.xpath("//*[@data-testid='probeTemp-switch']");
    private final By cargoTemperatureFromField = By.xpath("//*[@data-testid='probeTemp-from-input-field']");
    private final By cargoTemperatureToField = By.xpath("//*[@data-testid='probeTemp-to-input-field']");
    private final By cargoTemperatureDurationField = By.xpath("//*[@data-testid='probeTemp-duration-input-field']");

    private final By ambientTemperatureRadioButton = By.xpath("//button[@data-testid='ambient temp radio']");
    private final By shockRadioButton = By.xpath("//button[@data-testid='shock radio']");
    private final By tiltXRadioButton = By.xpath("//button[@data-testid='tiltx radio']");
    private final By tiltYRadioButton = By.xpath("//button[@data-testid='tilty radio']");
    private final By tiltZRadioButton = By.xpath("//button[@data-testid='tiltz radio']");
    private final By pressureRadioButton = By.xpath("//button[@data-testid='pressure radio']");
    private final By cargoHumidityRadioButton = By.xpath("//button[@data-testid='cargo humidity radio']");
    private final By ambientHumidityRadioButton = By.xpath("//button[@data-testid='ambient humidity radio']");
    private final By lightRadioButton = By.xpath("//button[@data-testid='light radio']");
    private final By doorRadioButton = By.xpath("//button[@data-testid='door radio']");
    private final By emailsField = By.xpath("//input[@name='emails']");
//    private final By emailsField = By.xpath("//*[@id=\":r45:-form-item\"]");
    private final By visibilityLevelButton = By.xpath("//button[@data-testid='visibility level']");
//    private final By visibilityLevelSelect = By.xpath("//select[@data-testid='visibility level']");
    private final By visibilityLevelSelect = By.xpath("//*[@data-testid='visibility-select']/select");

    private final By createButton = By.xpath("//*[text()='Create']/ancestor::button");
    private final By updateButton = By.xpath("//*[text()='Update']/ancestor::button");
    private final By ruleCreationSuccessMessage = By.xpath("//*[text()='Rule Created Successfully']");
    private final By ruleUpdateSuccessMessage = By.xpath("//*[text()='Rule Updated Successfully']");

    private final By tableHeaders = By.xpath("//table//thead//tr//th");
    private final By tableBodyRows = By.xpath("//table//tbody//tr");
    List<String> HeadersWithDoubleButton = Arrays.asList("Name", "Organization", "Created By", "Last Modified By");
    List<String> HeadersWithSingleButtonButSort = Arrays.asList("Created At", "Last Modified At");
    List<String> HeadersWithoutButton = Arrays.asList("Actions");
    public RulesPage(WebDriver driver){
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
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button[1]"));
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
                sleep(5000);
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
            sleep(2000);
            getElement(By.xpath("//input[@placeholder='Search']")).sendKeys(demo_test);
            //sleep(2000);
            getElement(By.xpath("//input[@placeholder='Search']//following-sibling::span//*[@stroke-linecap='round'][2]")).click();
            sleep(2000);
            List<List<String>> tableData = getTable();
            sleep(2000);
            getElement(By.xpath("//span[text()='"+ColumnName+" | "+demo_test+"']/following-sibling::*[contains(@class, 'lucide-circle-x')]")).click();
            return tableData.equals(table);
        }
        else return false;
    }

    public Map<String, String> createNewRule() {
        long time = Instant.now().getEpochSecond();
        Map<String, String> ruleData = new HashMap<>();
        click(addNewRuleButton);
        String ruleName = "AutoRule" + time;
        clearAndEnterText(ruleNameField, ruleName);
        ruleData.put("ruleName", ruleName);
        click(organizationButton);
        selectDropdownWithSearchByText("doglapan Training - Air Cargo", "49194360-27ab-4434-991f-88116dc775bb");
        ruleData.put("organization", "doglapan Training - Air Cargo");
        click(cargoTemperatureRadioButton);
        clearAndEnterText(cargoTemperatureFromField, "-4");
        ruleData.put("cargoTemperatureFrom", "-4");
        clearAndEnterText(cargoTemperatureToField, "-2");
        ruleData.put("cargoTemperatureToField", "-2");
        clearAndEnterText(cargoTemperatureDurationField, "10");
        ruleData.put("cargoTemperatureDuration", "10");
        clearAndEnterText(emailsField, "test@example.com");
        ruleData.put("emails", "test@example.com");
        selectDropdownByText(visibilityLevelSelect, "Organization Only");
        ruleData.put("visibilityLevel", "Organization Only");
        click(createButton);
        boolean status = waitForTextToBePresent(ruleCreationSuccessMessage, "Rule Created Successfully");
        if(!status)return null;
        return ruleData;
    }

    public void searchRuleByName(String name) {
        getElement(xpath("//*[text()='Name']/following-sibling::div/button[1]")).click();
        clearAndEnterText(xpath("//input[@data-testid='searchInput']"), name);
        keyboardKeyPress(Keys.ENTER);
    }

    public boolean updateRulesFunctionality(String ruleName) throws InterruptedException {
        searchRuleByName(ruleName);
        sleep(2000);
        click(xpath("(//table/tbody/tr[1]/td[3]//button)[1]"));
        enterText(emailsField, ", test1@example.com");
        click(updateButton);
        waitForTextToBePresent(ruleUpdateSuccessMessage, "Rule Updated Successfully");
        Thread.sleep(2000);
        return true;
    }
    public int getRowIndex(String Name){
        int columnIndex = getColumnIndex("Name");
        List<WebElement> rows = getElements(Datagrid);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).findElement(By.xpath(".//td["+(columnIndex + 1) + "]")).getText().equals(Name))return i;
        }
        return 0;
    }
    public void deleteRuleByName(String ruleName){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(ruleName);
        waitForVisibility(By.xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]/parent::button")).click();
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
    }
    public void clickOnRuleName(String ruleName){
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(ruleName);
        click(xpath("//table//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]"));
    }
    public Map<String, String> getDataFromRuleSummary(){
        Map<String, String> ruleData = new HashMap<>();
        String ruleName = getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        ruleData.put("ruleName", ruleName);
        Set<String> gettingOptions = new HashSet<>();
        gettingOptions.add(getElement(xpath("//*[@class='text-xs font-semibold text-doglapan-grey-80']")).getText());
        List<WebElement> optionsHeaders = getElements(xpath("//*[@class='whitespace-pre-wrap text-xs font-semibold text-doglapan-grey-80']"));
        System.out.println(optionsHeaders.size());
        for(WebElement headers : optionsHeaders){
            gettingOptions.add(headers.getText());
        }
        Set<String> expectedOptions = new HashSet<>();
        expectedOptions.addAll(Arrays.asList(
                "Shock", "Last Modified By", "Tilt X", "Last Modified At", "Tilt Z", "Tilt Y",
                "Email to", "Cargo Humidity", "Pressure", "Ambient Temperature", "Door",
                "Ambient Humidity", "Cargo Temperature", "Light", "Tilt", "Shock Z",
                "Created By", "Component", "Shock Y", "Shock X", "Created At"
        ));
        for(String expectedOption : expectedOptions){
            String str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[1]")).getText();
            if(expectedOption.endsWith("At") || expectedOption.endsWith("By"))str = getElement(xpath("(//*[text()='"+expectedOption+"']//following-sibling::*)[2]")).getText();
            if(str.equals("-"))continue;
            if(str.equals("N/A"))continue;
            ruleData.put(expectedOption, str);
        }
        if(!expectedOptions.equals(gettingOptions))return null;
        return ruleData;
    }
    public boolean checkSummaryDataWithGivenData(Map<String, String> summary, Map<String, String> givenData){
        boolean status = true;
        System.out.println("zigzag");
        if(!summary.get("ruleName").equals(givenData.get("ruleName")))return false;
        if(givenData.containsKey("cargoTemperatureFrom")){
            String data = "Below "+givenData.get("cargoTemperatureFrom")+"°C Above "+ givenData.get("cargoTemperatureToField")+"°C";
            System.out.println("zigzag"+data);
            if(!data.equals(summary.get("Cargo Temperature")))return false;
        }
        if(givenData.containsKey("ambientTemperatureTo")){
            String data = "Below "+givenData.get("ambientTemperatureFrom")+"°C Above "+ givenData.get("ambientTemperatureTo")+"°C";
            System.out.println("zigzag"+data);
            if(!data.equals(summary.get("Ambient Temperature")))return false;
        }
        if(givenData.containsKey("ambientHumidityFrom")){
            String data = "Below "+givenData.get("ambientHumidityFrom")+"% Above "+givenData.get("ambientHumidityTo")+"%";
            System.out.println("zigzag"+data);
            if(!data.equals(summary.get("Ambient Humidity")))return false;
        }
        if(!summary.get("Email to").equals(givenData.get("emails")))return false;
        return status;
    }
    public void clickOnRuleEditButton(){
        click(xpath("//*[contains(@class, 'lucide-pencil-line')]/following-sibling::*[text()='Edit Rule']/parent::button"));
    }
    public Map<String, String> updateRulesByNameWithSummaryEditButton(Map<String, String> ruleData){
        clickOnRuleEditButton();
        sleep(1000);
        Map<String, String> updatedData = new HashMap<>();
        long time = Instant.now().getEpochSecond();
        String ruleName = "AutoRule" + time;
        updatedData.put("ruleName", ruleName);
        List<String> lists = new ArrayList<String>(ruleData.keySet());
        for(String list : lists){
            if(list.equals("ruleName"))continue;
            updatedData.put(list, ruleData.get(list));
        }
        clearAndEnterText(ruleNameField, ruleName);
        click(xpath("//*[contains(@class, 'lucide-check')]//following-sibling::*[text()='Update']/ancestor::button"));
        return updatedData;
    }
    public void clickCrossButton(){
        click(xpath("(//*[contains(@class, 'lucide-x')]//parent::button)[2]"));
    }
    public void clickEditButtonByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(name);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//td["+(columnIndex + 1)+"]//*[contains(@class, 'lucide-pen-line')]/parent::button"));
    }
    public Map<String, String> updateRulesByNameWithEditButtonFromGrid(Map<String, String> ruleData){
        clickEditButtonByName(ruleData.get("ruleName"));
        Map<String, String>updatedData = new HashMap<>();
        click(xpath("//*[@data-testid='probeTemp-switch']"));
        click(xpath("//*[@data-testid='deviceTemp-switch']"));
        clearAndEnterText(xpath("//*[@data-testid='deviceTemp-from-input-field']"), "-10");
        updatedData.put("ambientTemperatureFrom", "-10");
        clearAndEnterText(xpath("//*[@data-testid='deviceTemp-to-input-field']"), "-5");
        updatedData.put("ambientTemperatureTo", "-5");
        clearAndEnterText(xpath("//*[@data-testid='deviceTemp-duration-input-field']"), "10");
        click(xpath("//*[@data-testid='deviceHumidity-switch']"));
        clearAndEnterText(xpath("//*[@data-testid='deviceHumidity-from-input-field']"), "5");
        updatedData.put("ambientHumidityFrom", "5");
        clearAndEnterText(xpath("//*[@data-testid='deviceHumidity-to-input-field']"), "20");
        updatedData.put("ambientHumidityTo", "20");
        clearAndEnterText(xpath("//*[@data-testid='deviceHumidity-duration-input-field']"), "10");
        List<String> lists = new ArrayList<String>(ruleData.keySet());
        for(String list : lists){
            if(list.equals("cargoTemperatureFrom"))continue;
            if(list.equals("cargoTemperatureToField"))continue;
            updatedData.put(list, ruleData.get(list));
        }
        click(xpath("//*[contains(@class, 'lucide-check')]//following-sibling::*[text()='Update']/ancestor::button"));
        return updatedData;
    }
}

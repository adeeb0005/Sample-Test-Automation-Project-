package com.TEST.pages;


import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

public class BasePage extends Page{


    protected WebDriverWait wait;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIME));
        this.actions = new Actions(driver);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        // maximizeWindow();
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Todo: need to make it efficient (combine waitForVisibility and waitForVisibilityWithoutWait
    protected WebElement waitForVisibility(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        sleep(DEFAULT_SLEEP_TIME_MILLISECONDS);
        logAction("Waited for visibility of element: " + locator);
        return element;
    }

    protected boolean isDisplayed(By locator) {
        try {
            boolean displayed = waitForVisibility(locator).isDisplayed();
            logAction("Element displayed status for " + locator + ": " + displayed);
            return displayed;
        } catch (TimeoutException e) {
            logAction("Element " + locator + " is not displayed.");
            return false;
        }
    }


    protected WebElement waitForVisibilityWithoutWait(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        logAction("Waited for visibility of element: " + locator);
        return element;
    }

    protected boolean isDisplayedWithoutWait(By locator) {
        try {
            boolean displayed = waitForVisibilityWithoutWait(locator).isDisplayed();
            logAction("Element displayed status for " + locator + ": " + displayed);
            return displayed;
        } catch (TimeoutException e) {
            logAction("Element " + locator + " is not displayed.");
            return false;
        }
    }

    protected void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        sleep(DEFAULT_SLEEP_TIME_MILLISECONDS);
        logAction("Waited for element to become invisible.");
    }

    protected void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        sleep(DEFAULT_SLEEP_TIME_MILLISECONDS);
        logAction("Waited for element to disappear.");
    }

    protected boolean waitForTextToBePresent(By locator, String text) {
        boolean isTextPresent = wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        if (isDisplayed(locator)) {
            logAction("Waited for text '" + text + "' to be present in element: " + locator);
        }
        return isTextPresent;
    }

    protected void waitForElementToAppear(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (isDisplayed(locator)) {
            logAction("Waited for element to appear: " + locator);
        }
    }

    protected void waitForPageToLoadCompletely() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        logAction("Waited for page to load completely");
    }

    protected WebElement waitForElementToBeClickable(By locator) {
        if (!isDisplayed(locator)) {
            throw new IllegalStateException("Element is not displayed before waiting: " + locator);
        }
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        logAction("Waited for element to be clickable: " + locator);
        return element;
    }

    protected WebElement getElement(By locator) {
        WebElement element = waitForVisibility(locator);
        logAction("Retrieved single element: " + locator);
        return element;
    }

    protected List<WebElement> getElements(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        logAction("Retrieved multiple elements for locator: " + locator + ", count: " + elements.size());
        return elements;
    }

    protected int getItemCount(By locator) {
        return this.getElements(locator).size();
    }

    public List<String> getAttributeValuesInList(By locator, String attribute) {
        List<WebElement> elements = getElements(locator);
        List<String> attributeValues = new ArrayList<>();
        for (WebElement element : elements) {
            String attributeValue = element.getAttribute(attribute);
            if (attributeValue != null) {
                attributeValues.add(attributeValue);
            }
        }
        return attributeValues;
    }

    // Click action
    protected void click(By locator) {
        waitForElementToBeClickable(locator).click();
        logAction("Clicked on: " + locator);
    }

    protected void clickUsingJS(By locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", waitForVisibility(locator));
        logAction("Clicked on element using JavaScript: " + locator);
    }

    protected void hoverOverElement(By locator) {
        WebElement element = waitForVisibility(locator);

        if (element == null) {
            throw new RuntimeException("Element not found: " + locator);
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        try {
            actions.moveToElement(element).perform();
            logAction("Hovered over element: " + locator);
        } catch (Exception e) {
            throw new RuntimeException("Hover action failed for: " + locator, e);
        }
    }


    protected void doubleClick(By locator) {
        actions.doubleClick(waitForVisibility(locator)).perform();
        logAction("Double-clicked on element: " + locator);
    }

    // Enter text into a text field
    protected void enterText(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.click();
        element.sendKeys(text);
        logAction("Entered text: '" + text + "' in: " + locator);
    }

    // Enter text after clearing the field
    protected void clearAndEnterText(By locator, String text) {
        scrollToElement(locator);
        WebElement element = waitForVisibility(locator);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(text);
        logAction("Clear the field then enter text '" + text + "' in: " +locator);
    }

    protected void keyboardKeyPress(Keys key) {
        try {
            actions.sendKeys(key).perform(); // Use the already initialized `actions`
            logAction("Pressed the key: " + key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to press the key: " + key, e);
        }
    }

    protected String getText(By locator) {
        String text = waitForVisibility(locator).getText();
        logAction("Retrieved text '" + text + "' from element: " + locator);
        return text;
    }

    protected String getAttributeValue(By locator, String attribute) {
        String value = waitForVisibility(locator).getAttribute(attribute);
        logAction("Retrieved attribute value '" + value + "' from element: " + locator);
        return value;
    }

    void scrollToView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    protected void selectDropdownWithSearchByText(String text){
        getElement(xpath("//*[@placeholder='Search...']")).sendKeys(text);
        sleep(2000);
        Set<String> seenOptions = new HashSet<>();
        boolean newOptionsLoaded = true;
        boolean done = false;
        while (newOptionsLoaded && !done) {
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
                if (optionText.equals(text)) {
                    option.click();
                    done = true;
                    break;
                }
            }
        }
    }
    protected boolean selectDropdownWithSearchByTextForNotFound(String text){
        getElement(xpath("//*[@placeholder='Search...']")).clear();
        getElement(xpath("//*[@placeholder='Search...']")).sendKeys(text);
        sleep(2000);
        return isDisplayed(xpath("//*[text()='No results found']"));
    }
    protected boolean selectDropdownWithSearchByTextForFound(String text, String id){
        getElement(xpath("//*[@placeholder='Search...']")).clear();
        getElement(xpath("//*[@placeholder='Search...']")).sendKeys(text);
        sleep(2000);
        Set<String> seenOptions = new HashSet<>();
        boolean newOptionsLoaded = true;
        while (newOptionsLoaded) {
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
                if (optionText.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
    protected void selectDropdownWithSearchByText(String text, String id){
        getElement(xpath("//*[@placeholder='Search...']")).sendKeys(text);
        sleep(2000);
        Set<String> seenOptions = new HashSet<>();
        boolean newOptionsLoaded = true, done = false;
        while (newOptionsLoaded) {
            if(done)break;
            newOptionsLoaded = false;
            List<WebElement> options = driver.findElements(By.xpath("//div[@cmdk-list-sizer]//div[@cmdk-item]"));
            for (WebElement option : options) {
                String optionText = option.getAttribute("data-value");
                if (!seenOptions.contains(optionText)) {
                    seenOptions.add(optionText);
                    newOptionsLoaded = true;
                }
                scrollToElement(option);
                sleep(1000);
                if (optionText.equals(id)) {
                    option.click();
                    done = true;
                    break;
                }
            }
        }
    }
    protected void selectDropdownByText(By locator, String text) {
        try {
            // Locate the option using the text directly
            WebElement option = getElement(locator).findElement(By.xpath(".//*[text()='" + text + "']"));
            scrollToView(option); // Scroll the element into view
            sleep(1000);
            option.click();
        } catch (Exception e) {
            try {
                // Locate the option by navigating to its parent div
                WebElement option = getElement(locator).findElement(By.xpath(".//*[text()='" + text + "']/parent::div"));
                scrollToView(option);
                sleep(1000);
                option.click();
            } catch (Exception ex) {
                try {
                    // Locate the option by navigating to its grandparent div
                    WebElement option = getElement(locator).findElement(By.xpath(".//*[text()='" + text + "']/parent::span/parent::div"));
                    scrollToView(option);
                    sleep(1000);
                    option.click();
                } catch (Exception finalEx) {
                    throw new RuntimeException("Unable to find and select dropdown option: " + text, finalEx);
                }
            }
        }
    }


    // Todo: it didn't work in test
    protected void selectDropdownByValue(By locator, String value) {
        Select dropdown = new Select(waitForVisibility(locator));
        dropdown.selectByValue(value);
        logAction("Selected dropdown option by value '" + value + "' for element: " + locator);
    }

    protected void selectDropdownByIndex(By locator, int index) {
        Select dropdown = new Select(waitForVisibility(locator));
        dropdown.selectByIndex(index);
        logAction("Selected dropdown option by index '" + index + "' for element: " + locator);
    }

    protected String getSelectedDropdownValue(By locator) {
        Select dropdown = new Select(waitForVisibility(locator));
        String selectedOption = dropdown.getFirstSelectedOption().getText();
        logAction("Retrieved selected dropdown value: " + selectedOption);
        return selectedOption;
    }

    public void scrollToElement(By locator) {
//        WebElement element = driver.findElement(locator); // Locate the element
        WebElement element = getElement(locator); // Locate the element
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver; // Cast the driver to JavascriptExecutor
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element); // Scroll to the element
    }
    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    protected void scrollUp() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -window.innerHeight);");
        logAction("Scrolled up by one screen height.");
    }

    protected void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, window.innerHeight);");
        logAction("Scrolled down by one screen height.");
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        logAction("Scrolled to the top of the page.");
    }

    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        logAction("Scrolled to the bottom of the page.");
    }

    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        logAction("Accepted alert dialog.");
    }

    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
        logAction("Dismissed alert dialog.");
    }

    protected String getAlertText() {
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();
        logAction("Retrieved alert text: " + alertText);
        return alertText;
    }
    protected int getLastPageNumber() {
        try {
            // Locate the pagination section
            By paginationLocator = By.xpath("//*[@aria-label='pagination']");
            WebElement paginationElement = driver.findElement(paginationLocator);

            // Find the last clickable page number dynamically
            WebElement lastPageNumberElement = paginationElement.findElement(By.xpath(".//*[contains(@class, 'cursor-pointer')][last()]"));

            // Extract and return the last page number as an integer
            String lastPageNumberText = lastPageNumberElement.getText();
            return Integer.parseInt(lastPageNumberText.trim());
        } catch (NoSuchElementException e) {
            logAction("Last page number not found in pagination.");
            return 0; // Return 0 if the last page number is not found
        } catch (NumberFormatException e) {
            logAction("Failed to parse the last page number.");
            return -1; // Return -1 if the page number is invalid
        }
    }

    public boolean LastPageClick(){
        WebElement LastPage = waitForVisibility(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'lucide-chevrons-right')]/ancestor::li[1]"));
        LastPage.click();
        return true;
    }
    protected void NextPageClick(){
        WebElement NextPage = waitForVisibility(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'lucide-chevron-right')]/ancestor::li[1]"));
        NextPage.click();
    }
    protected void PreviousPageClick(){
        WebElement PreviousPage = waitForVisibility(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'lucide-chevron-left')]/ancestor::li[1]"));
        PreviousPage.click();
    }
    protected void FirstPageClick(){
        WebElement FirstPage = waitForVisibility(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'lucide-chevrons-left')]/ancestor::li[1]"));
        FirstPage.click();
    }
    protected List<Integer> PageNumberShowInPagination(){
        List<WebElement> pageElements = driver.findElements(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'cursor-pointer')]"));
        List<Integer> pageNumbers = new ArrayList<>();
        for (WebElement pageElement : pageElements) {
            String pageText = pageElement.getText().trim();
            if (!pageText.isEmpty()) {
                try {
                    pageNumbers.add(Integer.parseInt(pageText));
                } catch (NumberFormatException e) {
                    // Ignore non-numeric elements (like dots for '...').
                }
            }
        }
        return pageNumbers;
    }
    protected void randomPageClick(){
        List<Integer>NumberOfPage = PageNumberShowInPagination();
        int randomPageNumber = generateRandomInt(NumberOfPage.size());
        WebElement randomPage = waitForVisibility(By.xpath("//*[@aria-label='pagination']//*[contains(@class, 'cursor-pointer') and text()='"+NumberOfPage.get(randomPageNumber)+"']"));
        randomPage.click();
    }
    // Todo: check/uncheck
    protected void checkCheckBox(By locator) {
        WebElement checkbox = driver.findElement(locator);

        // Check if the checkbox is already selected
        if (!checkbox.isSelected()) {
            checkbox.click(); // Click the checkbox if it's not already selected
        }
    }

    // Todo: clickCustomAlertButton


    protected List<String> extractTableHeaderByColumns(By locator) {
        List<String> tableHeaderList = new ArrayList<>();
        List<WebElement> headers = getElements(locator);
        for(WebElement header : headers){
//            if(!header.getText().isEmpty())tableHeaderList.add(header.getText());
            tableHeaderList.add(header.getText());
        }
        return tableHeaderList;
    }

    // Todo: Table > handle the empty or different data
    /**
     * Extract the headers from the table by the entire table locator
     * @param tableLocator is a By object
     * @return
     */
    protected List<String> extractTableHeadersByTable(By tableLocator) {
        List<String> headers = new ArrayList<>();
        WebElement table = getElement(tableLocator);
//        WebElement table = driver.findElement(By.xpath(tableLocator));

        // Find header row inside the <thead>, fallback to the first <tr> if <thead> is absent
        List<WebElement> headerElements = table.findElements(By.xpath(".//thead/tr/th"));
        if (headerElements.isEmpty()) {
            headerElements = table.findElements(By.xpath(".//tr[1]/th")); // Fall back to first row with <th>
        }
        if (headerElements.isEmpty()) {
            headerElements = table.findElements(By.xpath(".//tr[1]/td")); // Fall back to first row with <td>
        }

        for (WebElement header : headerElements) {
            headers.add(header.getText().trim());
        }
        return headers;
    }

    // Todo: Table > handle the empty or different data
    /**
     * Extract the table body data by table locator
     * @param tableLocator a By object
     * @return list of list
     */
    public List<List<String>> extractTableBodyByTable(By tableLocator) {
        List<List<String>> body = new ArrayList<>();
        WebElement table = getElement(tableLocator);
//        WebElement table = driver.findElement(By.xpath(tableLocator));

        // Find all rows inside <tbody>, fallback to rows after the first one in <table> if <tbody> is absent
        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
        if (rows.isEmpty()) {
            rows = table.findElements(By.xpath(".//tr[position() > 1]")); // Exclude header row
        }

        for (WebElement row : rows) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> cells = row.findElements(By.xpath(".//td"));
            for (WebElement cell : cells) {
                rowData.add(cell.getText().trim());
            }
            body.add(rowData);
        }
        return body;
    }

    /**
     * Extract the entire table data including headers
     * @param tableLocator a By object
     * @return entire table data in list of list data
     */
    public List<List<String>> extractFullTableData(By tableLocator) {
        List<List<String>> entireTable = new ArrayList<>();
        List<String> headers = extractTableHeadersByTable(tableLocator);
        List<List<String>> body = extractTableBodyByTable(tableLocator);

        // Add headers as the first row in the table
        entireTable.add(headers);
        entireTable.addAll(body);

        return entireTable;
    }

    // Todo: Table > Validate table column size
    public boolean validateTableColumnSize(By tableLocator, int expectedColumnSize) {
        WebElement table = getElement(tableLocator);
        List<WebElement> headerElements = table.findElements(By.xpath(".//thead/tr/th"));

        // Fallback to the first row if <thead> is not present
        if (headerElements.isEmpty()) {
            headerElements = table.findElements(By.xpath(".//tr[1]/td"));
        }
        if (headerElements.size() != expectedColumnSize) {
            System.out.println("Actual size: " + headerElements.size());
            System.out.println("Expected size: " + expectedColumnSize);
        }
        return headerElements.size() == expectedColumnSize;
    }

    // Todo: Table > Validate table column header
    public boolean validateTableColumnHeader(By tableLocator, List<String> expectedHeaders) {
        WebElement table = getElement(tableLocator);
        List<WebElement> headerElements = table.findElements(By.xpath(".//thead/tr/th"));

        // Fallback to the first row if <thead> is not present
        if (headerElements.isEmpty()) {
            headerElements = table.findElements(By.xpath(".//tr[1]/td"));
        }

        List<String> actualHeaders = new ArrayList<>();
        for (WebElement header : headerElements) {
            actualHeaders.add(header.getText().trim());
        }

        return actualHeaders.equals(expectedHeaders);
    }

    // Todo: Table > Validate table row size
    public boolean validateTableRowSize(By tableLocator, int expectedRowSize) {
        WebElement table = getElement(tableLocator);
        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));

        // Fallback to all rows excluding the first one if <tbody> is not present
        if (rows.isEmpty()) {
            rows = table.findElements(By.xpath(".//tr[position() > 1]"));
        }

        return rows.size() == expectedRowSize;
    }

    // Todo: Table > Validate table by ignoring specific column
    public boolean validateTableIgnoringColumn(By tableLocator, int columnToIgnore, List<List<String>> expectedData) {
        List<List<String>> actualData = new ArrayList<>();

        WebElement table = getElement(tableLocator);
        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));

        if (rows.isEmpty()) {
            rows = table.findElements(By.xpath(".//tr[position() > 1]"));
        }

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(".//td"));
            List<String> rowData = new ArrayList<>();

            for (int i = 0; i < cells.size(); i++) {
                if (i != columnToIgnore - 1) { // Ignore specified column (1-based index)
                    rowData.add(cells.get(i).getText().trim());
                }
            }
            actualData.add(rowData);
        }

        return actualData.equals(expectedData);
    }
    public int generateRandomInt(int Range){
        Random random = new Random();
        return random.nextInt(Range);
    }
    public String generateRandomString(int length, String StartsWith) {
        // Define the characters to use in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        randomString.append(StartsWith);
        for (int i = 0; i < length; i++) {
            // Pick a random character and append it to the string
            int index =  generateRandomInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
    public static List<List<String>> convertResultSetToList(ResultSet rs) {
        List<List<String>> table = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column headers as the first row
            List<String> header = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                header.add(metaData.getColumnLabel(i));
            }
            table.add(header);

            // Add each row's data
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i) != null ? rs.getString(i) : ""); // Handle NULL values
                }
                table.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }
    public boolean checkTable(List<List<String>> TableA, List<List<String>> TableB){
        if(TableA.size() != TableB.size()){
            System.out.println("Number of Row is not matched");
            return false;
        }
        int rowNumber = TableA.size();
        for(int i = 0; i < rowNumber; i++){
            List<String> rowOfA = TableA.get(i);
            List<String> rowOfB = TableB.get(i);
            if(!rowOfA.equals(rowOfB)){
                System.out.println(rowOfA);
                System.out.println(rowOfB);
                System.out.println("Not Match at:: Row Number: " + i);
                return false;
            }
        }
        System.out.println("matched");
        return true;
    }
    public By xpath(String xPath){
        return By.xpath(xPath);
    }
    public static List<List<String>> takeDataFromRange(List<List<String>> tableData, int First, int Last){
        List<List<String>> table = new ArrayList<>();
        table.add(tableData.get(0));
        for(int i = First; i <= Last; i++)table.add(tableData.get(i));
        return table;
    }
    public boolean checkColumn(List<List<String>> TableA, List<List<String>> TableB, String ColumnName){
        ColumnName = ColumnName.replace("'", "");
        int columnIndex = -1;
        for(int i = 0; i < TableA.get(0).size(); i++){
            if(ColumnName.equals(TableA.get(0).get(i))){
                columnIndex = i;
                break;
            }
        }
        List<String> columnDataA = new ArrayList<>();
        List<String> columnDataB = new ArrayList<>();
        for (List<String> row : TableA) {
            if (columnIndex < row.size()) { // Check if columnIndex exists in the current row
                columnDataA.add(row.get(columnIndex)); // Get the value from the specific column
            }
        }
        for (List<String> row : TableB) {
            if (columnIndex < row.size()) { // Check if columnIndex exists in the current row
                columnDataB.add(row.get(columnIndex)); // Get the value from the specific column
            }
        }
        System.out.println(columnDataA);
        System.out.println(columnDataB);
        boolean status = columnDataA.equals(columnDataB);
        System.out.println(status);
        if(!status){
            if(columnDataA.size() != columnDataB.size()) System.out.println("size is not same");
            else{
                for(int i = 0; i < columnDataA.size(); i++){
                    if(!columnDataA.get(i).equals(columnDataB.get(i))){
                        System.out.println(columnDataA.get(i));
                        System.out.println(columnDataB.get(i));
                    }
                }
            }
        }
        return status;
    }
    public JSONObject getJSONDataByMethodName(String methodName) throws IOException {
        JSONObject jsonObject = new JSONObject(Files.readString(Path.of(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                File.separator + "resources" + File.separator + "testdata" + File.separator + "data.json")));
        //JSONObject jsonObject = new JSONObject(Files.readString(Paths.get("C:\\Users\\amuhaimin\\Desktop\\azure_repo\\TEST-test-automation\\src\\test\\resources\\testdata\\data.json")));
        return jsonObject.getJSONObject(methodName);
    }
}

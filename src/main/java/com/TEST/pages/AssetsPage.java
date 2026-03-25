package com.TEST.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AssetsPage extends BasePage {

    private final By newAssetButton = By.xpath("//*[text()='New Asset']/ancestor::button[position() = 1]");
    private final By bulkActionsButton = By.xpath("//*[@data-testid='bulk-group-button']");
    private final By downloadButton = By.xpath("//*[@data-testid='upload-csv-button']");
    private final By cardTitleLocator = By.xpath("//*[@class='text-base font-light text-doglapan-blue-50']");
    private final By assetNameHeaderLocator = By.xpath("//label[text()='Asset Name']//span[text()='*']");
    private final By organizationHeaderLocator = By.xpath("//label[text()='Organization']//span[text()='*']");
    private final By createButtonLocator = By.xpath("//*[text()='Create']");
    private final By cancelButtonLocator = By.xpath("//*[text()='Cancel']");
    private final By dropdownLocator = By.xpath("//button[@role='combobox']");
    private final By popupLocator = By.xpath("//form//*[@class='text-base font-light text-doglapan-blue-50']");
    private final By dropdownSelectLocator = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By Title = By.xpath("//*[@data-testid='asset-list-header-title']");
    private final By bulkGroupDropdownContent = By.xpath("//*[@data-testid='bulk-group-dropdown-content']/*[@role='menuitem']");
    private final By multipleDeleteButton = By.xpath("//*[@data-testid='table-bulk-action-button']");
    private final By DeleteSelectedButton = By.xpath("//div[@data-testid='table-bulk-action-dropdown-content']//div[contains(text(),'Delete Selected')]");
    private final By tableConfigIcon = By.xpath("//*[@class='flex items-center justify-center data-[state=open]:ring-2 data-[state=open]:ring-doglapan-blue-50']");
    private final By filterDropDownButton = By.xpath("//button/span[text()='Filters']");
    private final By columnHeaders = By.xpath("//div//th");
    private final By columnHeadersSearchIcon = By.xpath("//div//th//*[@class='size-[18px] cursor-pointer']");
    private final By Datagrid = By.xpath("//tbody//tr");
    private final By bindIconOnActions = By.xpath("//*[contains(@class, 'lucide lucide-link')]");
    private final By unbindIconOnActions = By.xpath("//*[contains(@class, 'lucide lucide-unlink')]");
    private final By editIconOnActions = By.xpath("//*[contains(@class, 'lucide lucide-pen-line')]");
    private final By deleteIconOnActions = By.xpath("//*[contains(@class, 'lucide lucide-trash2')]");
    private final By expandIconOnActions = By.xpath("//*[contains(@class, 'lucide lucide-expand')]");
    private final By assetNameInput = By.xpath("//*[@placeholder='Set a Name']");
    private final By organizationDropdown = By.xpath("//*[@role='dialog']//div//*[@role='combobox']");
    private final By organizationDropdownOptions = By.xpath("//div[@role='option']");
    private final By assetPageButton = By.xpath("//*[text()='Assets']/ancestor::button[1]");
    private final By leftAlignImage = By.xpath("//div[contains(@class, 'flex') and contains(@class, 'justify-start')]//img[@alt='doglapan Product Image']");
    private final By rightAlignAssetDetails = By.xpath("//div[contains(@class, 'flex') and contains(@class, 'justify-start') and contains(@class, 'items-start')]/div");
    private final By general_info_active_status = By.xpath("//button[@aria-selected='true' and @data-state='active']//span[text()='General Info']");
    private final By environmental_condition_inactive_status = By.xpath("//button[@aria-selected='false' and @data-state='inactive']//span[text()='Environmental Conditions']");
    private final By location_inactive_status = By.xpath("//button[@aria-selected='false' and @data-state='inactive']//span[text()='Location']");
    private final By bound_data_loggers_inactive_status = By.xpath("//button[@aria-selected='false' and @data-state='inactive']//span[text()='Bound Data Logger(s)']");
    private final By genralInfoDataOnAssetDetails = By.xpath("//*[contains(@id, 'content-general-info')]//*[contains(@class, 'justify-start') and contains(@class, 'rounded')]");
    private final By assetnameisrequired = By.xpath("//*[text()='Asset Name is required']");
    private final By organizationisrequried = By.xpath("//*[text()='Organization is required']");
    private final By assetNameCharacter3isrequired = By.xpath("//*[text()='Asset Name must be at least 3 characters']");
    private final By assetNameBelowCharacter50isrequried = By.xpath("//*[text()='Asset Name must be within 50 characters']");
    private final By alreadyExist = By.xpath("//*[text()='Asset Creation Failed. Already exists']");
    private final By AcceptAssetDismiss = By.xpath("//div[.//div[text()='Asset Deleted Successfully']]//button[text()='Dismiss']");
    private final By AcceptAsset = By.xpath("//*[text()='Asset Created Successfully']");
    private final By bound_data_loggers_active_status = By.xpath("//button[@aria-selected='true' and @data-state='active']//span[text()='Bound Data Logger(s)']");
    private final By currentBoundHeaderLeft = By.xpath("//div[contains(@id, content-bound-data-logger)]//div[1]/section/div[1]/h2");
    private final By availableDataLoggers = By.xpath("//div[contains(@id, content-bound-data-logger)]//div[3]/section");
    private final By middleSectionButtonIcon = By.xpath("//*[contains(@id, 'bound-data-logger')]/div[@class='pt-5']//div[2]/button[contains(@class, 'justify-center')]");
    private final By currentBound = By.xpath("//div[contains(@id, content-bound-data-logger)]//div[1]/section");
    private final By checkPagination = By.xpath("//*[@aria-label='pagination']");
    private final By elementsOfColumnConfig = By.xpath("//*[@data-testid='configure-columns-dropdown-content']//*[contains(@class, 'cursor-pointer items-center')]");
    private final By clearFiltersButton = By.xpath("//*[text()='Clear Filters']");
    private final By filterCrossButtons = By.xpath("//*[contains(@class, 'items-center justify-between')]//*[@class='cursor-pointer']");
    private final By nextMonthCalenderButton = By.xpath("//button[@name='next-month']");
    private final By previousMonthCalenderButton = By.xpath("//button[@name='previous-month']");
    private final By monthYearInCalender = By.xpath("//*[@aria-live='polite' and @role = 'presentation']");
    private final By optionOfFilters = By.xpath("//*[text()='Filters']/ancestor::section/ancestor::div[@data-state='open']//label");
    private final By clickApplyFilter = By.xpath("//*[text()='Apply']");
    private final By clickFirstRow = By.xpath("//tr[1]//td[1]//button");
    private final By organizationButton = By.xpath("//label[text()='Organization']/following-sibling::button");
    //private final By optionOfFIltersAssetTypesButton = By.xpath("//*[text()='Filters']/ancestor::section//ancestor::div[@data-state='open']//button[contains(@id, 'assetTypes')]");
    List<String> COMMON_HEADERS = Arrays.asList("Name", "Actions", "Asset Type", "Bound Data Logger(s)", "Monitored Status",
            "Geolocation", "On Shipment", "Organization", "Created At", "Created By", "Last Modified At", "Last Modified By");
    Set<String> FIELDS_WITH_SEARCH_ICON = new HashSet<>(Arrays.asList("Name", "Asset Type", "Bound Data Logger(s)",
            "Monitored Status", "Geolocation", "Organization", "Created By", "Last Modified By"));
    List<String> EXPECTED_LABELS = Arrays.asList("Asset Name", "Asset Type", "Monitored Status", "Last Location", "Current Shipment",
            "Created At", "Created By", "Last Modified At", "Last Modified By");
    List<String> ASSET_TYPES = Arrays.asList("doglapan RKN", "doglapan RAP", "doglapan APS-S", "doglapan APS-D", "Silverpod® MAX RE",
            "Silverpod® MAX", "Silverpod® PRO", "Softbox® VIP", "Softbox® MAX", "Softbox® PUR", "Softbox® PRO", "Softbox® ONE",
            "CGT Cryo M4", "CGT Cryo M10", "CGT Ultra D10", "CGT Hand Carry", "Courier MAX", "Courier PRO", "Silverskin");
    Set<String> ITEMS = new HashSet<>(Arrays.asList("doglapan RKN", "doglapan RAP", "doglapan APS-S", "doglapan APS-D", "Silverpod® MAX RE", "Silverpod® MAX",
            "Silverpod® PRO", "Softbox® VIP", "Softbox® MAX", "Softbox® PUR", "Softbox® PRO", "Softbox® ONE", "CGT Cryo M4", "CGT Cryo M10",
            "CGT Ultra D10", "CGT Hand Carry", "Courier MAX", "Courier PRO", "Silverskin", "Created At", "Last Modified At", "Monitored", "Unmonitored", "Out of Coverage"));
    List<String> ORGANIZATION_TYPES = Arrays.asList("doglapan Training - Air Cargo", "TEST - Parent", "TEST - Child");
    public AssetsPage(WebDriver driver) {
        super(driver);
    }
    public String selectedAssetType(){
        return ASSET_TYPES.get(generateRandomInt(ASSET_TYPES.size()));
    }
    public void clickNewAssetDropDownButton(){
        waitForVisibility(dropdownLocator);
        click(dropdownLocator);
    }
    public boolean selectDropdownOptionForAsset(String text) {
        try {
            clickNewAssetDropDownButton(); //clickNewAsset();
            logAction("Dropdown button clicked successfully.");
            waitForVisibility(dropdownSelectLocator);
            selectDropdownByText(dropdownSelectLocator, text);
            logAction("Option with text '" + text + "' selected successfully.");
            return true;
        } catch (Exception e) {
            logAction("Failed to select option '" + text + "' in dropdown. Exception: " + e.getMessage());
            return false;
        }
    }
    public String getCardTitleText() {
        return getElement(cardTitleLocator).getText(); // Using getElement
    }
    public boolean isCardTitleCorrect(String expectedAssetType) {
        String actualTitle = getCardTitleText();
        logAction("Verifying card title: " + actualTitle);
        return actualTitle.contains("New " + expectedAssetType);
    }
    public boolean isAssetNameHeaderDisplayed() {
        return isDisplayed(assetNameHeaderLocator);
    }
    public boolean isOrganizationHeaderDisplayed() {
        return isDisplayed(organizationHeaderLocator);
    }
    public boolean isCreateButtonDisplayed() {
        return isDisplayed(createButtonLocator);
    }
    public boolean isCancelButtonDisplayed() {
        return isDisplayed(cancelButtonLocator);
    }
    public boolean clickCancelButton() {
        try {
            click(cancelButtonLocator);
            logAction("Cancel button clicked successfully.");
            return true;
        } catch (Exception e) {
            logAction("Failed to click cancel button. Exception: " + e.getMessage());
            return false;
        }
    }
    public boolean isPopupDisplayed() {
        return isDisplayed(popupLocator);
    }
    public boolean checkPageTitle() {
        String title = getElement(Title).getText();
        if (title.equals("Assets")) {
            logAction("Assets title Matched");
            return true;
        }
        return false;
    }
    public boolean checkNewAssetButton(){
        if (isDisplayed(newAssetButton)) {
            logAction("New Asset Displayed");
            return true;
        }
        return false;
    }
    public boolean checkBulkActions(){
        if (isDisplayed(bulkActionsButton)) {
            logAction("Bulk Actions icon Displayed");
            return true;
        }
        return false;
    }
    public boolean checkBulkOptions() {
        click(bulkActionsButton);
        waitForVisibility(bulkGroupDropdownContent);
        List<WebElement> bulkList = getElements(bulkGroupDropdownContent);
        boolean bulkUpload = false, bulkBind = false, bulkUnbind = false;
        for (WebElement bulk : bulkList) {
            String str = bulk.getText().trim();
            if (str.equalsIgnoreCase("Bulk Upload")) {
                bulkUpload = true;
            } else if (str.equalsIgnoreCase("Bulk Bind")) {
                bulkBind = true;
            } else if (str.equalsIgnoreCase("Bulk Unbind")) {
                bulkUnbind = true;
            }
        }
        if (bulkUpload && bulkBind && bulkUnbind) {
            logAction("Bulk Upload, Bulk Bind and Bulk Unbind are Displayed");
            return true;
        }
        if (!bulkUpload)logAction("Bulk Upload is not Displayed");
        if (!bulkBind)logAction("Bulk Bind is not Displayed");
        if (!bulkUnbind)logAction("Bulk Unbind is not Displayed");
        return false;
    }
    public boolean checkDownloadIcon(){
        if (isDisplayed(downloadButton)) {
            logAction("Download icon Displayed");
            return true;
        }
        return false;
    }
    public boolean checkMultipleDeleteButton(){
        if (isDisplayed(multipleDeleteButton)) {
            logAction("Multiple Delete Button is Displayed");
            return true;
        }
        return false;
    }
    public boolean checkDeleteSelectedFromMultipleDeleteClick() {
        refreshPage();
        click(clickFirstRow);
        click(multipleDeleteButton);
        waitForVisibility(DeleteSelectedButton);  // Assuming you have a method to wait for elements to appear
        if (isDisplayed(DeleteSelectedButton)) {
            logAction("Delete Selected Button is Displayed");
            return true;
        }
        logAction("Delete Selected Button is Not Displayed");
        return false;
    }
    public boolean tableConfigIconButton(){
        if (isDisplayed(tableConfigIcon)) {
            logAction("Table Config Icon Button is Displayed");
            return true;
        }
        return false;
    }
    public boolean filterButton(){
        if (isDisplayed(filterDropDownButton)) {
            logAction("Filter Dropdown Button is Displayed");
            return true;
        }
        return false;
    }
    public List<String> ListOfGridHeader(){
        List<String> Existedheaders = new ArrayList<>();
        List<WebElement> headers = getElements(columnHeaders);
        for(WebElement header : headers){
            if(!header.getText().isEmpty())Existedheaders.add(header.getText());
        }
        return Existedheaders;
    }
    public boolean checkGridHeader() {
        List<String> existingHeaders = ListOfGridHeader();
        Set<String> expectedHeaders = new HashSet<>(COMMON_HEADERS);
        Set<String> existingHeader = new HashSet<>(existingHeaders);
        if (expectedHeaders.containsAll(existingHeader)) {
            logAction("All headers are present.");
            return true;
        } else {
            Set<String> missingHeaders = new HashSet<>(expectedHeaders);
            missingHeaders.removeAll(existingHeader);
            logAction("The following headers are missing: " + missingHeaders);
            return false;
        }
    }
    public boolean checkSearchIcon() {
        List<String> existingSearchIcon = new ArrayList<>();
        List<WebElement> headers = getElements(columnHeaders);
        for (WebElement header : headers) {
            List<WebElement> searchIcons = header.findElements(columnHeadersSearchIcon);
            if (!searchIcons.isEmpty()) {
                existingSearchIcon.add(header.getText());
            }
        }
        Set<String> existingSearchIconSet = new HashSet<>(existingSearchIcon);
        if (existingSearchIconSet.containsAll(FIELDS_WITH_SEARCH_ICON)) {
            logAction("All required fields have a search icon.");
            return true;
        } else {
            Set<String> missingIcons = new HashSet<>(FIELDS_WITH_SEARCH_ICON);
            missingIcons.removeAll(existingSearchIconSet);
            logAction("The following fields are missing search icons: " + missingIcons);
            return false;
        }
    }
    public boolean checkGridHeaderSequenceVerify() {
        List<String> existingHeaders = new ArrayList<>();
        List<WebElement> headers = getElements(columnHeaders);
        for (WebElement header : headers) {
            if (!header.getText().isEmpty()) {
                existingHeaders.add(header.getText());
            }
        }
        if (existingHeaders.equals(COMMON_HEADERS)) {
            logAction("Headers are in the correct sequence.");
            return true;
        } else {
            logAction("Headers are not in the correct sequence. Found: " + existingHeaders);
            return false;
        }
    }
    public boolean checkActionsIcons() {
        List<WebElement> actionsRows = getElements(Datagrid);
        for (int i = 0; i < actionsRows.size(); i++) {
            WebElement actionRow = actionsRows.get(i);
            if (!actionRow.findElement((bindIconOnActions)).isDisplayed()) {
                logAction("Bind icon is missing in row " + (i + 1));
                return false;
            } else if(!actionRow.findElement((unbindIconOnActions)).isDisplayed()) {
                logAction("Unbind icon is missing in row " + (i + 1));
                return false;
            } else if(!actionRow.findElement((editIconOnActions)).isDisplayed()) {
                logAction("Edit icon is missing in row " + (i + 1));
                return false;
            } else if (!actionRow.findElement(deleteIconOnActions).isDisplayed()) {
                logAction("Delete icon is missing in row " + (i + 1));
                return false;
            }else if (!isDisplayed((expandIconOnActions))) {
                logAction("Expand icon is missing in row " + (i + 1));
                return false;
            }
        }
        logAction("All icons are present in all rows.");
        return true;
    }
    public String selectOrganizationOption(String organizationoption) {
        click(organizationDropdown);
        List<WebElement> dropdownOptions = getElements(organizationDropdownOptions);
        String selectedOrganizationOption = organizationoption;
        for(WebElement option : dropdownOptions){
            if(option.getText().equals(selectedOrganizationOption)){
                option.click();
                return  selectedOrganizationOption;
            }
        }
        return "";
    }
    public String GetRandomName(){
        List<WebElement> dataGrids = getElements(Datagrid);
        int numberOfName = dataGrids.size();
        int indexOfName = generateRandomInt(numberOfName);
        return getText(By.xpath("//tbody/tr[" + (indexOfName + 1) + "]/td[2]"));
    }
    public boolean checkAssetDetailsPageTitle(String Name){
        String str = getText(By.xpath("//div/main/div[1]"));
        if(str.startsWith("Assets") && str.endsWith(Name)){
            logAction("Assets " + Name + " is exist");
            return true;
        }
        else return false;
    }
    public boolean checkGerneralInfoActive(){
        boolean general_info_active = isDisplayed(general_info_active_status);
        boolean environmental_Conditions_inactive = isDisplayed(environmental_condition_inactive_status);
        boolean location_inactive = isDisplayed(location_inactive_status);
        boolean bound_data_loggers_inactive = isDisplayed(bound_data_loggers_inactive_status);
        if(general_info_active && environmental_Conditions_inactive && location_inactive && bound_data_loggers_inactive){
            logAction("General Info Active");
            return true;
        }
        return false;
    }
    public boolean checkLeftAlignImage(){
        boolean status = isDisplayed(leftAlignImage);
        if(status){
            logAction("Left Align image is exist");
            return true;
        }
        logAction("Left Align image is not exist");
        return false;
    }
    public boolean checkRightAlignlabels(){
        List<WebElement> labels = getElements(rightAlignAssetDetails);
        List<String> existinglabels = new ArrayList<>();

        for (WebElement label : labels) {
            if (!label.getText().isEmpty()) {
                existinglabels.add(label.getText());
            }
        }
        if (existinglabels.equals(EXPECTED_LABELS)) {
            logAction("Labels are in the correct sequence, row by row.");
            return true;
        } else {
            logAction("Labels are not in the correct sequence. Found: " + existinglabels);
            return false;
        }
    }
    public void backToAssetPage(){
        click(assetPageButton);
        waitForPageToLoadCompletely();
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
    public WebElement getRowsByName(String name){
        List<WebElement> rows = getElements(Datagrid);
        int nameRowIndex = getColumnIndex("Name");
        for(WebElement row : rows){
            WebElement element = row.findElement(By.xpath(".//td[" + (nameRowIndex + 1)+ "]"));
            boolean x = element.isDisplayed();
            if(x && element.getText().equals(name))return row;
        }
        return null;
    }

    public String getCellDataByName(String name, String ColumnName) {
        WebElement row = getRowsByName(name); // Find the row using name
        int columnIndex = getColumnIndex(ColumnName);
        return row.findElement(By.xpath(".//td["+ (columnIndex + 1) + "]")).getText();
    }
    public String getCellDataByNameForMonitoredStatus(String name){
        List<WebElement> rows = getElements(Datagrid);
        int nameRowIndex = getColumnIndex("Name");
        int MonitoredStatusIndex = getColumnIndex("Monitored Status");
        for(int i = 0; i < rows.size(); i++){
            WebElement row = rows.get(i);
            WebElement element = row.findElement(By.xpath(".//td[" + (nameRowIndex + 1)+ "]"));
            boolean x = element.isDisplayed();
            if(x && element.getText().equals(name)){
                hoverOverElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[" + (MonitoredStatusIndex + 1) + "]//div[@data-state]"));
                String text = getText(By.xpath("//*[@data-radix-popper-content-wrapper]"));
                text = text.split("\n")[0];
                return  text;
            }
        }
        return "";
    }
    public int getColumnIndexFromAssetDetailsPage_GI(String name){
        if(!checkGerneralInfoActive())return -1;
        List<WebElement> rows = getElements(genralInfoDataOnAssetDetails);
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).findElement(By.xpath(".//div")).getText().contains(name))return i;
        }
        return -1;
    }
    public String getCellDataByNameFromAssetDetailsPage_GI(String CellName){
        int dataIndex = getColumnIndexFromAssetDetailsPage_GI(CellName);
        if(dataIndex < 0)return "";
        return getElements(genralInfoDataOnAssetDetails).get(dataIndex).findElement(By.xpath(".//p")).getText();
    }
    public boolean checkStringType(Object obj) {
        return obj instanceof String;
    }
    public boolean checkEmptyStringType(Object obj) {
        if(obj instanceof String){
            String str = obj.toString();
            if(str.isEmpty()){
                logAction("String is empty");
                return true;
            }
        }
        return false;
    }
    public boolean checkDateTimeType(String str){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dateTimeFormat.setLenient(false);
        try {
            dateTimeFormat.parse(str); return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public boolean checkString(String A, String B){
        return A.equals(B);
    }
    public void ClickExpandOnName(String Name) {
        int columnIndex = getColumnIndex("Actions");
        int rowIndex = getRowIndex(Name); // Find the row using name
        click(By.xpath("//tbody/tr["+ (rowIndex + 1)+"]/td["+ (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-expand')]"));
    }

    public List<WebElement> obtainPageData() {
        List<WebElement> rows = getElements(Datagrid);
        return rows;
    }
    public boolean checkFirstPage() {
        List<WebElement> rows = obtainPageData();
        List<String> assetHeaders = ListOfGridHeader();
        List<String> dropdownOptionsList = new ArrayList<>();
        boolean drowdownDone = false, selecting1 = false, selecting2 = false;
        for (int i = 0; i < rows.size(); i++) {
            for (String header : assetHeaders) {
                System.out.println(header);
                int columnIndex = getColumnIndex(header); // Get column index dynamically
                String text = rows.get(i).findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText().trim();
                System.out.println(text);
                if (header.equals("Created At") || header.equals("Last Modified At")) {
                    if (!checkDateTimeType(text)) {
                        logAction("Invalid date-time format for: " + header);
                        return false;
                    }
                }
                if (header.equals("Name")) {
                    if (!checkStringType(text)) {
                        logAction("Invalid string format for Name");
                        return false;
                    }
                }
                if (header.equals("Asset Type")) {
                    if (!ASSET_TYPES.contains(text)) {
                        logAction("Invalid asset type: '" + text + "'");
                        return false;
                    }
                }
                if (header.contains("Bound Data Logger(s)")) {
                    String str = rows.get(i).findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText().trim();
                    boolean boundDataLoggers = false;
                    if (str.contains("N/A")) boundDataLoggers = true;
                    if (!boundDataLoggers) {
                        if(isDisplayed(xpath("//tbody//tr["+(i+1)+"]/td["+(columnIndex+1)+"]//a"))) {
                            WebElement hrefElement = rows.get(i).findElement(By.xpath(".//td[" + (columnIndex + 1) + "]//a"));
                            String href = hrefElement.getAttribute("href");
                            if (href != null && href.contains("/data-loggers")) boundDataLoggers = true;
                        }
                    }
                    if (!boundDataLoggers) {
                        hoverOverElement(xpath("//tbody//tr["+(i+1)+"]/td["+(columnIndex+1)+"]/div"));
                        List<WebElement> elements = getElements(xpath("//div[@data-radix-popper-content-wrapper and contains(@style, 'position: fixed')]//a"));
                        int cnt = 0;
                        for(WebElement element : elements){
                            String href = element.getAttribute("href");
                            if (!(href != null && href.contains("/data-loggers"))){
                                return false;
                            }
                            else cnt++;
                        }
                        if(cnt == elements.size())boundDataLoggers = true;
                    }
                    if(!boundDataLoggers)return false;
                }
                if (header.equals("Geolocation")) {
                    if (!(text.contains("Out of Coverage") || (text.contains("Lat") && text.contains("Lon")))) {
                        if(!text.isEmpty())continue;
                        logAction("Geolocation is missing or invalid for Row: " + i);
                        return false;
                    }
                }
                if (header.equals("On Shipment")) {
                    if(!(text.equals("Yes") || text.equals("No")))return false;
                }
                if(header.equals("Organization")){
                    if(!drowdownDone){
                        String selectedAsset = ASSET_TYPES.get(0);
                        waitForVisibility(dropdownLocator);
                        click(dropdownLocator);
                        logAction("Dropdown button clicked successfully.");
                        waitForVisibility(dropdownSelectLocator);
                        selectDropdownByText(dropdownSelectLocator, selectedAsset);
                        click(organizationDropdown);
                        List<WebElement> dropdownOptions = getElements(xpath("//*[@role='option']"));

                        if(!selecting2) {
                            for (int k = 0; k < dropdownOptions.size(); k++) {
                                if (!selecting1) selecting1 = true;
                                dropdownOptionsList.add(dropdownOptions.get(k).getText());
                                System.out.println(dropdownOptions.get(k).getText());
                            }
                            doubleClick(cancelButtonLocator);
                            selecting2 = true;
                        }
                        drowdownDone = true;
                    }
                    if(!dropdownOptionsList.contains(text)) {
                        System.out.println(dropdownOptionsList);
                        System.out.println(text);
                        //return false;
                    }
                }
                if(header.equals("Created By") || header.equals("Last Modified By")){
                    if(!checkStringType(text))return false;
                }
                if(header.equals("Monitored Status")){
                    hoverOverElement(By.xpath("//tbody/tr["+(i + 1)+"]/td["+(columnIndex + 1) +"]//div[@data-state]"));
                    sleep(2000);
                    text = getText(By.xpath("//*[@data-radix-popper-content-wrapper]"));
                    text = text.split("\n")[0];
                    if(!(text.equals("Monitored") || text.equals("Unmonitored"))){
                        return false;
                    }

                }
            }
        }
        return true; // All checks passed
    }
    public void clickNewAsset(){
        click (newAssetButton);
    }
    public String SelectRandomAssetType() throws InterruptedException {
        int index = generateRandomInt(ASSET_TYPES.size());
        selectDropdownByText(dropdownSelectLocator, ASSET_TYPES.get(index));
        return ASSET_TYPES.get(index);
    }
    public void clickCreateButton(){click(createButtonLocator);}
    public boolean AssetNameIsRequireMessage(){return isDisplayed(assetnameisrequired);}
    public boolean OrganizationIsRequireMessage(){return isDisplayed(organizationisrequried);}
    public boolean AssetNameCharacter3isRequireMessage() {
        waitForVisibility(assetNameInput).sendKeys(generateRandomString(2, ""));
        return isDisplayed(assetNameCharacter3isrequired);
    }
    public boolean AssetNameBelowCharacter50isRequireMessage() {
        waitForVisibility(assetNameInput).sendKeys(generateRandomString(52, ""));
        return isDisplayed(assetNameBelowCharacter50isrequried);
    }
    public String getDataFromGridUsingRowNumber(int index, String ColumnName){
        List<WebElement> data = obtainPageData();
        return data.get(index - 1).findElement(By.xpath(".//td[" + (getColumnIndex(ColumnName) + 1) + "]")).getText();
    }
    public boolean checkDuplicate() {
        String assetname = getDataFromGridUsingRowNumber(1, "Name");
        String organization = getDataFromGridUsingRowNumber(1, "Organization");
        getElement(assetNameInput).clear();
        waitForVisibility(assetNameInput).sendKeys(assetname);
        String str = selectOrganizationOption(organization);
        clickCreateButton();
        boolean result = waitForVisibility(alreadyExist).isDisplayed();
        return result;
    }
    public boolean AssetAccept(){
        return isDisplayed(AcceptAsset);
    }
    public boolean checkElementFromSpecificCell(List<WebElement> dataGrid, int row,String ColumnName, String text){
        System.out.println("boom");
        int columnIndex = getColumnIndex(ColumnName);
        System.out.println("boom");
        System.out.println(dataGrid.get(row - 1).findElement(xpath(".")));
        System.out.println("puku");
        String existingdata = dataGrid.get(row - 1).findElement(By.xpath(".//td[" + (columnIndex + 1) +"]")).getText();
        System.out.println(existingdata);
        if(ColumnName.equals("Created At") || ColumnName.equals("Last Modified At")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime dateTime1 = LocalDateTime.parse(existingdata, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(text, formatter);
            long secondsDiff = Duration.between(dateTime1, dateTime2).getSeconds();
            return secondsDiff <= 60;
        }
        System.out.println("boom");
        if(ColumnName.equals("Monitored Status")){
            hoverOverElement(By.xpath("//tbody/tr["+(row)+"]/td["+(columnIndex + 1) +"]//div[@data-state]"));
            hoverOverElement(By.xpath("//tbody/tr["+(row)+"]/td["+(columnIndex + 1) +"]//div[@data-state]"));
            hoverOverElement(By.xpath("//tbody/tr["+(row)+"]/td["+(columnIndex + 1) +"]//div[@data-state]"));
            existingdata = getText(By.xpath("//*[@data-radix-popper-content-wrapper]"));
            existingdata = existingdata.split("\n")[0];
            return existingdata.equals(text);
        }
        System.out.println("boom");
        return existingdata.equals(text);
    }
    public boolean verifyBoundDataLoggers(){
        return isDisplayed(bound_data_loggers_inactive_status);
    }
    public boolean clickBoundDataLoggers(){
        click(bound_data_loggers_inactive_status);
        return isDisplayed(bound_data_loggers_active_status);
    }
    public boolean verifyCurrentlyBound(){
        return waitForVisibility(currentBoundHeaderLeft).getText().contains("Currently Bound");
    }
    public boolean verifyAvailableDataLoggers() {
        return waitForVisibility(availableDataLoggers).findElement(By.xpath(".//div[1]/h2")).getText().contains("Available Data Loggers");
    }
    public boolean verifyMiddleSectionButtonIcon(){
        return waitForVisibility(middleSectionButtonIcon).findElements(By.tagName("svg")).size() > 0;
    }
    public int numberOfAvailableDataLoggers() {
        String text = waitForVisibility(availableDataLoggers).findElement(By.xpath(".//div[1]/h2")).getText();
        // Escape regex special characters in the prefix
        String prefixToRemove = "Available Data Loggers \\(";

        // Remove the prefix
        text = text.replaceFirst("^" + prefixToRemove, "");
        // Remove the trailing ")"
        if (text.endsWith(")")) text = text.substring(0, text.length() - 1);
        // Convert the resulting string to an integer
        return Integer.parseInt(text.trim());
    }
    public int numberOfCurrentlyBound() {
        String text = waitForVisibility(currentBoundHeaderLeft).getText();
        // Escape regex special characters in the prefix
        String prefixToRemove = "Currently Bound \\(";
        // Remove the prefix
        text = text.replaceFirst("^" + prefixToRemove, "");
        // Remove the trailing ")"
        if (text.endsWith(")")) text = text.substring(0, text.length() - 1);
        // Convert the resulting string to an integer
        return Integer.parseInt(text.trim());
    }
    public boolean verifyColumnNamesOfCurrentlyBound(){
        List<WebElement> columnNames = waitForVisibility(currentBound).findElements(By.xpath(".//table/thead//th"));
        if(columnNames.size() < 3)return false;
        for(int i = 0; i < columnNames.size(); i++){
            if(columnNames.get(i).getText().equals("Name") || columnNames.get(i).getText().equals("Component") || columnNames.get(i).getText().equals("Action"))continue;
            else return false;
        }
        return true;
    }
    public boolean haveBoundDataLoggers(String Name){
        String text = getCellDataByName(Name, "Bound Data Logger(s)");
        if(text.equals("N/A"))return false;
        return true;
    }
    public boolean observeActionColumnContains(){
        String text = waitForVisibility(currentBound).findElement(By.xpath(".//table/tbody//td[3]")).getText();
        return text.equals("Unbind");
    }
    public boolean SearchIconInAvailableDataLoggers(){
        return waitForVisibility(availableDataLoggers).findElement(By.xpath(".//div[1]//input[@placeholder='Search By Name']")).isDisplayed();
    }
    public boolean verifyColumnNamesOfdDataLoggers(){
        List<WebElement> columnNames = waitForVisibility(availableDataLoggers).findElements(By.xpath(".//table/thead//th"));
        if(columnNames.size() < 3)return false;
        boolean dataLoggerType = false, Name = false, Component = false;
        for(int i = 0; i < columnNames.size(); i++){
            if(columnNames.get(i).getText().equals("Data Logger Type"))dataLoggerType = true;
            if(columnNames.get(i).getText().equals("Name"))Name = true;
            if(columnNames.get(i).getText().equals("Component"))Component = true;
        }
        if(dataLoggerType && Name && Component)return true;
        return false;
    }

    public boolean clickedNextPage(){
        NextPageClick();
        return true;
    }
    public boolean clickedPreviousPage(){
        PreviousPageClick();
        return true;
    }
    public boolean clickedFirstPage(){
        FirstPageClick();
        return true;
    }
    public boolean clickedRandomPage(){
        randomPageClick();
        return true;
    }
    public boolean paginationExist(){
        return isDisplayed(checkPagination);
    }
    public boolean AssetGridSize(){
        return obtainPageData().size() == 20;
    }

    public void configIcon(){
        click(tableConfigIcon);
    }
    public List<WebElement> listOfElementsOfColumnConfig(){
        return getElements(elementsOfColumnConfig);
    }
    public boolean clickOnElementsOfColumnConfig(String ColumnName){
        List<WebElement> listOfElements = listOfElementsOfColumnConfig();
        for(int i = 0; i < listOfElements.size(); i++){
            if(listOfElements.get(i).getText().equals(ColumnName)){
                listOfElements.get(i).click();
                break;
            };

        }
        List<WebElement> Headers = getElements(columnHeaders);
        for(int i = 0; i < Headers.size(); i++){
            if(Headers.get(i).getText().equals(ColumnName))return true;
        }
        return false;
    }
    public void clickFilter(){
        click(filterDropDownButton);
    }
    public boolean clickClearFilters(){
        boolean display = isDisplayed(clearFiltersButton);
        if(!display)return false;
        click(clearFiltersButton);
        return true;
    }
    public boolean clickARandomCrossButtonInFilter(){
        List<WebElement> listOfCrossButton = getElements(filterCrossButtons);
        int randomIndex = generateRandomInt(listOfCrossButton.size());
        boolean display = listOfCrossButton.get(randomIndex).isDisplayed();
        if(!display)return false;
        listOfCrossButton.get(randomIndex).click();
        return true;
    }
    public boolean verifyFilterColumnHeaders(){
        for(int i = 0; i < 4; i++){
            String text = getText(By.xpath("//*[text()='Filters']/ancestor::div[@role='dialog']//div["+(i + 1)+"]/p"));
            text = text.trim();
            if(!text.equals("Asset Type") && !text.equals("Date Range") && !text.equals("Monitored Status") && !text.equals("Geolocation Status"))return false;
        }
        List<WebElement>listOfLebels = getElements(optionOfFilters);
        for(int i = 0; i < listOfLebels.size(); i++){
            String str = listOfLebels.get(i).getText();
            int Len = ITEMS.size();
            ITEMS.add(str);
            if(ITEMS.size() != Len)return false;
        }
        return true;
    }
    public void clickSort(String columnName) {
        int columnIndex = getColumnIndex(columnName);
        // XPath for the sort button
        System.out.println("//table/thead/tr/th[" + (columnIndex + 1) + "]//button");
        By sortButtonBy = By.xpath("//table/thead/tr/th[" + (columnIndex + 1) + "]//button");
        // Wait for the button to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(sortButtonBy));
        // Scroll to the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", sortButton);
        sleep(1000);
        // Click the element using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortButton);
    }
    public void selectDateInFilter(LocalDateTime local_Date_Time, String text) {

        if(text.equals("Start Date")){
            click(By.xpath("//*[text()='Select start date']"));
        }
        else click(By.xpath("//*[text()='Select end date']"));
        sleep(2000);
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        while (true) {
            // Get current month and year from the calendar
            WebElement monthYearElement = getElement(monthYearInCalender);
            String currentMonthYear = monthYearElement.getText();
            // Format the target date to "MMMM yyyy"
            String targetMonthYear = local_Date_Time.format(monthYearFormatter);
            // Check if the current calendar matches the target date's month and year
            if (currentMonthYear.equals(targetMonthYear)) {
                break; // Exit the loop if the target month and year are found
            }
            // Navigate to previous or next month
            if (local_Date_Time.isBefore(LocalDateTime.now())) {
                getElement(previousMonthCalenderButton).click();
            } else {
                getElement(nextMonthCalenderButton).click();
            }
            sleep(3000); // Wait for the calendar to update
        }

        // XPath for the target day in the calendar
        int targetDay = local_Date_Time.getDayOfMonth();
        //System.out.println(targetDay);
        By targetDayLocator = By.xpath("//td/button[text()='"+targetDay+"' and not(contains(@class, 'day-outside'))]");
        // Click on the target day
        click(targetDayLocator);
    }
    public void clickFilterCheckButtonByName(String ID, String Name) {
        waitForVisibility(By.xpath("//*[text()='Filters']/ancestor::section//ancestor::div[@data-state='open']//button[contains(@id, '"+ID+":"+Name+"')]"));
        //System.out.println("//*[text()='Filters']/ancestor::section//ancestor::div[@data-state='open']//button[contains(@id, '"+ID+":"+Name+"')]");
        sleep(5000);
        getElement(By.xpath("//*[text()='Filters']/ancestor::section//ancestor::div[@data-state='open']//button[contains(@id, '"+ID+":"+Name+"')]")).click();
    }
    public boolean clickRandomAssetsTypeFromFilters() {
        int index = generateRandomInt(ASSET_TYPES.size());
        clickFilterCheckButtonByName("assetTypes", ASSET_TYPES.get(index));
        click(clickApplyFilter);
        sleep(5000);
        List<WebElement> listOfAssetTypes = obtainPageData();
        for(int i = 0; i < listOfAssetTypes.size(); i++){
            String str = getDataFromGridUsingRowNumber(i + 1, "Asset Type");
            if(!str.equals(ASSET_TYPES.get(index)))return false;
        }
        return true;
    }
    public boolean clickRandomItemsFromFilters() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("document.body.style.zoom='90%'");
        List<LocalDateTime> CreatedAtrandomDateAndFinalDate = clickRandomDate("Created At");
        List<LocalDateTime> ModifiedAtrandomDateAndFinalDate = clickRandomDate("Last Modified At");
        //System.out.println(123456789);
        boolean date = true;
        sleep(5000);
        System.out.println("pass");
        List<String> items = new ArrayList<>(ITEMS);
        int numberOfFilters = generateRandomInt(items.size()) + 1;
        sleep(5000);
        //System.out.println(987654321);
        Set<String> SetOfRandomItems = new HashSet<>();
        //System.out.println(43211234);

        while (true){
            int number = generateRandomInt(items.size());
            SetOfRandomItems.add(items.get(number));
            if(SetOfRandomItems.size() == numberOfFilters)break;
        }
        sleep(5000);
        //System.out.println(12344321);
        //System.out.println(SetOfRandomItems);
        for(String item : SetOfRandomItems){
            clickFilter();
            //System.out.println(item);
            waitForVisibility(optionOfFilters);
            click(By.xpath("//*[text()='Geolocation Status']"));
            if(item.equals("Out of Coverage")){
                clickFilterCheckButtonByName("geolocationStatus","OUT_OF_COVERAGE");
            }
            else if(item.equals("Unmonitored")){
                clickFilterCheckButtonByName("monitoringStatus","UNMONITORED");
            }
            else if(item.equals("Monitored")){
                clickFilterCheckButtonByName("monitoringStatus","MONITORED");
            }
            else if(item.equals("Created At")) {
                clickFilterCheckButtonByName("dateRangeFilterTypes", "CREATED_AT");
                if(!date){
                    selectDateInFilter(CreatedAtrandomDateAndFinalDate.get(0), "Start Date");
                    sleep(5000);
                    selectDateInFilter(CreatedAtrandomDateAndFinalDate.get(1), "End Date");
                    date = true;
                }
            }
            else if(item.equals("Last Modified At")){
                clickFilterCheckButtonByName("dateRangeFilterTypes","LAST_MODIFIED_AT");
                if(!date){
                    selectDateInFilter(ModifiedAtrandomDateAndFinalDate.get(0), "Start Date");
                    sleep(5000);
                    selectDateInFilter(ModifiedAtrandomDateAndFinalDate.get(1), "End Date");
                    date = true;
                }
            }
            else{
                clickFilterCheckButtonByName("assetTypes", item);
            }
            getElement(clickApplyFilter).click();
            sleep(4000);
            //System.out.println("done");
        }

        return true;
    }
    public List<LocalDateTime> clickRandomDate(String ColumnName) {
        clickSort(ColumnName);
        String LastCreatedAtDate = getDataFromGridUsingRowNumber(1, ColumnName);
        sleep(2000);
        clickSort(ColumnName);
        String FirstCreatedAtDate = getDataFromGridUsingRowNumber(1, ColumnName);
        if (FirstCreatedAtDate == null || FirstCreatedAtDate.isEmpty() || LastCreatedAtDate == null || LastCreatedAtDate.isEmpty()) {
            throw new IllegalArgumentException("One or both dates are null/empty!");
        }
        // Define date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        // Parse the dates using LocalDateTime
        LocalDateTime startDate = LocalDateTime.parse(FirstCreatedAtDate, formatter);
        LocalDateTime endDate = LocalDateTime.parse(LastCreatedAtDate, formatter);

        List<LocalDateTime> RandomFinal = new ArrayList<>();
        long randomTime = ThreadLocalRandom.current().nextLong(startDate.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli(),
                endDate.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        LocalDateTime randomDate = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(randomTime), java.time.ZoneId.systemDefault());
        //System.out.println("Random Date: " + randomDate.format(formatter));
        long daysBetween = ChronoUnit.DAYS.between(randomDate.toLocalDate(), endDate.toLocalDate());
        int daysBetweenInt = (int) daysBetween;
        int number = 0;
        if(daysBetweenInt != 0)number = generateRandomInt(daysBetweenInt);
        long randomDayGap = number + 1;
        //System.out.println(randomDayGap);
        LocalDateTime finalDateTime = randomDate.plusDays(randomDayGap); // Sleep for 5 seconds (if needed)
        //System.out.println(finalDateTime.format(formatter));
        RandomFinal.add(randomDate);
        RandomFinal.add(finalDateTime);
        return RandomFinal;
    }
    public boolean AllActionsSituation(boolean ExpectedResult) throws InterruptedException {
        for(int i = 0; i < 1; i++){
            String str = getDataFromGridUsingRowNumber(i + 1, "Bound Data Logger(s)");
            System.out.println(str+"ab");
            if(str.equals("N/A"))continue;
            System.out.println(str+"ba");
            boolean x1 = isDisplayed(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-pen-line')]/ancestor::*[contains(@class, '!opacity-50')]"));
            boolean x2 = isDisplayed(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-link')]/ancestor::*[contains(@class, '!opacity-50')]"));
            boolean x3 = isDisplayed(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-unlink')]/ancestor::*[contains(@class, '!opacity-50')]"));;
            System.out.println(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-pen-line')]/ancestor::*[contains(@class, '!opacity-50')]"));
            System.out.println(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-link')]/ancestor::*[contains(@class, '!opacity-50')]"));
            System.out.println(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-unlink')]/ancestor::*[contains(@class, '!opacity-50')]"));

            if(x1 == x2 && x2 == x3 && x3 == ExpectedResult){
                click(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-expand')]/ancestor::a"));
                click(bound_data_loggers_inactive_status);
                waitForVisibility(bound_data_loggers_active_status);
                sleep(5000);
                boolean sitaution = false;
                List<WebElement> x5 = getElement(availableDataLoggers).findElements(By.xpath(".//tbody[1]/tr[1]/td[1]/button[@disabled='' ]"));
                if(x5.size()>0)sitaution = true;
                if(sitaution != ExpectedResult)return false;
                List<WebElement> x6 = getElement(currentBound).findElements(By.xpath(".//tbody/tr[1]/td[3]/button[@disabled='']"));
                if(x6.size()>0)sitaution = true;
                if(sitaution != ExpectedResult)return false;
                List<WebElement> x7 = getElement(currentBound).findElements(By.xpath(".//button[text()='Unbind All' and @disabled]"));
                if(x7.size()>0)sitaution = true;
                if(sitaution != ExpectedResult)return false;
                return true;
            }
            else return false;
        }
        return true;

    }
    public boolean checkBindButton() {
        List<WebElement> listOfRows = obtainPageData();
        for(int i = 0; i < listOfRows.size(); i++){
            boolean activity = isDisplayed(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-link')]/ancestor::*[contains(@class, '!opacity-50')]"));
            if(!activity){
                click(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-link')]"));
                sleep(5000);
                boolean visible = isDisplayed(bound_data_loggers_active_status);
                if(visible)return true;
                else return false;
            }

        }
        return true;
    }
    public boolean checkUnBindButton() {
        List<WebElement> listOfRows = obtainPageData();
        for(int i = 0; i < listOfRows.size(); i++){
            boolean activity = isDisplayed(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-unlink')]/ancestor::*[contains(@class, '!opacity-50')]"));
            if(!activity){
                click(By.xpath("//tbody//tr["+(i + 1)+"]//*[contains(@class, 'lucide lucide-unlink')]"));
                sleep(5000);
                boolean visible = isDisplayed(bound_data_loggers_active_status);
                if(visible)return true;
                else return false;
            }

        }
        return true;
    }
    public String getMultipleDLsByName(String name){
        List<WebElement> rows = getElements(Datagrid);
        int nameRowIndex = getColumnIndex("Name");
        int BoundDataLoggersIndex = getColumnIndex("Bound Data Logger(s)");
        for(int i = 0; i < rows.size(); i++){
            WebElement row = rows.get(i);
            WebElement element = row.findElement(By.xpath(".//td[" + (nameRowIndex + 1)+ "]"));
            boolean x = element.isDisplayed();
            //tbody/tr[1]/td[5]//a
            if(x && element.getText().equals(name)){
                hoverOverElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[" + (BoundDataLoggersIndex + 1) + "]"));
                List<WebElement> listOfDLs = getElements(By.xpath("//tbody/tr["+(i + 1)+"]//a"));
                Set<String> St = new HashSet<>(); // Initialize the Set
                for (int j = 0; j < listOfDLs.size(); j++) {
                    String lDLs = listOfDLs.get(j).getText().trim(); // Trim to remove extra spaces
                    if (lDLs.isEmpty()) continue; // Skip if the text is empty or blank
                    St.add(lDLs); // Add the cleaned lDLs to the Set
                }
                // Combine elements of St into a single string with ", " as the separator
                String result = String.join(", ", St);
                // Print the result
                return result;
            }
        }
        return "";
    }

    public void searchByName(String Name, String ColumnName){
        getElement(By.xpath("//*[text()='"+ColumnName+"']//following-sibling::div/button[1]")).click();
        sleep(2000);
        waitForVisibility(By.xpath("//input[@placeholder='Search']"));
        getElement(By.xpath("//input[@placeholder='Search']")).sendKeys(Name);
        sleep(2000);
        getElement(By.xpath("//input[@placeholder='Search']//following-sibling::span//*[@stroke-linecap='round'][2]")).click();
        sleep(2000);
    }
    public List<String > getAllDataByName(String Name){
        WebElement nameRow = getRowsByName(Name);
        int columnIndex = getColumnIndex("Name");
        String name =  nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        columnIndex = getColumnIndex("Asset Type");
        String assetType = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        String monitoredStatus = getCellDataByNameForMonitoredStatus(name);
        columnIndex = getColumnIndex("Geolocation");
        String geolocation = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        columnIndex = getColumnIndex("Bound Data Logger(s)");
        String boundDataLoggers = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        // 2 DLs/3 DLs/4 DLs
        String situations = "false";
        if(boundDataLoggers.equals("2 DLs") || boundDataLoggers.equals("3 DLs") || boundDataLoggers.equals("4 DLs")){
            boundDataLoggers = getMultipleDLsByName(name);
            situations = "true";
        }
        columnIndex = getColumnIndex("Created At");
        String CreatedAt = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        columnIndex = getColumnIndex("Created By");
        String CreatedBy = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        columnIndex = getColumnIndex("Last Modified At");
        String LastModifiedAt = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        columnIndex = getColumnIndex("Last Modified By");
        String LastModifiedBy = nameRow.findElement(By.xpath(".//td[" + (columnIndex + 1) + "]")).getText();
        List<String>name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9 = new ArrayList<>();
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(name);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(assetType);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(monitoredStatus);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(geolocation);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(boundDataLoggers);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(CreatedAt);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(CreatedBy);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(LastModifiedAt);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(LastModifiedBy);
        name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9.add(situations);
        return name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8sitaution9;
    }
    public boolean clickAndCheckPopupByname(List<String> name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9) throws InterruptedException {
        String name = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(0);
        String assetType = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(1);
        String monitoredStatus = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(2);
        String geolocation = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(3);
        String boundDataLoggers = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(4);
        String CreatedAt = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(5);
        String CreatedBy = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(6);
        String LastModifiedAt = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(7);
        String LastModifiedBy = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(8);
        String situation = name0assetType1monitoredStatus2geolocation3boundDataLoggers4CreatedAt5CreatedBy6LastModifiedAt7LastModifiedBy8Situation9.get(9);
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(name);
        for(int p = 0; p < 10; p++){
            try{
                getElement(By.xpath("//tbody/tr["+(rowIndex + 1)+"]/td[" +(columnIndex + 1) +"]//button")).click();
                break;
            } catch (Exception e) {
                sleep(3000);
                throw new RuntimeException(e);
            }
        }
        waitForVisibility(By.xpath("//*[@class='flex h-full flex-col px-[24px] py-4']"));
        boolean x = isDisplayed(By.xpath("//*[@class='flex h-full flex-col px-[24px] py-4']"));
        if(!x)return false;
        String popupName = getElement(By.xpath("//h2[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        if(!name.equals(popupName))return false;
        List<WebElement> AssettypeMonitoredstatusGeolocationBounddataloggers = getElements(By.xpath("//p[@class='flex w-full items-center gap-2 text-xs']"));
        for(int i = 0; i < AssettypeMonitoredstatusGeolocationBounddataloggers.size(); i++){
            String pqr = AssettypeMonitoredstatusGeolocationBounddataloggers.get(i).getText();
            //System.out.println("pass "+pqr);
            if(pqr.startsWith("Asset Type")){
                if(!pqr.endsWith(assetType))return false;
            }
            else if(pqr.startsWith("Monitored Status")){
                if(!pqr.endsWith(monitoredStatus))return false;
            }
            else if(pqr.startsWith("Geolocation")){
//                System.out.println(geolocation);
//                System.out.println(pqr);
                if(!pqr.endsWith(geolocation))return false;
            }
            else if(pqr.startsWith("Bound Data Logger(s)")){
                if(situation.equals("false")){
                    if(!pqr.endsWith(boundDataLoggers))return false;
                }
                else{
                    pqr = pqr.substring(pqr.indexOf("\n") + 1);
                    Set<String> uniqueSet1 = new HashSet<>(Arrays.asList(pqr.split(", ")));
                    Set<String> uniqueSet2 = new HashSet<>(Arrays.asList(boundDataLoggers.split(", ")));
                    if(!uniqueSet1.equals(uniqueSet2))return false;
                }

            }
        }
        List<WebElement> CaCbLmaLmbOptionName = getElements(By.xpath("//*[@class='whitespace-pre-wrap text-xs font-semibold text-doglapan-grey-80']"));
        List<WebElement> CaCbLmaLmbOptionResult = getElements(By.xpath("//*[@class='whitespace-pre-wrap text-xs text-doglapan-grey-80']"));
        for(int i = 0; i < 4; i++){
            String pqr1 = CaCbLmaLmbOptionName.get(i).getText().trim();
            String pqr2 = CaCbLmaLmbOptionResult.get(i).getText().trim();
            if(pqr1.equals("Created At")){
                if(!pqr2.equals(CreatedAt))return false;
            }
            else if(pqr1.equals("Created By")){
                if(!pqr2.equals(CreatedBy))return false;
            }
            else if(pqr1.equals("Last Modified At")){
                if(!pqr2.equals(LastModifiedAt))return false;
            }
            else if(pqr1.equals("Last Modified By")){
                if(!pqr2.equals(LastModifiedBy))return false;
            }
        }
        if(!isDisplayed(By.xpath("//*[text()='View Full Detail']/ancestor::a[contains(@href, '/assets')]")))return false;
        //Thread.sleep(5000);
        click(By.xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']//following-sibling::*//*[contains(@class, 'lucide lucide-x')]//parent::button"));
        return true;
    }
    public boolean clickAndCheckAssetDetailsPageByName() throws InterruptedException {
        int columnIndex = getColumnIndex("Name");
        for(int p = 0; p < 10; p++){
            try{
                getElement(By.xpath("//tbody/tr[1]/td[" +(columnIndex + 1) +"]//button")).click();
                break;
            } catch (Exception e) {
                sleep(3000);
                throw new RuntimeException(e);
            }
        }
        waitForVisibility(By.xpath("//*[@class='flex h-full flex-col px-[24px] py-4']"));
        Thread.sleep(5000);
        boolean x = isDisplayed(By.xpath("//*[@class='flex h-full flex-col px-[24px] py-4']"));
        if(!x)return false;
        List<String> datas = new ArrayList<>();
        String popupName = getElement(By.xpath("//h2[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText();
        datas.add("Asset Name\n"+popupName);
        List<WebElement> AssettypeMonitoredstatusGeolocationBounddataloggers = getElements(By.xpath("//p[@class='flex w-full items-center gap-2 text-xs']"));
        for(int i = 0; i < AssettypeMonitoredstatusGeolocationBounddataloggers.size(); i++){
            String pqr = AssettypeMonitoredstatusGeolocationBounddataloggers.get(i).getText();
            if(pqr.startsWith("Bound Data Logger(s)"))continue;
            datas.add(pqr);
        }
        List<WebElement> CaCbLmaLmbOptionName = getElements(By.xpath("//*[@class='whitespace-pre-wrap text-xs font-semibold text-doglapan-grey-80']"));
        List<WebElement> CaCbLmaLmbOptionResult = getElements(By.xpath("//*[@class='whitespace-pre-wrap text-xs text-doglapan-grey-80']"));
        for(int i = 0; i < 4; i++) {
            String pqr1 = CaCbLmaLmbOptionName.get(i).getText().trim();
            String pqr2 = CaCbLmaLmbOptionResult.get(i).getText().trim();
            datas.add(pqr1+"\n"+pqr2);
        }
        click(By.xpath("//*[text()='View Full Detail']/ancestor::a[contains(@href, '/assets')]"));
        List<WebElement> assetDetailsPageGI = getElements(By.xpath("//*[@class='flex items-start justify-start gap-6 rounded bg-white p-2 pl-0']"));
        boolean situation = false;
        for(int i = 0; i < assetDetailsPageGI.size(); i++){
            String wst = assetDetailsPageGI.get(i).getText();
            if(wst.startsWith("Current Shipment"))continue;
            if(wst.startsWith("Location"))continue;
            if(wst.startsWith("Last Location"))situation = true;
            else{
                if(!datas.contains(wst))return false;
            }
        }
        return situation;
    }
    public String GetRandomNameAPStype(){
        List<WebElement> dataGrids = getElements(Datagrid);
        int numberOfName = dataGrids.size();
        int columnIndex = getColumnIndex("Asset Type");
        for(int i = 0; i < numberOfName; i++){
            String assetType = getText(By.xpath("//tbody/tr["+(i + 1)+"]/td["+(columnIndex + 1)+"]"));
            if(assetType.contains("APS")){
                columnIndex = getColumnIndex("Name");
                return getText(By.xpath("//tbody/tr[" + (i + 1) + "]/td["+(columnIndex + 1)+"]"));
            }
        }
        return "";
    }
    public boolean APSdropingOptionBoundDataLogger(String assetType){
        click(By.xpath("//div[contains(@id, content-bound-data-logger)]//div[3]/section//tbody//tr[1]//td[4]//button"));
        waitForVisibility(By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]"));
        Set<String> givenCompartmentSet = new HashSet<>(Set.of("Compartment Door", "APPS"));
        if(assetType.equals("doglapan APS-D")){
            givenCompartmentSet.add("Compartment Ceiling Back");
            givenCompartmentSet.add("Compartment Ceiling Front");
        }
        else{
            givenCompartmentSet.add("Compartment Ceiling");
        }
        List<WebElement> texts = getElements(By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]//div[@role='option']"));
        Set<String> gettingCompartmentSet = new HashSet<>();
        for(int i = 0; i < texts.size(); i++){
            gettingCompartmentSet.add(texts.get(i).getText());
            if(i + 1 == texts.size())texts.get(i).click();
        }
        return gettingCompartmentSet.equals(givenCompartmentSet);
    }
    public String UserName() {
        List<WebElement> headers = getElements(By.xpath("//header//span[1]"));
        return headers.get(headers.size() - 1).getText();
    }
    public String selectRandomFromDropdownBox(By Button, By SelectOption, String StartsWith){
        click(Button);
        List<WebElement>listOfOrganizationName = getElement(SelectOption).findElements(xpath(".//p"));
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
    public String selectOrg(String Org) {
        click(organizationButton);
        sleep(5000);
        List<WebElement> dropdownOptions = getElements(xpath("//*[@role='option']"));
        if(Org.equals("")) {
            int randomNumber = generateRandomInt(dropdownOptions.size());
            System.out.println(dropdownOptions.get(randomNumber).getText());
            dropdownOptions.get(randomNumber).click();
            return dropdownOptions.get(randomNumber).getText();
        }
        else{
            for(int i = 0; i < dropdownOptions.size(); i++){
                if(Org.equals(dropdownOptions.get(i).getText().trim())){
                    dropdownOptions.get(i).click();
                    return dropdownOptions.get(i).getText();
                }
            }
        }
        return "";
    }
    public List<String> createAssetMethod(){
        clickNewAsset();
        String assetType = ASSET_TYPES.get(generateRandomInt(ASSET_TYPES.size()));
        selectDropdownByText(dropdownSelectLocator, assetType);
        String assetName = generateRandomString(5, "autoAssetName");
        waitForVisibility(assetNameInput).sendKeys(assetName);
        String organization = selectOrg("");
        clickCreateButton();
        LocalDateTime timeAfterClick = LocalDateTime.now();
        String dateTime = timeAfterClick.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        String username = UserName();
        List<String> listOfNameOrganizationAssetTypeTimeUserName = Arrays.asList(assetName, organization, assetType, dateTime, username);
        return listOfNameOrganizationAssetTypeTimeUserName;
    }
    public void deleteAssetByName(String name){
        int columnIndex = getColumnIndex("Actions");
        int row = getRowIndex(name);
        waitForVisibility(By.xpath("//tbody/tr[" + (row + 1) + "]/td[" + (columnIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]")).click();
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
    }
    public List<List<String>> searchByColumn(String searhValue, String columnname){
//        getElement().click();
        click(By.xpath("//*[text()='"+columnname+"']//following-sibling::div/button[1]"));
        sleep(2000);
        waitForVisibility(By.xpath("//input[@placeholder='Search']"));
        getElement(By.xpath("//input[@placeholder='Search']")).sendKeys(searhValue);
        sleep(2000);
        getElement(By.xpath("//*[contains(@class,'lucide-search')]")).click();
        sleep(2000);
        return extractFullTableData(By.xpath("//table"));
    }
    public boolean isDataContainInColumn(String searchValue,String columnKey) throws InterruptedException {
        System.out.println(searchByColumn(searchValue, columnKey));
        List<List<String>> tableValueAfterSearch = searchByColumn(searchValue, columnKey);
        // Verify if the search returned any results
        if (tableValueAfterSearch.isEmpty()) {
            return  false;
        }
        // Print the table rows after the search
        for (List<String> row : tableValueAfterSearch) {
            System.out.println(row);
        }

        // Get the column index for verification
        int columnIndex = getColumnIndex(columnKey);

        // Verify the results
        boolean allMatch = true;
        for (int i = 1; i < tableValueAfterSearch.size(); i++) { // Skip the header row
            List<String> row = tableValueAfterSearch.get(i);
            String cellValue = row.get(columnIndex).toLowerCase();

            if (!cellValue.contains(searchValue.toLowerCase())) {
                System.out.println("Mismatch found: " + cellValue + " does not contain " + searchValue);
                allMatch = false;
            }
        }

        if (allMatch) {
            System.out.println("All rows match for " + columnKey + " = " + searchValue);
            return true;
        }
//        // Clear filters before the next iteration
//        clickClearFilters();
        Thread.sleep(2000); // Small delay to allow UI to reset
        return false;
    }
    public void clickOnName(String Name){
        int columnIndex = getColumnIndex("Name");
        int rowIndex = getRowIndex(Name);
        for(int p = 0; p < 10; p++){
            try{
                getElement(By.xpath("//tbody/tr["+(rowIndex + 1)+"]/td[" +(columnIndex + 1) +"]//button")).click();
                break;
            } catch (Exception e) {
                sleep(3000);
                throw new RuntimeException(e);
            }
        }
    }
    public String getDataFromPopupByColumnName(String ColumnName){
        String typeA = "//p[@class='flex w-full items-center gap-2 text-xs']";
        List<WebElement> elements = getElements(xpath(typeA+"//*[1]"));
        for(int i = 0; i < elements.size(); i++){
            if(elements.get(i).getText().equals(ColumnName))return getElements(xpath(typeA+"//*[2]")).get(i).getText();
        }
        String typeB1 = "//*[@class='text-xs font-semibold text-doglapan-grey-80']";
        String typeB2 = "//*[@class='text-xs text-doglapan-grey-80']";
        elements = getElements(xpath(typeB1));
        for(int i = 0; i < elements.size(); i++){
            if(elements.get(i).getText().equals(ColumnName))return getElements(xpath(typeB2)).get(i).getText();
        }
        return "";
    }
    public void clickFullDetailsPageFromPopup(){
        try {
            click(xpath("//*[text()='View Full Detail']/ancestor::a[contains(@href, '/assets')]"));
        } catch (Exception e) {
            getElement(xpath("//*[text()='View Full Detail']/ancestor::a[contains(@href, '/assets')]")).click();
        }
    }
    public void clickOnFullDetailsPageFromGrid(String assetName){
        int rowIndex = getRowIndex(assetName);
        click(xpath("//tbody//tr["+(rowIndex + 1)+"]//*[contains(@class, 'lucide-expand')]//parent::a"));
    }
    public void clickOnLocation(){
        click(xpath("//*[text()='Location']/parent::button[@data-state='inactive']"));
    }
    public boolean ValidatePushedLocationData(String longT, String latT){
        String lat = getElement(xpath("(//p)[1]")).getText();
        String lon = getElement(xpath("(//p)[2]")).getText();
        System.out.println(lat);
        System.out.println(lon);
        if(!lat.equals("Latitude" + latT))return false;
        return lon.equals("Longitude" + longT);
    }
    public void clickOnEnvironMentalConditions(){
        click(xpath("//*[text()='Environmental Conditions']/parent::button[@data-state='inactive']"));
    }

    private Integer extractTemperature(String xpathExpression) {
        try {
            String str = getElement(xpath(xpathExpression)).getText().replace("°C", "").trim();
            double doubleValue = Double.parseDouble(str) * 10;
            return (int) Math.round(doubleValue);
        } catch (Exception e) {
            System.err.println("Error parsing temperature: " + e.getMessage());
            return null; // Or return a default value like 0
        }
    }

    // Helper method for humidity extraction
    private Integer extractHumidity(String xpathExpression) {
        try {
            String str = getElement(xpath(xpathExpression)).getText().replace("%", "").trim();
            return Integer.parseInt(str);
        } catch (Exception e) {
            System.err.println("Error parsing humidity: " + e.getMessage());
            return null; // Or return a default value like 0
        }
    }
    public boolean validatePushedEnvironmentalConditionData(Integer cargoTemp, Integer ambientTemp, Integer cargoHumidity, Integer ambientHumidity){
        if(!isDisplayed(xpath("//*[text()='Cargo Temperature']//ancestor::button[@data-state='active']" +
                "//following-sibling::*//*[text()='Ambient Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Cargo Humidity']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Humidity']//ancestor::button[@data-state='inactive']"))) return false;
        Integer temp = extractTemperature("(//*[text()='Cargo Temperature']//ancestor::button//p)[2]");
        if(temp != cargoTemp)return false;
        temp = extractTemperature("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(temp != cargoTemp)return false;
        click(xpath("//*[text()='Ambient Temperature']//ancestor::button[@data-state='inactive']"));
        sleep(2000);
        if(!isDisplayed(xpath("//*[text()='Cargo Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Temperature']//ancestor::button[@data-state='active']" +
                "//following-sibling::*//*[text()='Cargo Humidity']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Humidity']//ancestor::button[@data-state='inactive']"))) return false;
        temp = extractTemperature("(//*[text()='Ambient Temperature']//ancestor::button//p)[2]");
        if(temp != ambientTemp)return false;
        temp = extractTemperature("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(temp != ambientTemp)return false;


        click(xpath("//*[text()='Cargo Humidity']//ancestor::button[@data-state='inactive']"));
        sleep(2000);
        if(!isDisplayed(xpath("//*[text()='Cargo Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Cargo Humidity']//ancestor::button[@data-state='active']" +
                "//following-sibling::*//*[text()='Ambient Humidity']//ancestor::button[@data-state='inactive']"))) return false;
        temp = extractHumidity("(//*[text()='Cargo Humidity']//ancestor::button//p)[2]");
        if(temp != cargoHumidity)return false;
        temp = extractHumidity("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(temp != cargoHumidity)return false;
        click(xpath("//*[text()='Ambient Humidity']//ancestor::button[@data-state='inactive']"));
        sleep(2000);
        if(!isDisplayed(xpath("//*[text()='Cargo Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Temperature']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Cargo Humidity']//ancestor::button[@data-state='inactive']" +
                "//following-sibling::*//*[text()='Ambient Humidity']//ancestor::button[@data-state='active']"))) return false;
        temp = extractHumidity("(//*[text()='Ambient Humidity']//ancestor::button//p)[2]");
        if(temp != ambientHumidity)return false;
        temp = extractHumidity("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(temp != ambientHumidity)return false;
        return true;
    }
    public void clickAssetsFromAssetDetails(){
        while (true){
            try{
                click(xpath("//button[text()='Assets']"));
                break;
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean ActionsSituation(String monitoredStatus, String onShipment, String assetName) {
        int rowIndex = getRowIndex(assetName);
        if (onShipment.equals("Yes")) {
            boolean bindDisable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-link')]//parent::*[contains(@class, '!opacity-50')]"));
            boolean unBindDisable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-unlink')]//parent::*[contains(@class, '!opacity-50')]"));
            boolean editDisable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-pen-line')]//parent::*[contains(@class, '!opacity-50')]"));
            boolean deleteDisable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]//parent::*[contains(@class, '!opacity-50')]"));
            return bindDisable && unBindDisable && editDisable && deleteDisable;
        } else {
            if (monitoredStatus.equals("Monitored")) {
                boolean bindEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-link')]//parent::a"));
                boolean unBindEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-unlink')]//parent::a"));
                boolean editEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-pen-line')]//parent::button"));
                boolean deleteDisable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]//parent::*[contains(@class, '!opacity-50')]"));
                return bindEnable && unBindEnable && editEnable && deleteDisable;
            } else {
                boolean bindEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-link')]//parent::a"));
                boolean unBindEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-unlink')]//parent::a"));
                boolean editEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-pen-line')]//parent::button"));
                boolean deleteEnable = isDisplayed(xpath("//tbody//tr[" + (rowIndex + 1) + "]//*[contains(@class, 'lucide lucide-trash2')]//parent::button"));
                return bindEnable && unBindEnable && editEnable && deleteEnable;
            }
        }
    }

}
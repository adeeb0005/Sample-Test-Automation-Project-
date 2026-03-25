package com.TEST.pages;

import com.TEST.utils.RunManager;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ShipmentsPage extends BasePage{
    public static String downloadDir = RunManager.getDownloadsDirectory();
    String assetNameBulk = "autoAssetJ001";
    String assetNameGene = "AutoAsset002";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final By shipmentsPageLink = xpath("//div[@role='menubar']//*[text()='Shipments']/ancestor::button");
    private final By shipmentsHeaderTitle = By.xpath("//*[@data-testid='asset-list-header-title']");
    private final By newShipmentButton = By.xpath("//*[text()='New Shipment']/ancestor::button[position() = 1]");
    private final By shipmentDownloadButton = By.xpath("//button[@data-testid='upload-csv-button'][position() = 1]");
    private final By shipmentsTableBulkActionButton = By.xpath("//button[@data-testid='table-bulk-action-button'][position() = 1]");
    private final By shipmentsDeleteSelectedDropdown = By.xpath("//div[text()='Delete Selected']");
    private final By shipmentsFilterButton = By.xpath("//span[text()='Filters']/ancestor::button[position() = 1]");
    private final By shipmentsDataGridTable = By.xpath("//table");
    private final By shipmentsDataGridTableHeaderColumns = By.xpath("//table//th");
    private final By addNewShipmentButton = By.xpath("//*[text()='New Shipment']/ancestor::button");
    private final By selectNewShipment = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");

    // New Shipment initial form
    private final By shipmentNameField = By.xpath("//label[text()='Shipment Name' and .//span[text()='*']]/following-sibling::div/input[1]");
    private final By shipperNameField = By.xpath("//label[text()='Shipper Name']/following-sibling::div/input[position()=1]");
    private final By orderNameField = By.xpath("//label[text()='Order Number']/following-sibling::div/input[position()=1]");
    private final By carrierNameSelect = By.xpath("//*[text()='Carrier Name ']/following-sibling::button");
    private final By modeOfTransportSelect = By.xpath("//label[text()='Mode of Transport']/following-sibling::div/select[position()=1]");
    private final By customerNameField = By.xpath("//label[text()='Customer Name']/following-sibling::div/input[position()=1]");
    private final By recipientNameField = By.xpath("//label[text()='Recipient Name']/following-sibling::div/input[position()=1]");
    private final By organizationSelect = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By dropdownSelect = xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By modeOfTransportSelectButton = xpath("//label[text()='Mode of Transport']/following-sibling::*//button");
    private final By organizationSelectButton = By.xpath("//label[text()='Organization ' and .//span[text()='*']]/following-sibling::button[position()=1]");
    private final By carrierNameSelectButton = xpath("//label[text()='Carrier Name']/following-sibling::*//button");
    private final By dataLoggerProfileSectionButton = By.xpath("//*[text()='Data Logger Profile']/ancestor::button");
    private final By dataLoggerProfileSelectButton = By.xpath("//label[text()='Data Logger Profile']/following-sibling::button");
    private final By dataLoggerProfileSelect = By.xpath("//div[contains(@class,'scrollbar css')]");

    private final By rulesSectionButton = By.xpath("//form//*[text()='Rules']/ancestor::button");
    private final By availableRulesSection = By.xpath("//*[contains(text(),'Available Rules')]");
    private final By selectedRulesSection = By.xpath("//*[contains(text(),'Selected Rules')]");
    private final By selectAllRulesFromAvailableRules = By.xpath("//*[contains(text(), 'Available Rules')]/parent::div//*[@data-testid='select-all']");
    private final By selectNoneRulesFromAvailableRules = By.xpath("//*[contains(text(), 'Available Rules')]/parent::div//*[@data-testid='none']");
    private final By plusIconToAddAvailableRules = By.xpath("//*[contains(text(), 'Available Rules')]/ancestor::div[2]/following-sibling::div[1]/button");
    private final By RemoveAllRulesFromSelectedRules = By.xpath("//*[contains(text(), 'Selected Rules')]/parent::div//*[@data-testid='remove-all']");
    private final By RemoveSelectedRulesFromSelectedRules = By.xpath("//*[contains(text(), 'Selected Rules')]/parent::div//*[@data-testid='remove-all-selected']");

    private final By originAndDestinationSectionButton = By.xpath("//*[text()='Origin and Destination' and .//span[text()='*']]/ancestor::button");
    private final By inboundRadioButton = By.xpath("//*[text()='Origin and Destination' and .//span[text()='*']]/ancestor::button/following-sibling::div//button[@value='Inbound']");
    private final By outboundRadioButton = By.xpath("//*[text()='Origin and Destination' and .//span[text()='*']]/ancestor::button/following-sibling::div//button[@value='Inbound']");
    private final By routeSelection = By.xpath("//label[text()='Select Route' and .//span[text()='*']]/following-sibling::div//select");
    private final By plannedDepartureDateTimeField = By.xpath("//label[text()='Planned Departure']/following-sibling::input");
    private final By plannedArrivalDateTimeField = By.xpath("//label[text()='Planned Arrival']/following-sibling::input");

    private final By assetsInShipmentSectionButton = By.xpath("//*[text()='Assets in Shipment' and .//span[text()='*']]/ancestor::button");
    private final By assetTypeSelectButton = By.xpath("//button[text()='Show']");
    private final By assetsInShipmentSectionDropdown = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By showButton = By.xpath("//*[text()='Assets in Shipment' and .//span[text()='*']]/ancestor::button/following-sibling::div//button[text()='Show']");
    private final By availableAssetsSection = By.xpath("//*[text()='Assets in Shipment' and .//span[text()='*']]/ancestor::button/following-sibling::div//h2[contains(text(), 'Available Assets')]");
    private final By addedAssetsSection = By.xpath("//*[text()='Assets in Shipment' and .//span[text()='*']]/ancestor::button/following-sibling::div//h2[contains(text(), 'Added Assets')]");
    private final By selectAllAssetsFromAvailableAssets = By.xpath("//*[contains(text(),'Available Assets')]/parent::div//*[@data-testid='select-all']");
    private final By selectNoneAssetsFromAvailableAssets = By.xpath("//*[contains(text(),'Available Assets')]/parent::div//*[@data-testid='none']");
    private final By RemoveAllAssetsFromSelectedAssets = By.xpath("//*[contains(text(),'Added Assets')]/parent::div//*[@data-testid='remove-all']");
    private final By RemoveSelectedAssetsFromSelectedAssets = By.xpath("//*[contains(text(),'Added Assets')]/parent::div//*[@data-testid='remove-all-selected']");
    private final By plusIconToAddAvailableAssets = By.xpath("//*[contains(text(), 'Available Assets')]/ancestor::div[2]/following-sibling::div[1]/button");
    private final By selectedAssetsSection = By.xpath("//*[contains(text(),'Added Assets')]");
    private final By assetTrackingID = By.xpath("//*[contains(text(),'Added Assets')]//ancestor::div/div[contains(@class, 'scrollbar')]//input[@placeholder='Tracking ID']");
    String desiredAssetXpathBulk = String.format("//*[text()='%s']/preceding-sibling::button", assetNameBulk);
    private final By desiredAssetCheckBoxBulk = By.xpath(desiredAssetXpathBulk);
    String desiredAssetXpathGene = String.format("//*[text()='%s']/preceding-sibling::button", assetNameGene);
    private final By desiredAssetCheckBoxGene = By.xpath(desiredAssetXpathGene);
    private final By plusIconToAddAssets = By.xpath("//*[contains(text(), 'Available Assets')]/ancestor::div[2]/following-sibling::div[1]/button");
    public final By shipmentRules = xpath("//*[@data-testid='shipment-rules-multi-value-left-scrollbar']");
    private final By notificationSectionButton = By.xpath("//*[text()='Notification'and .//span[text()='*']]/ancestor::button");
    private final By emailFieldInNotificationSection = By.xpath("//label[normalize-space()='To Email* (Separate multiple email addresses by comma)']/following-sibling::div/input");
    private final By statusButton = xpath("//*[text()='Status']/ancestor::button");
    private final By createButtonInAddNewShipment = By.xpath("//*[text()='Create']/ancestor::button");
    private final By cancelButtonInAddNewShipment = By.xpath("//*[text()='Cancel']/ancestor::button");

    private final By checkPagination = By.xpath("//*[@aria-label='pagination']");
    private final By generalInfoButton = xpath("//*[text()='General Info']//ancestor::button");
    private final By isRequireds = By.xpath("//p[contains(text(), 'required')]");
    private final By shipmentOptions = By.xpath("//*[@role='radiogroup']//button//following-sibling::input");
    private final By shipmentOptionsButton = By.xpath("//*[@role='radiogroup']//button");
    private final By selectRouteButton = By.xpath("//*[text()='Select Route ' and //*[text()='*']]//following-sibling::button");
    private final By selectRouteButtonOptions = By.xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]");
    private final By tableHeaders = By.xpath("//table//thead//tr//th");
    private final By tableBodyRows = By.xpath("//table//tbody//tr");
    String status = "//*[contains(@class, 'lucide-sun-snow')]//following-sibling::*[text()='Status']//parent::button";
    String assetsInMap = "//*[contains(@class, 'lucide-map')]//following-sibling::*[text()='Assets in Map']//parent::button";
    String generalInfo = "//*[contains(@class, 'lucide-list')]//following-sibling::*[text()='General Info']//parent::button";
    String progressButton = "//*[contains(@class, 'lucide-arrow-up-from-dot')]//following-sibling::*[text()='Progress']//parent::button";
    String mapView = "//*[contains(@class, 'lucide-map')]//following-sibling::*[text()='Map View']//parent::button";
    String environmentalConditions = "//*[contains(@class, 'lucide-sun-snow')]//following-sibling::*[text()='Environmental Conditions']//parent::button";
    String chainOfCustody = "//*[text()='Chain of Custody']/parent::button";
    String activeDataState = "[@data-state='active']";
    String inactiveDataState = "[@data-state='inactive']";
    String shipmentsTextInDetailsPage = "//*[contains(@class, 'lucide-truck')]//following-sibling::button[text()='Shipments']";
    String[] shipmentsDataGridTableColumnHeaderTitle = {
            "", "Name", "Actions", "Shipment Type", "Status", "Direction", "Origin", "Destination", "Shipper Name",
            "Order Number", "Carrier Name", "Mode of Transport", "Customer Name", "Recipient Name", "Organization",
            "Asset Count", "Planned Departure", "Planned Arrival", "Route Name", "Data Logger Profile", "Created At",
            "Created By", "Last Modified At", "Last Modified By"
    };
    Set<String> requiredFields = Set.of(
            "Shipment Name is required",
            "Organization is required",
            //"Rule is required",
            "Route is required",
            "Asset is required",
            "Email(s) is required"
    );
    List<String> HeadersWithDoubleButton = Arrays.asList("Name", "Origin", "Destination", "Order Number", "Customer Name",
            "Recipient Name","Created By");
    public ShipmentsPage(WebDriver driver) {
        super(driver);
    }

    public String getShipmentsHeaderTitle() {
        return getText(shipmentsHeaderTitle);
    }

    public boolean isDownloadButtonPresent() {
        return isDisplayed(newShipmentButton);
    }

    public boolean isNewShipmentButtonPresent() {
        return isDisplayed(shipmentDownloadButton);
    }

    public boolean isShipmentsTableBulkActionButtonPresent() {
        return isDisplayed(shipmentsTableBulkActionButton);
    }

    public boolean isShipmentsDeleteSelectedDropdownPresent() {
        return isDisplayed(shipmentsDeleteSelectedDropdown);
    }

    public boolean isShipmentsFilterButtonPresent() {
        return isDisplayed(shipmentsFilterButton);
    }

    public boolean isShipmentsDataGridTablePresent() {
        return isDisplayed(shipmentsDataGridTable);
    }

    // ======================================== Start ======================================
    public void clickOnAddNewShipment() {
        click(addNewShipmentButton);
        logAction("Clicked on '+ New Shipment'");
    }

    public void clickOnCreateButtonInCreateShipmentPopper() {
        click(createButtonInAddNewShipment);
    }

    public void clickOnCancelButtonInAddNewShipment() {
        click(cancelButtonInAddNewShipment);
    }

    public void expendDataLoggerProfileInAddNewShipments() {
        click(dataLoggerProfileSectionButton);
    }

    public void expendRulesInAddNewShipments() {
        click(rulesSectionButton);
    }

    public void expendOriginAndDestinationInAndNewShipments() {
        click(originAndDestinationSectionButton);
    }

    public void expendAssetsInShipmentInAddNewShipments() {
        click(assetsInShipmentSectionButton);
    }

    public void expendNotificationInAddNewShipments() {
        click(notificationSectionButton);
    }

    public String selectShipmentType(String shipmentType) {
        clickOnAddNewShipment();
        try {
            selectDropdownByText(selectNewShipment, shipmentType);
            logAction("Successfully selected shipment type: " + shipmentType);
            return shipmentType;
        } catch (NoSuchElementException e) {
            logAction("Failed to select shipment type '" + shipmentType + "': Element not found. Error: " + e.getMessage());
        } catch (Exception e) {
            logAction("An unexpected error occurred while selecting shipment type '" + shipmentType + "'. Error: " + e.getMessage());
        }
        return null;
    }

    public String clearAndEnterShipmentName(String shipmentName) {
//        String shipmentName = generateRandomString(12, "AutoShipment");
        clearAndEnterText(shipmentNameField, shipmentName);
        return shipmentName;
    }

    public String clearAndEnterShipperName(String shipperName) {
//        String  shipperName = generateRandomString(12, "AutoShipper" );
        clearAndEnterText(shipperNameField, shipperName);
        return shipperName;
    }

    public String clearAndEnterOrderNumber(String orderNumber) {
//        String orderNumber = generateRandomString(12, "AutoOrder");
        clearAndEnterText(orderNameField, orderNumber);
        return orderNumber;
    }

    public String selectCarrierName(String carrierName) {
        click(xpath("//*[text()='Carrier Name ']/following-sibling::button"));
//        selectDropdownByText(xpath("//*[@data-testid='CarrierNameInputField']/select"), carrierName);
        click(xpath("//div[@cmdk-list-sizer]//*[@data-value='"+carrierName+"']"));
        return carrierName;
    }

    public String selectModeOfTransport(String modeOfTransport) {
        click(xpath("//*[@data-testid='ModeOfTransportSelectField']/button"));
//        selectDropdownByText(xpath("//*[@data-testid='ModeOfTransportSelectField']/select"), modeOfTransport);
        selectDropdownByText(xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]"), modeOfTransport);
        return modeOfTransport;
    }

    public String clearAndEnterCustomerName(String customerName) {
//        String str = generateRandomString(12, "AutoCustomer");
        clearAndEnterText(customerNameField, customerName);
        return customerName;
    }

    public String clearAndEnterRecipientName(String recipientName) {
//        String str = generateRandomString(12, "AutoRecipient");
        clearAndEnterText(recipientNameField, recipientName);
        return recipientName;
    }

    public String selectOrganization(String organization, String organizationId) {
        click(organizationSelectButton);
        selectDropdownWithSearchByText(organization, organizationId);
        return organization;
    }

    public Map<String, Object> fillUpNewShipmentsInitial(
            String shipmentName,
            String shipperName,
            String orderNumber,
            String carrierName,
            String customerName,
            String recipientName,
            String organization,
            String organizationId
    ) {
        Map<String, Object> initialShipmentData = new HashMap<>();

        initialShipmentData.put("shipmentName", clearAndEnterShipmentName(shipmentName));
        initialShipmentData.put("shipperName", clearAndEnterShipperName(shipperName));
        initialShipmentData.put("orderNumber", clearAndEnterOrderNumber(orderNumber));
        initialShipmentData.put("carrierName", selectCarrierName(carrierName));
        initialShipmentData.put("customerName", clearAndEnterCustomerName(customerName));
        initialShipmentData.put("recipientName", clearAndEnterRecipientName(recipientName));
        initialShipmentData.put("organization", selectOrganization(organization, organizationId));

        return initialShipmentData;
    }

    public String selectDataLoggerProfile(String dataLoggerProfile) {
        click(dataLoggerProfileSelectButton);
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
                if (optionText.equals(dataLoggerProfile)) {
                    option.click();
                    return dataLoggerProfile;
                }
            }
        }
        throw new RuntimeException("Dropdown option not found: " + dataLoggerProfile);
    }


    public String fillUpDataLoggerProfile(String dataLoggerProfile) {
        return selectDataLoggerProfile(dataLoggerProfile);
    }

    public List<List<String>> fillUpRules(List<List<String>> rules) {
        Set<String> seenIds = new HashSet<>(); // আগের অপশনগুলো স্টোর করবে
        boolean newOptionsLoaded = true; // নতুন অপশন লোড হয়েছে কিনা চেক করব

        while (newOptionsLoaded) {
            newOptionsLoaded = false; // ধরে নিচ্ছি নতুন কিছু লোড হবে না
            List<WebElement> checkboxes = getElements(By.xpath("//div[@data-testid='shipment-rules-multi-value-left-scrollbar']//button[@role='checkbox']"));
            for (WebElement checkbox : checkboxes) {
                String checkboxId = checkbox.getAttribute("id");
                if (checkboxId != null && !seenIds.contains(checkboxId)) {
                    seenIds.add(checkboxId); // নতুন অপশন স্টোর করো
                    newOptionsLoaded = true; // নতুন কিছু লোড হয়েছে মানে আমরা স্ক্রল চালিয়ে যেতে পারব
                }
                scrollToElementNotSmooth(checkbox); // স্ক্রল করো
                sleep(500); // স্ক্রল লোডিংয়ের জন্য অপেক্ষা
            }

            // সব ID ক্লিক করা হয়েছে কিনা চেক করো
            boolean allClicked = true;
            for (List<String> rule : rules) {
                String ruleId = rule.get(1);
                if (!seenIds.contains(ruleId)) {
                    allClicked = false;
                    break;
                }
            }

            // যদি সব ক্লিক হয়ে যায়, তাহলে লুপ থেকে বের হয়ে যাও
            if (allClicked) {
                break;
            }
        }

        // সব পাওয়া গেলে, ক্লিক করো
        for (List<String> rule : rules) {
            click(xpath("//button[@id='" + rule.get(1) + "']"));
        }

        click(plusIconToAddAvailableRules);
        return rules;
    }
    public void scrollToElementNotSmooth(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    public void selectRouteInOriginAndDestination(String routeName, String routeId){
        sleep(5000);
        waitForVisibility(selectRouteButton).click();
        sleep(2000);
        selectDropdownWithSearchByText(routeName, routeId);
        //selectDropdownByText(selectRouteButtonOpitons, route);
    }

    public Map<String, Object> fillUpOriginAndDestination(
            String originAndDestinationType,
            String routeOrLocation,
            String routeName,
            String routeId,
            String isPredefined,
            String originLocation,
            String destinationLocation,
            String plannedDeparture,
            String plannedArrival
    ) {
        Map<String, Object> originAndDestination = new HashMap<>();

        if (Objects.equals(originAndDestinationType, "Inbound")) {
            click(xpath("//button[@data-testid='shipment-type-inbound-radio-button']"));
        } else if (Objects.equals(originAndDestinationType, "Outbound")) {
            click(xpath("//button[@data-testid='shipment-type-outbound-radio-button']"));
        }
        originAndDestination.put("originAndDestinationType", originAndDestinationType);

        if (Objects.equals(routeOrLocation, "Route")) {
//            selectRoute(route);
            selectRouteInOriginAndDestination(routeName, routeId);
        } else if (Objects.equals(routeOrLocation, "Location")) {
            // Todo: location select
//            selectLocationInOriginAndDestination(isPredefined, originLocation, destinationLocation);
            System.out.println("Pending");
        }
        originAndDestination.put("routeOrLocationChoice", routeOrLocation);
        originAndDestination.put("route", routeName);
        originAndDestination.put("isPredefined", isPredefined);
        originAndDestination.put("originLocationName", originLocation);
        originAndDestination.put("destinationLocationName", destinationLocation);

        // Todo: calender fillUp departure and arrival
//        fillupPlannedDepartureAndArrivalDates();
//        String departure = generateDateTime(LocalDateTime.now());
//        String arrival = generateDateTime(LocalDateTime.parse(departure, formatter));
//        setDataTimeForRouteButton("departure", departure);
//        setDataTimeForRouteButton("arrival", arrival);
//        originAndDestination.put("plannedDeparture", plannedDeparture);
//        originAndDestination.put("plannedArrival", plannedArrival);

        return originAndDestination;
    }

    public String selectAssetTypeAndShow(String assetType) {
        assetType = assetType.trim();
        if(!assetType.isEmpty()) {
            click(assetTypeSelectButton);
            selectDropdownByText(assetsInShipmentSectionDropdown, assetType);
            click(showButton);
        }
        return assetType;
    }

    public List<List<String>> fillUpAssetsInShipment(List<List<String>> assets) {
        List<List<String>> assetsInOrigin = new ArrayList<>();

        // Todo: select all assets by assetNames
        for (List<String> asset: assets) {
            //String assetType = asset.get(2);
            //String str = selectAssetTypeAndShow(assetType);
            //if(!str.equals(assetType))return null;
            getElement(xpath("//input[@data-testid='asset-search-input']")).clear();
            getElement(xpath("//input[@data-testid='asset-search-input']")).sendKeys(asset.get(0));
            sleep(2000);
            click(xpath("//button[@id='"+ asset.get(1) +"']"));
            assetsInOrigin.add(asset);
        }
        click(plusIconToAddAvailableAssets);
        sleep(2000);
        if(assets.get(0).size() == 4){
            for(int i = 0; i < assets.size(); i++){
                getElement(xpath("(//input[@placeholder='Tracking ID'])["+(i+1)+"]")).sendKeys(assets.get(i).get(3));
            }
        }
        return assetsInOrigin;
    }

    // Todo: fix it
    public String fillUpNotification(String emails) {
        // Todo: clear the input field
        // Todo: loop through the rules
        // Todo: enter each email separated by comma
        enterText(emailFieldInNotificationSection, emails);
        return emails;
    }


    public Map<String, Object> createShipment(
            String shipmentType,
            String shipmentName,
            String shipperName,
            String orderNumber,
            String carrierName,
            String modeOfTransport,
            String customerName,
            String recipientName,
            String organization,
            String organizationId,
            String dataLoggerProfile,
            List<Map<String, String>> rules,
            String originAndDestinationType,
            String routeOrLocation,
            String routeName,
            String routeId,
            String isPredefined,
            String originLocation,
            String destinationLocation,
            String plannedDeparture,
            String plannedArrival,
            List<List<String>> assets,
            String emails
    ) {
        Map<String, Object> shipmentData = new HashMap<>();
        shipmentData.put("shipmentType", selectShipmentType(shipmentType));
        shipmentData.putAll(fillUpNewShipmentsInitial(shipmentName, shipperName, orderNumber, carrierName, customerName, recipientName, organization, organizationId));
        sleep(5000);
        expendDataLoggerProfileInAddNewShipments();
        //shipmentData.put("dataLoggerProfile", fillUpDataLoggerProfile(dataLoggerProfile));

        //expendRulesInAddNewShipments();
        //shipmentData.put("rules", fillUpRules(rules));
        System.out.println(routeName);
        System.out.println(routeId);
        expendOriginAndDestinationInAndNewShipments();
        shipmentData.putAll(fillUpOriginAndDestination(originAndDestinationType, routeOrLocation, routeName, routeId, isPredefined,
                originLocation, destinationLocation, plannedDeparture, plannedArrival));
        if(routeOrLocation.equals("Location"))shipmentData.put("modeOfTransport", selectModeOfTransport(modeOfTransport));
        expendAssetsInShipmentInAddNewShipments();
        shipmentData.put("assets", fillUpAssetsInShipment(assets));

        expendNotificationInAddNewShipments();
        shipmentData.put("emails", fillUpNotification(emails));

        clickOnCreateButtonInCreateShipmentPopper();
        return shipmentData;
    }

    public void gotoShipmentDetailsPage(int row){
        getElement(xpath("//table/tbody/tr["+row+"]//*[contains(@class, 'lucide-expand')]/parent::a")).click();
    }
    public void gotoShipmentDetailsPageWithSearchByName(String shipmentName){
        int columnIndex = getColumnIndex("Name");
        click(xpath("//thead/tr/th["+(columnIndex + 1)+"]//*[contains(@class, 'cursor-pointer')]/parent::button"));
        sleep(2000);
        getElement(xpath("//input[@data-testid='searchInput']")).clear();
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(shipmentName);
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(Keys.ENTER);
        sleep(2000);
        List<WebElement> rows = getElements(xpath("//tbody//tr"));
        for(int i = 0; i < rows.size(); i++){
            String shipmentNameGetting = rows.get(i).findElement(xpath(".//td["+(columnIndex+1)+"]")).getText();
            System.out.println(shipmentNameGetting);
            if(shipmentNameGetting.equals(shipmentName)){
                int actionColumnIndex = getColumnIndex("Actions");
                click(xpath("//tbody/tr["+(i+1)+"]/td["+(actionColumnIndex + 1)+"]//a"));
                return;
            }
        }
    }
    // Todo: Go to shipmentDetails by shipmentName and shipmentID


    public void selectAllAssetsAndAdd() {
        waitForVisibility(selectAllAssetsFromAvailableAssets);
        click(selectAllAssetsFromAvailableAssets);
        clickOnPlusIconToAddAvailableAssets();
    }

    public void fillUpAssetsInShipment() {
        selectAllAssetsAndAdd();
    }

    // Todo: SelectAllAssetsInShipment


    // ================================================= checked all above ======================



    public void fillUpShipmentDetailsBasedOnCharLengthNeed(int length){
        clearAndEnterText(shipmentNameField, generateRandomString(length, ""));
        clearAndEnterText(shipperNameField, generateRandomString(length, ""));
        clearAndEnterText(orderNameField, generateRandomString(length, ""));
        clearAndEnterText(customerNameField, generateRandomString(length, ""));
        clearAndEnterText(recipientNameField, generateRandomString(length, ""));
    }

    public boolean verifyAllFieldsUnderDataLoggerInAddNewShipments() {
        expendDataLoggerProfileInAddNewShipments();
        return isDisplayedWithoutWait(dataLoggerProfileSelectButton);
    }

    public boolean verifyAllFieldsUnderRulesInAddNewShipments() {
        return isDisplayedWithoutWait(availableRulesSection) && isDisplayedWithoutWait(selectedRulesSection)
                && isDisplayedWithoutWait(selectNoneRulesFromAvailableRules)
                && isDisplayedWithoutWait(RemoveSelectedRulesFromSelectedRules) && isDisplayedWithoutWait(RemoveAllRulesFromSelectedRules);
    }

    public boolean verifyAllFieldsUnderOriginAndDestinationInAddNewShipments() {
        return isDisplayedWithoutWait(inboundRadioButton) && isDisplayedWithoutWait(outboundRadioButton)
                && isDisplayedWithoutWait(plannedDepartureDateTimeField) && isDisplayedWithoutWait(plannedArrivalDateTimeField);
    }

    public boolean verifyAllFieldsUnderAssetsInShipmentInAddNewShipments(String option) {
        click(assetTypeSelectButton);
        return  isDisplayedWithoutWait(showButton)
                && isDisplayedWithoutWait(availableAssetsSection) && isDisplayedWithoutWait(addedAssetsSection)
                && isDisplayedWithoutWait(selectNoneAssetsFromAvailableAssets)
                && isDisplayedWithoutWait(RemoveAllAssetsFromSelectedAssets) && isDisplayedWithoutWait(RemoveSelectedAssetsFromSelectedAssets);
    }

    public boolean verifyAllFieldsUnderNotificationInAddNewShipments() {
        expendNotificationInAddNewShipments();
        return isDisplayedWithoutWait(emailFieldInNotificationSection);
    }

    public boolean verifyPresenceOfCreateAndCancelButtonInAddNewShipments() {
        return isDisplayedWithoutWait(createButtonInAddNewShipment) && isDisplayedWithoutWait(cancelButtonInAddNewShipment);
    }

    public boolean verifyAllDefaultExpendedFieldsInAddNewShipments(String shipmentType){
        boolean modeOfTransportVisible = isDisplayedWithoutWait(modeOfTransportSelect);
        if(shipmentType.equals("Bulk Air Cargo"))modeOfTransportVisible = true;
        return isDisplayedWithoutWait(shipmentNameField) && isDisplayedWithoutWait(shipperNameField)
                && isDisplayedWithoutWait(orderNameField) && isDisplayedWithoutWait(carrierNameSelect)
                &&modeOfTransportVisible  && isDisplayedWithoutWait(customerNameField)
                && isDisplayedWithoutWait(recipientNameField) && isDisplayedWithoutWait(organizationSelectButton);
    }

    public boolean verifyCollapsedSectionsInAddNewShipments() {
        System.out.println(Objects.equals(getAttributeValue(dataLoggerProfileSectionButton, "aria-expanded"), "false"));
        return Objects.equals(getAttributeValue(dataLoggerProfileSectionButton, "aria-expanded"), "false")
                && Objects.equals(getAttributeValue(rulesSectionButton, "aria-expanded"), "false")
                && Objects.equals(getAttributeValue(originAndDestinationSectionButton, "aria-expanded"), "false")
                && Objects.equals(getAttributeValue(assetsInShipmentSectionButton, "aria-expanded"), "false")
                && Objects.equals(getAttributeValue(notificationSectionButton, "aria-expanded"), "false");
    }

    public boolean verifyShipmentsDataGridColumnHeaderTitle() {
        // Todo: get all the column header title
        System.out.println(extractTableHeadersByTable(shipmentsDataGridTable));
        System.out.println(extractTableBodyByTable(shipmentsDataGridTable));
        if (validateTableColumnSize(shipmentsDataGridTable, shipmentsDataGridTableColumnHeaderTitle.length) && validateTableColumnHeader(shipmentsDataGridTable, Arrays.asList(shipmentsDataGridTableColumnHeaderTitle))) {
            return true;
        };
        return false;

        // Todo: assert the list of predefined column header title with the retrieved titles
    }

    public boolean paginationExist(){
        return isDisplayed(checkPagination);
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

    public boolean isRequiredError(String ShipmentType, String routeOrLocation){
        sleep(5000);
        List<WebElement> errorLists = getElements(isRequireds);
        Set<String> errorMessage = new HashSet<>();
        for(int i = 0; i < errorLists.size(); i++){
            String str = errorLists.get(i).getText().trim();
            if(str.isEmpty())continue;
            errorMessage.add(str);
        }
        Set<String> requriedMsgs = new HashSet<>(requiredFields);
        if((ShipmentType.equals("Parcel") || ShipmentType.equals("Cell and Gene")) && routeOrLocation.equals("location"))requriedMsgs.add("Mode of Transport is required");
        System.out.println(requriedMsgs);
        System.out.println(errorMessage);
        return errorMessage.equals(requriedMsgs);
    }

    public boolean CreateToast() {
        System.out.println(isDisplayed(xpath("//*[text()='Shipment Created Successfully']")));
        return isDisplayed(By.xpath("//*[text()='Shipment Created Successfully']"));
    }
    public void deleteShipmentByName(String Name) {
        sleep(2000);
        if(getElement(By.xpath("//tbody//tr[1]//td[2]")).getText().equals(Name)){
            waitForVisibility(By.xpath("//tbody/tr[1]/td[3]//*[contains(@class, 'lucide lucide-trash2')]/ancestor::button")).click();
            sleep(2000);
            waitForVisibility(By.xpath("//button[normalize-space()='Yes, Delete']")).click();
        }
    }

    public void clickOnPlusIconToAddAvailableAssets() {
        click(plusIconToAddAvailableAssets);
    }

    public boolean checkMinimum3char(){
        fillUpShipmentDetailsBasedOnCharLengthNeed(2);
        // todo msg checking
        String x1 = getElement(shipmentNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x2 = getElement(shipperNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x3 = getElement(orderNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x4 = getElement(customerNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x5 = getElement(recipientNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String ending = " must be at least 3 characters";
        if(x1.startsWith("Shipment Name") && x2.startsWith("Shipper Name") && x3.startsWith("Order Number")
                && x4.startsWith("Customer Name") && x5.startsWith("Recipient Name") && x1.endsWith(ending)
                && x2.endsWith(ending) && x3.endsWith(ending) && x4.endsWith(ending) && x5.endsWith(ending)) return true;
        return false;
    }
    public boolean checkMax50char(){
        fillUpShipmentDetailsBasedOnCharLengthNeed(52);
        // todo msg checking
        String x1 = getElement(shipmentNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x2 = getElement(shipperNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x3 = getElement(orderNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x4 = getElement(customerNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String x5 = getElement(recipientNameField).findElement(By.xpath(".//following-sibling::p")).getText();
        String ending = " must be within 50 characters";
        if(x1.startsWith("Shipment Name") && x2.startsWith("Shipper Name") && x3.startsWith("Order Number")
                && x4.startsWith("Customer Name") && x5.startsWith("Recipient Name") && x1.endsWith(ending)
                && x2.endsWith(ending) && x3.endsWith(ending) && x4.endsWith(ending) && x5.endsWith(ending)) return true;
        return false;
    }
    public boolean clearAndEnterEmailInNotificationDuplicate() {
        clearAndEnterText(emailFieldInNotificationSection, "autotest@doglapanglobal.com, autotest@doglapanglobal.com");
        String str = getElement(emailFieldInNotificationSection).findElement(By.xpath(".//following-sibling::p")).getText();
        return str.equals("Duplicate email addresses are not allowed");
    }
    public boolean clearAndEnterEmailInNotificationInComplete() {
        clearAndEnterText(emailFieldInNotificationSection, "autotest@doglapanglobal");
        String str = getElement(emailFieldInNotificationSection).findElement(By.xpath(".//following-sibling::p")).getText();
        return str.equals("Contains invalid email address");
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
    public String getColumnName(int ColumnIndex){
        List<WebElement> headers = getElements(tableHeaders);
        return headers.get(ColumnIndex - 1).getText();
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
            List<String> items = Arrays.asList("Name", "Origin", "Destination", "Order Number", "Customer Name",
                    "Recipient Name", "Planned Departure", "Planned Arrival", "Created At", "Created By", "Last Modified At");//, "Last Modified By");
            for(String header : items){
                int columnIndex = getColumnIndex(header);
                row.add(cellInRow.get(columnIndex).getText().trim());
            }
            body.add(row);
        }
        return body;
    }

    public List<List<String>> getTable(){
        List<String> headers = Arrays.asList("Name", "Origin", "Destination", "Order Number", "Customer Name", "Recipient Name", "Planned Departure", "Planned Arrival", "Created At", "Created By", "Last Modified At");
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
            sleep(7000);
            if (HeadersWithDoubleButton.contains(header)) {
                findAndClickSearchButton(By.xpath("//thead//*[text()='" + header + "']//ancestor::th//button[1]"));
                boolean situation = isDisplayed(By.xpath("//*[@placeholder='Search']"));
                if (!situation) return -1;
                situation = isDisplayed(By.xpath("//*[@placeholder='Search']//following-sibling::span/*[1]"));
                if(!situation) return 0;
                else return 1;
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

    public boolean checkSearchOnColumn(String ColumnName, String demo_test, List<List<String>> table){
        int status = setHeaderIntoXpathForSearchButtonClick(ColumnName);
        if(status == 1){
            sleep(2000);
            getElement(By.xpath("//input[@placeholder='Search']")).clear();
            getElement(By.xpath("//input[@placeholder='Search']")).sendKeys(demo_test);
            sleep(2000);
            getElement(By.xpath("//input[@placeholder='Search']//following-sibling::span//*[@stroke-linecap='round'][2]")).click();
            sleep(2000);
            List<List<String>> tableData = getTable();
            System.out.println(tableData);
            System.out.println(table);
            getElement(By.xpath("//span[text()='"+ColumnName+" | "+demo_test+"']/following-sibling::*[contains(@class, 'lucide-circle-x')]")).click();
            return tableData.equals(table);
        }
        else return true;
    }
    public void clickSort(String columnName) {
        columnName = columnName.replace("'", "");
        int columnIndex = getColumnIndex(columnName);
        //System.out.println(columnIndex);
        List<String> onlySortButtonContains = Arrays.asList("Planned Departure", "Planned Arrival", "Created At", "Last Modified At");
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

    public boolean checkStatusInAssetDetails(String path){
        List<WebElement> mx = getElements(xpath(path));
        return mx.size() > 0;
    }
    public boolean validateCreatedData(Map<String, Object> CreatedData){
        String shipmentName = CreatedData.get("shipmentName").toString();
        String shipperName = CreatedData.get("shipperName").toString();
        String orderNumber = CreatedData.get("orderNumber").toString();
        String carrierName = CreatedData.get("carrierName").toString();
        String modeOfTransport = CreatedData.get("modeOfTransport").toString();
        String customerName = CreatedData.get("customerName").toString();
        String recipientName = CreatedData.get("recipientName").toString();
        String organization = CreatedData.get("organization").toString();
        String dataLoggerProfile = CreatedData.get("dataLoggerProfile").toString();
        String rules = CreatedData.get("rules").toString();
        //String
        return true;
    }
    List<String> haveToHoverForData = Arrays.asList("Status","Direction");
    public String getCellDataFromGrid(int RowNumber, String ColumnName){
        int columnIndex = getColumnIndex(ColumnName) + 1;
        if(haveToHoverForData.contains(ColumnName)){
            hoverOverElement(xpath("//tbody/tr["+RowNumber+"]//td["+columnIndex+"]//*[@data-state]"));
            String text = getElement(xpath("//div[@data-radix-popper-content-wrapper]")).getText();
            click(xpath("//tbody/tr["+RowNumber+"]//td["+(getColumnIndex("Name")+1)+"]"));
            sleep(2000);
            click(xpath("//*[contains(@class, 'lucide-x')]//following-sibling::*[text()='Close sidebar']/ancestor::button"));
            return text;
        }
        return getElement(xpath("//tbody/tr["+RowNumber+"]//td["+columnIndex+"]")).getText();
    }
    public String getCellDataFromGrid(int RowNumber, int ColumnNumber){
        String ColumnName = getColumnName(ColumnNumber);
        if(haveToHoverForData.contains(ColumnName)){
            hoverOverElement(xpath("//tbody/tr["+RowNumber+"]//td["+ColumnNumber+"]//*[@data-state]"));
            String text = getElement(xpath("//div[@data-radix-popper-content-wrapper]")).getText();
            click(xpath("//tbody/tr["+RowNumber+"]//td["+(getColumnIndex("Name")+1)+"]"));
            sleep(2000);
            click(xpath("//*[contains(@class, 'lucide-x')]//following-sibling::*[text()='Close sidebar']/ancestor::button"));
            return text;
        }
        return getElement(xpath("//tbody/tr["+RowNumber+"]//td["+ColumnNumber+"]")).getText();
    }
    public Map<String, String> getDataByRowFromGrid(int RowNumber){
        Map<String, String> shipmentGridData = new HashMap<>();
        List<WebElement> headersList = getElements(tableHeaders);
        for(int i = 0; i < headersList.size(); i++){
            String header = headersList.get(i).getText();
            if(header.equals("Actions")){
                String url = getElement(xpath("//tbody/tr["+RowNumber+"]//td["+(i+1)+"]//a")).getAttribute("href");
                String shipmentID = url.substring(url.lastIndexOf('/') + 1);
                shipmentGridData.put("shipmentId", shipmentID);
            }
            else{
                String str = header;
                if(str.equals("Name"))str = "shipmentName";
                if(str.equals("Planned Departure"))str = "plannedDepartureDate";
                if(str.equals("Planned Arrival"))str = "plannedArrivalDate";
                if(str.equals("Shipment Type"))str= "shipmentType";
                if(str.equals("Status"))str = "shipmentStatus";
                if(str.equals("Direction"))str = "shipmentDirection";
                if(str.equals(""))str = "shipperName";
                str = "orderNumber";
                str = "travelMode";
                str = "customerName";
                str = "recipientName";
                str = "carrierName";
                str = "originLocationName";
                str = "destinationLocationName";
                str = "routeName";
                str = "createdAt";
                str = "createdBy";
                str = "updatedAt";
                str = 
                shipmentGridData.put(str, getCellDataFromGrid(RowNumber, i + 1));
            }
        }
        return shipmentGridData;
    }
    public void clickGeneralInfo(){
        click(generalInfoButton);
    }
    public Map<String, Object> getDataFromGeneralInfo(){
        List<WebElement> listOfDataFromGeneralInfo = getElements(xpath("//div[@class='flex items-start justify-start gap-6 rounded bg-white p-2 pl-0']"));
        Map<String, Object> mapOfDataFromGeneralInfo = new HashMap<>();
        for(int i = 0; i < listOfDataFromGeneralInfo.size(); i++){
            String header = listOfDataFromGeneralInfo.get(i).findElement(xpath("./div")).getText();

            if(header.equals("Rules")){
                continue;
                /*
                boolean displayed = isDisplayed(xpath("//div[@class='flex items-start justify-start gap-6 rounded bg-white p-2 pl-0']//a"));
                List<String> rules = new ArrayList<>();
                String text = listOfDataFromGeneralInfo.get(i).findElement(xpath("./div[2]")).getText();
                if(displayed){
                    text = text.substring(0, text.lastIndexOf(','));
                }
                rules.add(text);
                if(displayed) {
                    hoverOverElement(xpath("//div[@class='flex items-start justify-start gap-6 rounded bg-white p-2 pl-0']//a"));
                    sleep(2000);
                    List<WebElement> options = getElements(xpath("//div[@data-radix-popper-content-wrapper]//p"));
                    for (int j = 0; j < options.size(); j++) rules.add(options.get(j).getText());
                }
                mapOfDataFromGeneralInfo.put("Rules", rules);
            */
            }

            else{

            System.out.println(header);
                String data = listOfDataFromGeneralInfo.get(i).findElement(xpath("./p")).getText();
                mapOfDataFromGeneralInfo.put(header, data);

            }


        }
        return mapOfDataFromGeneralInfo;
    }
    public int getRowNumberByName(String shipmentName){
        int columnNumber = getColumnIndex("Name");
        List<WebElement> data = getElements(xpath("//tbody/tr"));
        for(int i = 0; i < data.size(); i++){
            String name = data.get(i).findElement(xpath(".//td["+(columnNumber+1)+"]")).getText();
            if(name.equals(shipmentName)){
                return i + 1;
            }
        }
        return -1;
    }
    public String getShipmentID(int rowNumber){
        int columnNumber = getColumnIndex("Actions");
        String url = getElement(xpath("//tbody/tr["+rowNumber+"]//td["+(columnNumber+1)+"]//a")).getAttribute("href");
        String shipmentID = url.substring(url.lastIndexOf('/') + 1);
        return shipmentID;
    }
    public String getShipmentID(String ShipmentName){
        int rowNumber = getRowNumberByName(ShipmentName);
        return getShipmentID(rowNumber);
    }
        public boolean deleteIconExist(String Section){
        String trashBox = ".//ancestor::div[contains(@class, 'rounded-sm')]//*[contains(@class, 'lucide-trash2')]";
        if(Section.equals("Rules")){
            listOfRulesOrAssets("Rules", 1);
            int len = getElement(selectedRulesSection).findElements(By.xpath(trashBox)).size();
            //System.out.println(len);
            if(len > 0)return true;
            else return false;
        }
        else if(Section.equals("Assets in Shipment")){
            listOfRulesOrAssets("Assets", 1);
            int len = getElement(selectedAssetsSection).findElements(By.xpath(trashBox)).size();
            //System.out.println(len);
            if(len > 0)return true;
            else return false;
        }
        return false;
    }
    public void listOfRulesOrAssets(String RulesOrAssets, int number){
        if(RulesOrAssets.equals("Rules")){
            List<WebElement> listofrules = getElements(xpath("//*[@data-testid='shipment-rules-multi-value-left-scrollbar']//button"));
            for(int i = 0; i < number; i++){
                listofrules.get(i).click();
                click(plusIconToAddAvailableRules);
            }
        }
        else if(RulesOrAssets.equals("Assets")){
            List<WebElement>listofassets = getElements(xpath("//*[@data-testid='shipment-assets-multi-value-left-scrollbar']//button"));
            for(int i = 0; i < number; i++){
                listofassets.get(i).click();
                clickOnPlusIconToAddAvailableAssets();
            }
        }
    }
    public void clickStatusButton(){
        click(statusButton);
    }
    public boolean validateData(Map<String, Object> detailsFromAPI, Map<String, Object> dataFromGeneralInfo, Map<String, Object> shipmentData){
        for (String key : shipmentData.keySet()) {
            System.out.println("Key: " + key + ", Value: " + shipmentData.get(key));
            if(key.equals("orderNumber")){
                String validateDataValue = (String) shipmentData.get(key);
                String detailsFromAPIValue = (String) shipmentData.get(key);
                String dataFromGeneralInfoValue = (String) dataFromGeneralInfo.get("Order Number");
                if(!(validateDataValue.equals(dataFromGeneralInfoValue)
                        && detailsFromAPIValue.equals(dataFromGeneralInfoValue))){
                    System.out.println("Data is not matched at "+ "Order Number");
                }
            }
            //else if(key.equals(""))
        }
        return true;
    }
    public void clickShipmentPage(){
        click(shipmentsPageLink);
    }
    public void clickOnForceCompleteByName(String shipmentName){
        int rowNumber = getRowNumberByName(shipmentName);
        System.out.println(rowNumber);
        int columnNumber = getColumnIndex("Actions");
        System.out.println(columnNumber);
        hoverOverElement(xpath("//tbody/tr["+rowNumber+"]/td["+(columnNumber + 1)+"]//div[@class='flex w-full items-center gap-4']//div[1]"));
        click(xpath("//tbody/tr["+rowNumber+"]/td["+(columnNumber + 1)+"]//div[@class='flex w-full items-center gap-4']//div[1]//button"));
        sleep(2000);
        waitForVisibility(By.xpath("//button[normalize-space()='Yes, Complete']")).click();
    }
    public boolean checkStatusActive(){
        return isDisplayed(xpath(status+activeDataState)) && isDisplayed(xpath(assetsInMap+inactiveDataState))
                && isDisplayed(xpath(generalInfo+inactiveDataState));
    }
    public boolean checkShipmentBannerAndName(String shipmentName){
        return isDisplayed(xpath(shipmentsTextInDetailsPage+"//parent::*" +
                "//following-sibling::*[contains(@class, 'lucide-chevron-right')]" +
                "//following-sibling::*[text()='"+shipmentName+"']//following-sibling::" +
                "*/*[contains(@class, 'lucide-share2')]/parent::button//following-sibling::button"));
    }
    public boolean checkShipmentBannerAndNameWithPendingStatus(String shipmentName){
        return isDisplayed(xpath(shipmentsTextInDetailsPage+"//parent::*" +
                "//following-sibling::*[contains(@class, 'lucide-chevron-right')]" +
                "//following-sibling::*[text()='"+shipmentName+"']//following-sibling::" +
                "*/*[contains(@class, 'lucide-share2')]/parent::button//following-sibling::button" +
                "//following-sibling::*/*[text()='Pending']/following-sibling::*[contains(@class, 'auto block')]"));
    }
    public String assetCounts(){
        return getElement(xpath("//*[@data-testid='asset-count']")).getText();
    }
    public boolean checkAssetCount(List<String> assets){
        String assetCountExpected = "";
        if(assets.size() < 2){
            assetCountExpected = "1 Asset";
        }
        else assetCountExpected = assets.size()+" Assets";
        return assetCounts().equals(assetCountExpected);
    }
    public boolean checkShipmentsAssetsWhenStatusIsPending(List<String> assets){
        String assetCountExpected = "";
        if(assets.size() < 2){
            assetCountExpected = "1 Asset";
        }
        else assetCountExpected = assets.size()+" Assets";
        boolean assetCountStatus = assetCounts().equals(assetCountExpected);
        if(!assetCountStatus)return false;
        return true;
    }
    public boolean checkAssetsMatchAtButton(List<String> assets){
        click(xpath("//*[@data-testid='asset-count']//following-sibling::*/*[contains(@class, 'lucide-chevron-right')]/parent::button"));
        List<WebElement> assetsNameElementGet = getElements(xpath("//div[@data-radix-popper-content-wrapper and @dir='ltr' and contains(@style, 'position: fixed')]//*[@role='menuitem']"));
        Set<String> assetsNameGet = new HashSet<>();
        for (WebElement webElement : assetsNameElementGet) {
            assetsNameGet.add(webElement.getText());
        }
        Set<String>expectedAssets = new HashSet<>(assets);
        sleep(2000);
        assetsNameElementGet.get(0).click();
        return expectedAssets.equals(assetsNameGet);
    }
    public boolean checkAssetBoxSnapshot(List<String> assets){
        int activeAsset = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-50')]")).size();
        int inactiveAsset = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-30')]")).size();
        if(activeAsset != 1 || (activeAsset + inactiveAsset) != assets.size())return false;
        String activeAssetName = getElement(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-50')]" +
                "//*[@class='w-[230px] text-left text-lg text-doglapan-blue-50 font-semibold']//p")).getText();
        List<WebElement> inActiveAssetNameListElement = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-30')]" +
                "//*[@class='w-[230px] text-left text-lg text-doglapan-blue-50 font-light']//p"));
        List<String> inActiveAssetNameList = new ArrayList<>();
        for(WebElement webElement : inActiveAssetNameListElement){
            inActiveAssetNameList.add(webElement.getText());
        }
        Set<String> totalAssets = new HashSet<>();
        totalAssets.add(activeAssetName);
        totalAssets.addAll(new HashSet<>(inActiveAssetNameList));
        Set<String> expectedTotalAssets = new HashSet<>(assets);
        if(!totalAssets.equals(expectedTotalAssets))return false;
        String snapshotString = getElement(xpath("//*[contains(@class," +
                " 'h-shipment-asset-container-height')]//*[@class='w-full truncate whitespace-pre']")).getText();
        return snapshotString.equals(activeAssetName);
    }
    public boolean checkSnapshotDownTablesInStatus(String shipmentType){
        boolean chainOfCustodyStatus = true;
        if(shipmentType.equals("Cell and Gene") || shipmentType.equals("Parcel"))chainOfCustodyStatus = isDisplayed(xpath(chainOfCustody+inactiveDataState));
        boolean isProgressActive = isDisplayed(xpath(progressButton+activeDataState)) && isDisplayed(xpath(mapView+inactiveDataState))
                && chainOfCustodyStatus && isDisplayed(xpath(environmentalConditions+inactiveDataState));
        click(xpath(mapView));
        boolean isMapViewActive = isDisplayed(xpath(progressButton+inactiveDataState)) && isDisplayed(xpath(mapView+activeDataState))
                && chainOfCustodyStatus && isDisplayed(xpath(environmentalConditions+inactiveDataState));
        if(shipmentType.equals("Cell and Gene") || shipmentType.equals("Parcel")){
            click(xpath(chainOfCustody));
            boolean temp = isDisplayed(xpath(progressButton+inactiveDataState)) && isDisplayed(xpath(mapView+inactiveDataState))
                    && isDisplayed(xpath(chainOfCustody+activeDataState)) && isDisplayed(xpath(environmentalConditions+inactiveDataState));
            if(!temp)return temp;
        }
        click(xpath(environmentalConditions));
        boolean isEnvironmentalConditionsActive = isDisplayed(xpath(progressButton+inactiveDataState)) && isDisplayed(xpath(mapView+inactiveDataState))
                && chainOfCustodyStatus && isDisplayed(xpath(environmentalConditions+activeDataState));
        return isProgressActive && isMapViewActive && isEnvironmentalConditionsActive;
    }
    public boolean checkStatusSnapshot(String shipmentName, List<String> assets, String shipmentType){
        boolean isStatusActive = isDisplayed(xpath(status+activeDataState)) && isDisplayed(xpath(assetsInMap+inactiveDataState))
                && isDisplayed(xpath(generalInfo+inactiveDataState));
        boolean banner = checkShipmentBannerAndName(shipmentName);
        boolean assetCount = checkAssetCount(assets);
        boolean assetsMatchAtButton = checkAssetsMatchAtButton(assets);
        boolean assetBoxSnapshot = checkAssetBoxSnapshot(assets);
        boolean snapshotDownTablesInStatus = checkSnapshotDownTablesInStatus(shipmentType);
        return isStatusActive && banner && assetCount && assetsMatchAtButton && assetBoxSnapshot && snapshotDownTablesInStatus;
    }
    public boolean checkAssetsInMapSnapshot(List<String> assets){
        boolean isAssetsInMapActive = isDisplayed(xpath(status+inactiveDataState)) && isDisplayed(xpath(assetsInMap+activeDataState))
                && isDisplayed(xpath(generalInfo+inactiveDataState));
        int activeAsset = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-50')]")).size();
        int inactiveAsset = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-30')]")).size();
        if(activeAsset != 1 || (activeAsset + inactiveAsset) != assets.size() + 1)return false;
        String allAssets = getElement(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-50')]" +
                "//*[@class='w-[230px] text-left text-lg text-doglapan-blue-50 font-semibold']//p")).getText();
        if(!allAssets.equals("All Assets"))return false;
        List<WebElement> inActiveAssetNameListElement = getElements(xpath("//*[@data-radix-scroll-area-viewport]//button[contains(@class, 'border-doglapan-blue-30')]" +
                "//*[@class='w-[230px] text-left text-lg text-doglapan-blue-50 font-light']//p"));
        Set<String> inActiveAssetNameList = new HashSet<>();
        for(WebElement webElement : inActiveAssetNameListElement){
            inActiveAssetNameList.add(webElement.getText());
        }
        Set<String> expectedAssets = new HashSet<>(assets);
        if(!expectedAssets.equals(inActiveAssetNameList))return false;
        boolean assetCount = checkAssetCount(assets);
        boolean assetsMatchAtButton = checkAssetsMatchAtButton(assets);
        return isAssetsInMapActive && assetCount && assetsMatchAtButton;
    }
    public boolean checkGeneralInfoSnapshot(Map<String, Object> shipmentData){
        boolean isGeneralInfoActive = isDisplayed(xpath(status+inactiveDataState)) && isDisplayed(xpath(assetsInMap+inactiveDataState))
                && isDisplayed(xpath(generalInfo+activeDataState));
        List<WebElement> generalInfoHeadersElements = getElements(xpath("//div[@class='flex items-start justify-start gap-6 rounded bg-white p-2 pl-0']//div"));
        Set<String> getGeneralInfoHeaders = new HashSet<>();
        for(WebElement webElement : generalInfoHeadersElements){
            getGeneralInfoHeaders.add(webElement.getText());
        }
        Set<String> expectedGeneralInfoHeaders = Set.of("Shipment Name", "Planned Departure", "Planned Arrival", "Shipment Type", "Order Number", "Shipper Name", "Carrier Name",
                "Customer Name", "Recipient Name", "Organization", "Mode of Transport", "Shipment Direction", "Route", "Rules", "Origin", "Destination", "Number of Assets", "Created At",
                "Created By", "Last Modified At", "Last Modified By");
        if(!getGeneralInfoHeaders.equals(expectedGeneralInfoHeaders))return false;
        Map<String, Object> dataFromGeneralInfo = getDataFromGeneralInfo();
        List<List<String>> assets = (List<List<String>>)shipmentData.get("assets");
        int assetsSize = Integer.parseInt((String)dataFromGeneralInfo.get("Number of Assets"));
        if(shipmentData.get("orderNumber").equals(dataFromGeneralInfo.get("Order Number")) &&
                shipmentData.get("shipperName").equals(dataFromGeneralInfo.get("Shipper Name")) &&
                shipmentData.get("customerName").equals(dataFromGeneralInfo.get("Customer Name")) &&
                shipmentData.get("shipmentType").equals(dataFromGeneralInfo.get("Shipment Type")) &&
                shipmentData.get("orderNumber").equals(dataFromGeneralInfo.get("Order Number")) &&
                shipmentData.get("route").equals(dataFromGeneralInfo.get("Route")) &&
                shipmentData.get("originLocationName").equals(dataFromGeneralInfo.get("Origin")) &&
                assets.size() == assetsSize &&
                shipmentData.get("carrierName").equals(dataFromGeneralInfo.get("Carrier Name")) &&
                shipmentData.get("organization").equals(dataFromGeneralInfo.get("Organization")) &&
                shipmentData.get("destinationLocationName").equals(dataFromGeneralInfo.get("Destination")) &&
                shipmentData.get("recipientName").equals(dataFromGeneralInfo.get("Recipient Name")) &&
                shipmentData.get("shipmentName").equals(dataFromGeneralInfo.get("Shipment Name")) &&
                isGeneralInfoActive)return true;
        else return false;
    }
    public boolean snapShotOfShipmentDetails(Map<String, Object> shipmentData){
        List<List<String>> assets = (List<List<String>>) shipmentData.get("assets");
        List<String> assetsName = new ArrayList<>();
        for (List<String> asset : assets) {
            assetsName.add(asset.get(0));
        }
        boolean statusSnapshot = checkStatusSnapshot((String) shipmentData.get("shipmentName"), assetsName, (String) shipmentData.get("shipmentType"));
        click(xpath(assetsInMap));
        boolean assetsInMapSnapshot = checkAssetsInMapSnapshot(assetsName);
        click(xpath(generalInfo));
        boolean generalInfoSnapshot = checkGeneralInfoSnapshot(shipmentData);
        System.out.println(statusSnapshot);
        System.out.println(assetsInMapSnapshot);
        System.out.println(generalInfoSnapshot);
        return statusSnapshot && assetsInMapSnapshot && generalInfoSnapshot;
    }
    public void clickOnShipmentName(String shipmentName){
        int columnIndex = getColumnIndex("Name");
        click(xpath("//thead/tr/th["+(columnIndex + 1)+"]//*[contains(@class, 'cursor-pointer')]/parent::button"));
        sleep(2000);
        getElement(xpath("//input[@data-testid='searchInput']")).clear();
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(shipmentName);
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(Keys.ENTER);
        sleep(2000);
        click(xpath("//*[text()='"+shipmentName+"']/parent::button"));
    }
    public Map<String, Object> getShipmentCardData(){
        Map<String, Object> data = new HashMap<>();
        boolean isCardDisplay = isDisplayed(xpath("//*[contains(@class, 'fixed bottom-0 right')]"));
        if(!isCardDisplay)return data;
        data.put("shipmentName", getElement(xpath("//*[@class='whitespace-pre-wrap text-lg font-light text-doglapan-blue-50']")).getText());
        List<WebElement> firstPortion = getElements(xpath("//*[@class='flex h-full items-center text-xs font-semibold text-doglapan-grey-80']"));
        Set<String> expectedFirstPortionText = Set.of("Status", "Actual Departure", "Actual Arrival");
        Set<String> gotFirstPortionText = new HashSet<>();
        for(WebElement webElement : firstPortion){
            gotFirstPortionText.add(webElement.getText());
        }
        if(!expectedFirstPortionText.equals(gotFirstPortionText))return null;
        Set<String> assets = new HashSet<>();
        List<WebElement> assetsElement = getElements(xpath("//*[@class='w-full text-xs text-doglapan-blue-50']"));
        for(WebElement webElement : assetsElement){
            assets.add(webElement.getText());
        }
        data.put("assets", assets);
        List<WebElement> got2ndPortionElements = getElements(xpath("//*[@class='whitespace-pre-wrap text-xs font-semibold text-doglapan-grey-80']"));
        Set<String> got2ndPortionText = new HashSet<>();
        for(WebElement webElement : got2ndPortionElements){
            got2ndPortionText.add(webElement.getText());
        }
        Set<String> S2ndPortionText = Set.of("Shipment Direction", "Origin", "Destination",
                "Created At", "Created By", "Last Modified At", "Last Modified By");
        if(!S2ndPortionText.equals(got2ndPortionText))return null;
        data.put("origin", getElement(xpath("//p[text()='Origin']//following-sibling::p[1]")).getText());
        data.put("destination", getElement(xpath("//p[text()='Destination']//following-sibling::p[1]")).getText());
        return data;
    }
    public boolean compareShipmentDataAndShipmentCardData(Map<String, Object> shipmentData, Map<String, Object> shipmentCardData) {
        List<List<String>> assets = (List<List<String>>) shipmentData.get("assets");
        Set<String> assetsName = new HashSet<>();
        for (List<String> asset : assets) {
            assetsName.add(asset.get(0));
        }
        Object shipmentCardAssets = shipmentCardData.get("assets");
        if (!(shipmentCardAssets instanceof Set<?>)) {
            return false; // Avoid ClassCastException
        }
        Set<String> shipmentCardAssetsSet = new HashSet<>();
        for (Object obj : (Set<?>) shipmentCardAssets) {
            if (obj instanceof String) {
                shipmentCardAssetsSet.add((String) obj);
            } else {
                return false; // If any element is not a String, return false
            }
        }
        return assetsName.equals(shipmentCardAssetsSet) &&
                shipmentData.get("originLocationName").equals(shipmentCardData.get("origin")) &&
                shipmentData.get("destinationLocationName").equals(shipmentCardData.get("destination")) &&
                shipmentData.get("shipmentName").equals(shipmentCardData.get("shipmentName"));
    }
    public void clickFullDetailsPage(){
        click(xpath("//*[text()='View Full Detail']/parent::a"));
    }
    public void clickButton(String path){
        scrollToElement(xpath(path));
        click(xpath(path));

    }
    public boolean isDisplayed(String path){
        return isDisplayed(xpath(path));
    }
    public Integer getCargoTemp() {
        return extractTemperature("(//*[text()='Cargo Temperature']//ancestor::div/p)[2]");
    }

    public Integer getAmbientTemp() {
        return extractTemperature("(//*[text()='Ambient Temperature']//ancestor::div/p)[2]");
    }

    public Integer getCargoHumidity() {
        return extractHumidity("(//*[text()='Cargo Humidity']//ancestor::div/p)[2]");
    }

    public Integer getAmbientHumidity() {
        return extractHumidity("(//*[text()='Ambient Humidity']//ancestor::div/p)[2]");
    }

    // Helper method for temperature extraction
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

    public void clickEnvironmentalConditions(){
        clickButton(environmentalConditions);
    }
    public void clickAmbientTemp(){
        clickButton(ambientTempPath);
    }
    public void clickCargoHumidity(){
        clickButton(cargoHumidPath);
    }
    public void clickAmbientHumidity(){
        clickButton(ambientHumidPath);
    }
    public boolean checkStatusOfBannerInShipmentDetailsPage(String shipmentName, String Status){
        return isDisplayed(xpath(shipmentsTextInDetailsPage+"//parent::*" +
                "//following-sibling::*[contains(@class, 'lucide-chevron-right')]" +
                "//following-sibling::*[text()='"+shipmentName+"']//following-sibling::" +
                "*/*[contains(@class, 'lucide-share2')]/parent::button//following-sibling::button" +
                "//following-sibling::*/*[text()='"+Status+"']"));
    }
    public void clickTableView(){
        clickButton("//button[text()='Table View']");
    }
    String cargoTempPath = "//*[text()='Cargo Temperature']//ancestor::button";
    String ambientTempPath = "//*[text()='Ambient Temperature']//ancestor::button";
    String cargoHumidPath = "//*[text()='Cargo Humidity']//ancestor::button";
    String ambientHumidPath = "//*[text()='Ambient Humidity']//ancestor::button";
    public boolean checkdownTableEnvironmentalConditionsOfCargoTemp(String deviceId, Integer cargoTemp, long time, String Address, String location){
        Integer topStatus1 = extractTemperature("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(!topStatus1.equals(cargoTemp))return false;
        String topStatus2 =  getElement(xpath("//*[@class='mx-2 text-xs font-normal text-doglapan-grey-80']")).getText();
        String result = topStatus2.replace("Last updated at ", "");
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String formattedDate = sdf.format(date);
        if(!formattedDate.equals(result))return false;
        if(getElement(xpath("//table/thead/tr[1]/th[1]")).getText().equals("DL ID")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[1]")).getText().equals(deviceId))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[2]")).getText().equals("Cargo Temperature")){
            String temp = Integer.toString(cargoTemp);
            String extractTemp = extractTemperature("//table/tbody/tr[1]/td[2]").toString();
            if(!extractTemp.equals(temp))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[3]")).getText().equals("Event Time")){
            date = new Date(time * 1000L);
            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            formattedDate = sdf.format(date);
            if(!getElement(xpath("//table/tbody/tr[1]/td[3]")).getText().equals(formattedDate))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[4]")).getText().equals("Address")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[4]")).getText().equals(Address))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[5]")).getText().equals("Location")){
            System.out.println(getElement(xpath("//table/tbody/tr[1]/td[5]")).getText());
            System.out.println(location);
            System.out.println(getElement(xpath("//table/tbody/tr[1]/td[5]")).getText().equals(location));
            if(!getElement(xpath("//table/tbody/tr[1]/td[5]")).getText().equals(location))return false;
        }
        if(isDisplayed(cargoTempPath+activeDataState) && isDisplayed(ambientTempPath + inactiveDataState)
                && isDisplayed(cargoHumidPath + inactiveDataState) && isDisplayed(ambientHumidPath+inactiveDataState))return true;
        else return false;
    }
    public boolean checkdownTableEnvironmentalConditionsOfAmbientTemp(String deviceId, Integer ambientTemp, long time, String Address, String location){
        Integer topStatus1 = extractTemperature("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        System.out.println(topStatus1);
        System.out.println(ambientTemp);
        System.out.println(topStatus1.equals(ambientTemp));
        if(!topStatus1.equals(ambientTemp))return false;
        String topStatus2 =  getElement(xpath("//*[@class='mx-2 text-xs font-normal text-doglapan-grey-80']")).getText();
        String result = topStatus2.replace("Last updated at ", "");
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String formattedDate = sdf.format(date);
        if(!formattedDate.equals(result))return false;
        if(getElement(xpath("//table/thead/tr[1]/th[1]")).getText().equals("DL ID")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[1]")).getText().equals(deviceId))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[2]")).getText().equals("Ambient Temperature")){
            String temp = Integer.toString(ambientTemp);
            String extractTemp = extractTemperature("//table/tbody/tr[1]/td[2]").toString();
            if(!extractTemp.equals(temp))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[3]")).getText().equals("Event Time")){
            date = new Date(time * 1000L);
            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            formattedDate = sdf.format(date);
            if(!getElement(xpath("//table/tbody/tr[1]/td[3]")).getText().equals(formattedDate))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[4]")).getText().equals("Address")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[4]")).getText().equals(Address))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[5]")).getText().equals("Location")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[5]")).getText().equals(location))return false;
        }
        if(isDisplayed(cargoTempPath+inactiveDataState) && isDisplayed(ambientTempPath + activeDataState)
                && isDisplayed(cargoHumidPath + inactiveDataState) && isDisplayed(ambientHumidPath+inactiveDataState))return true;
        else return false;
    }
    public boolean checkdownTableEnvironmentalConditionsOfCargoHumidity(String deviceId, Integer cargoHumidity, long time, String Address, String location){
        Integer topStatus1 = extractHumidity("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(!topStatus1.equals(cargoHumidity))return false;
        String topStatus2 =  getElement(xpath("//*[@class='mx-2 text-xs font-normal text-doglapan-grey-80']")).getText();
        String result = topStatus2.replace("Last updated at ", "");
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String formattedDate = sdf.format(date);
        if(!formattedDate.equals(result))return false;
        if(getElement(xpath("//table/thead/tr[1]/th[1]")).getText().equals("DL ID")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[1]")).getText().equals(deviceId))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[2]")).getText().equals("Cargo Humidity")){
            String temp = Integer.toString(cargoHumidity);
            String extractTemp = extractHumidity("//table/tbody/tr[1]/td[2]").toString();
            if(!extractTemp.equals(temp))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[3]")).getText().equals("Event Time")){
            date = new Date(time * 1000L);
            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            formattedDate = sdf.format(date);
            if(!getElement(xpath("//table/tbody/tr[1]/td[3]")).getText().equals(formattedDate))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[4]")).getText().equals("Address")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[4]")).getText().equals(Address))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[5]")).getText().equals("Location")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[5]")).getText().equals(location))return false;
        }
        if(isDisplayed(cargoTempPath+inactiveDataState) && isDisplayed(ambientTempPath + inactiveDataState)
                && isDisplayed(cargoHumidPath + activeDataState) && isDisplayed(ambientHumidPath+inactiveDataState))return true;
        else return false;
    }
    public boolean checkdownTableEnvironmentalConditionsOfAmbientHumidity(String deviceId, Integer ambientHumidity, long time, String Address, String location){
        Integer topStatus1 = extractHumidity("//*[@class='text-xl font-normal text-doglapan-blue-50']");
        if(!topStatus1.equals(ambientHumidity))return false;
        String topStatus2 =  getElement(xpath("//*[@class='mx-2 text-xs font-normal text-doglapan-grey-80']")).getText();
        String result = topStatus2.replace("Last updated at ", "");
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String formattedDate = sdf.format(date);
        if(!formattedDate.equals(result))return false;
        if(getElement(xpath("//table/thead/tr[1]/th[1]")).getText().equals("DL ID")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[1]")).getText().equals(deviceId))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[2]")).getText().equals("Ambient Humidity")){
            String temp = Integer.toString(ambientHumidity);
            String extractTemp = extractHumidity("//table/tbody/tr[1]/td[2]").toString();
            if(!extractTemp.equals(temp))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[3]")).getText().equals("Event Time")){
            date = new Date(time * 1000L);
            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            formattedDate = sdf.format(date);
            if(!getElement(xpath("//table/tbody/tr[1]/td[3]")).getText().equals(formattedDate))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[4]")).getText().equals("Address")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[4]")).getText().equals(Address))return false;
        }
        if(getElement(xpath("//table/thead/tr[1]/th[5]")).getText().equals("Location")){
            if(!getElement(xpath("//table/tbody/tr[1]/td[5]")).getText().equals(location))return false;
        }
        if(isDisplayed(cargoTempPath+inactiveDataState) && isDisplayed(ambientTempPath + inactiveDataState)
                && isDisplayed(cargoHumidPath + inactiveDataState) && isDisplayed(ambientHumidPath+activeDataState))return true;
        else return false;
    }
    public boolean checkStatusByName(String shipmentName, String status){
        int columnIndex = getColumnIndex("Name");
        click(xpath("//thead/tr/th["+(columnIndex + 1)+"]//*[contains(@class, 'cursor-pointer')]/parent::button"));
        sleep(2000);
        getElement(xpath("//input[@data-testid='searchInput']")).clear();
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(shipmentName);
        getElement(xpath("//input[@data-testid='searchInput']")).sendKeys(Keys.ENTER);
        sleep(2000);
        List<WebElement> rows = getElements(xpath("//tbody//tr"));
        for(int i = 0; i < rows.size(); i++){
            String shipmentNameGetting = rows.get(i).findElement(xpath(".//td["+(columnIndex+1)+"]")).getText();
            System.out.println(shipmentNameGetting);
            if(shipmentNameGetting.equals(shipmentName)){
                int statusColumnIndex = getColumnIndex("Status");
                hoverOverElement(xpath("//tbody/tr["+(i+1)+"]/td["+(statusColumnIndex + 1)+"]//*[@data-state]"));
                sleep(800);
                String findings = getElement(xpath("(//*[@class='whitespace-pre-wrap break-words text-xs'])[1]")).getText();
                if(findings.equals(status))return true;
                else return false;
            }
        }
        return false;
    }
//    public void clickOnPlusIconToAddAvailableRules() {
//        click(plusIconToAddAvailableRules);
//    }
//
    //    public String selectBulkAirCargoInAddNewAsset() {
//        String shipmentType = "Bulk Air Cargo";
//        selectShipmentType(shipmentType);
//        return shipmentType;
//    }
//
//    public void selectCellAndGeneInAddNewAsset() {
//        clickOnAddNewShipment();
//        selectDropdownByText(selectNewShipment, "Cell and Gene");
//    }

//    public String selectdoglapanTrainingAirCargoOrganizationName() {
//        return selectOrganization("doglapan Training - Air Cargo");
//    }

//    public void SelectShipmentType(){
//        List<WebElement> ShipmentOptions = getElements(shipmentOptions);
//        int index = generateRandomInt(ShipmentOptions.size());
//        logAction(ShipmentOptions.get(index).getAttribute("value")+" is selected");
//        getElements(shipmentOptionsButton).get(index).click();
//    }

//    public void clearAndEnterEmailInNotification() {
//        clearAndEnterText(emailFieldInNotificationSection, "autotest@doglapanglobal.com");
//    }

//    public List<String> fillUpNewShipmentsInitial() {
//        List<String> shipmentInitialData = new ArrayList<>();
//        String str = clearAndEnterShipmentName();
//        shipmentInitialData.add(str);
//        str = clearAndEnterShipperName();
//        shipmentInitialData.add(str);
//        str = clearAndEnterOrderNumber();
//        shipmentInitialData.add(str);
//        //selectCarrierName();
////        str = selectModeOfTransportName();
//        str = "";
//        shipmentInitialData.add(str);
//        str = clearAndEnterCustomerName();
//        shipmentInitialData.add(str);
//        str = clearAndEnterRecipientName();
//        shipmentInitialData.add(str);
//        str = selectdoglapanTrainingAirCargoOrganizationName();
//        shipmentInitialData.add(str);
//        return shipmentInitialData;
//    }

//    public void fillUpAssetsInShipment(String AssetType) {
//        selectAssetTypeAndShow(AssetType);
//        if(AssetType.equals("doglapan RKN"))checkCheckBox(desiredAssetCheckBoxBulk);
//        if(AssetType.equals("CGT Hand Carry"))checkCheckBox(desiredAssetCheckBoxGene);
//        click(plusIconToAddAssets);
//    }

//    public void fillUpEmailInShipment() {
//        clearAndEnterEmailInNotification();
//    }

//    public String selectModeOfTransportName() {
//    //        selectDropdownByText(modeOfTransportSelect, "Air");
//        selectDropdownByValue(modeOfTransportSelect, "Air");
//        return "Air";
//    }

//    public void selectAllRulesAndAdd() {
//        click(selectAllRulesFromAvailableRules);
//        clickOnPlusIconToAddAvailableRules();
//    }
//
//    public void removeAllRules() {
//        click(RemoveAllRulesFromSelectedRules);
//    }
//
//    public String generateDateTime(LocalDateTime minDateTime) {
//        // Current time incremented by 1 minute to ensure it's always valid
//        LocalDateTime generatedTime = LocalDateTime.now().plusMinutes(1);
//
//        // If the generated time is before the minimum time, adjust it
//        if (generatedTime.isBefore(minDateTime)) {
//            generatedTime = minDateTime.plusMinutes(1); // Ensure it's strictly greater than minDateTime
//        }
//        int randomHours = generateRandomInt(24); // Random hours between 0 and 23
//        int randomMinutes = generateRandomInt(60); // Random minutes between 0 and 59
//        if(randomMinutes == 0 && randomHours == 0){
//            randomHours = 1;
//            randomMinutes = 1;
//        }
//        // Add random time to the minimum datetime
//        LocalDateTime randomizedDateTime = minDateTime.plusHours(randomHours).plusMinutes(randomMinutes);
//        // Format the datetime to "yyyy-MM-dd'T'HH:mm"
//
//        return randomizedDateTime.format(formatter);
//    }

    // Todo: check
//    public boolean deleteIconExist(String Section){
//        String trashBox = ".//ancestor::div[contains(@class, 'rounded-sm')]//*[contains(@class, 'lucide-trash2')]";
//        if(Section.equals("Rules")){
//            fillUpRules();
//            int len = getElement(selectedRulesSection).findElements(By.xpath(trashBox)).size();
//            //System.out.println(len);
//            if(len > 0)return true;
//            else return false;
//        }
//        else if(Section.equals("Assets in Shipment")){
//            fillUpAssetsInShipment();
//            int len = getElement(selectedAssetsSection).findElements(By.xpath(trashBox)).size();
//            //System.out.println(len);
//            if(len > 0)return true;
//            else return false;
//        }
//        return false;
//    }


//    public void setDataTimeForRouteButton(String Mood, String dateTimeValue) {
//        WebElement dateTimeInput = getElement(By.xpath("//input[@name='"+Mood+"']"));
//        dateTimeInput.click();
//        sleep(3000); // Wait for the calendar to open
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("arguments[0].value = arguments[1];", dateTimeInput, dateTimeValue);
//        sleep(3000);
//    }

    // Todo: Check
//    public boolean checkOriginAndDestinationTimeMessage() throws InterruptedException {
//        selectRouteInOriginAndDestination();
//        clickOnCreateButtonInCreateShipmentPopper();
//        sleep(2000);
//
//        String departure = generateDateTime(LocalDateTime.now());
//        String arrival = generateDateTime(LocalDateTime.parse(departure, formatter));
//        setDataTimeForRouteButton("departure", LocalDateTime.now().format(formatter));
//        clickOnCreateButtonInCreateShipmentPopper();
//        String str = getElement(plannedDepartureDateTimeField).findElement(By.xpath(".//ancestor::div/following-sibling::p")).getText();
//        boolean existence = str.contains("Departure time must be greater than current time");
//        if(!existence)return false;
//        str = getElement(plannedArrivalDateTimeField).findElement(By.xpath(".//ancestor::div/following-sibling::p")).getText();
//        existence = str.contains("Planned Arrival is required");
//        if(!existence)return false;
//        click(cancelButtonInAddNewShipment);
//        selectCellAndGeneInAddNewAsset();
//        selectdoglapanTrainingAirCargoOrganizationName();
//        click(createButtonInAddNewShipment);
//        setDataTimeForRouteButton("arrival", LocalDateTime.now().format(formatter));
//        click(createButtonInAddNewShipment);
//        str = getElement(plannedArrivalDateTimeField).findElement(By.xpath(".//ancestor::div/following-sibling::p")).getText();
//        existence = str.contains("Arrival time must be greater than current time");
//        if(!existence)return false;
//        str = getElement(plannedDepartureDateTimeField).findElement(By.xpath(".//ancestor::div/following-sibling::p")).getText();
//        existence = str.contains("Planned Departure is required");
//        if(!existence)return false;
//
//        //setDataTimeForRouteButton("arrival", arrival);
//        return true;
//    }
//
    // Todo: check
//    public List<String> CreateShipment() throws InterruptedException {
//        List<String>results = new ArrayList<>();
//        String shipmentType = SelectRandomShipmentTypeFromNewShipmentButton();
//        sleep(5000);
//        System.out.println(shipmentType);
//        clickOnCreateButtonInCreateShipmentPopper();
//        //checking error message of first section
//        boolean check = isRequiredError(shipmentType);
//        if(!check){
//            results.add("Failed"); results.add("All Required Message is not shown");return results;
//        }
//        check = checkMinimum3char();
//        if(!check){
//            results.add("Failed"); results.add("Minimum 3 Char warning is not shown");return results;
//        }
//        check = checkMax50char();
//        if(!check){
//            results.add("Failed"); results.add("Maximum 50 Char warning is not shown");return results;
//        }
//        //filling boxes of First section
//        String shipmentName = clearAndEnterShipmentName();
//        String shipperName = clearAndEnterShipperName();
//        String orderName = clearAndEnterOrderNumber();
//        String carrierName = selectRandomFromDropdownBox(carrierNameSelectButton, dropdownSelect, "");
//        String modeOfTransport = selectRandomFromDropdownBox(modeOfTransportSelectButton, dropdownSelect, "");
//        String customerName = clearAndEnterCustomerName();
//        String recipientName = clearAndEnterRecipientName();
//        String organizationName = selectRandomFromDropdownBox(organizationSelectButton, dropdownSelect, "");
//        //filling boxes of Second Section
//        String dataLoggerProfile = selectRandomFromDropdownBox(dataLoggerProfileSelectButton, dropdownSelect, "auto");
//        // rules checking and adding all rules
//        check = deleteIconExist("Rules");
//        if(!check){
//            results.add("Failed"); results.add("Delete Rules Icon is not Exist.");return results;
//        }
//        check = verifyAllFieldsUnderRulesInAddNewShipments();
//        if(!check){
//            results.add("Failed"); results.add("All Fields Under Rules In Add News Shipments are not Exist.");return results;
//        }
//        //origin and destination check
//        String selectRoute = selectRandomFromDropdownBox(selectRouteButton, dropdownSelect, "");
//        //Assets in shipment
//        check = deleteIconExist("Assets in Shipment");
//        waitForVisibility(RemoveAllAssetsFromSelectedAssets).click();
//        if(!check){
//            results.add("Failed"); results.add("Delete Assets Icon is not Exist.");return results;
//        }
//        sleep(5000);
//        //select data from Available Asset
//        String selectedAsset = selectAssetFromAvailableAssets("Auto");
//        String trackingID = "";
//        if(shipmentType.equals("Parcel")){
//            trackingID = setTrackingID();
//        }
//        clickOnCreateButtonInCreateShipmentPopper();
//        System.out.println(clearAndEnterEmailInNotificationDuplicate());
//        System.out.println(clearAndEnterEmailInNotificationInComplete());
//        clearAndEnterEmailInNotification();
//        clickOnCreateButtonInCreateShipmentPopper();
//        results.add("Passed");
//        results.add("Pass all the test");
//        return results;
//    }
//
//    public void scrolltoview(By place){
//        WebElement sortButton = wait.until(ExpectedConditions.presenceOfElementLocated(place));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sortButton);
//    }
//
    //    public boolean validateCreatedData(List<String> CreatedData){
//        List<WebElement> firstRow = getElements(xpath("//tbody/tr[1]/td"));
//        List<WebElement> headers = getElements(xpath("//thead/tr[1]/th"));
//        int cnt = 0;
//        System.out.println(headers.size());
//        for(int i = 1; i < headers.size(); i++){
//            String str = headers.get(i).getText().trim();
//            if(str.equals("Name")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(0)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Shipment Type")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(7)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Shipper Name")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(1)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Order Number")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(2)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Mode of Transport")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(3)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Customer Name")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(4)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Recipient Name")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(5)))return false;
//                else cnt++;
//            }
//            else if(str.equals("Organization")){
//                String data = firstRow.get(i).getText();
//                if(!data.equals(CreatedData.get(6)))return false;
//                else cnt++;
//            }
//        }
//        System.out.println(cnt);
//        return cnt == 8;
//    }
//    public List<String> getHeaders(){
//        List<String> headers = new ArrayList<>();
//        List<WebElement> webElementsHeaders = getElements(tableHeaders);
//        for(int i = 0; i < webElementsHeaders.size(); i++){
//            String str = webElementsHeaders.get(i).getText().trim();
//            if(str.isEmpty())continue;
//            if(str.equals("Actions"))continue;
//            headers.add(str);
//        }
//        return headers;
//    }
//
//    public String selectRandomFromDropdownBox(By Button, By SelectOption, String StartsWith){
//        click(Button);
//        List<WebElement>listOfOrganizationName = getElement(SelectOption).findElements(xpath(".//p"));
//        String organizationName = listOfOrganizationName.get(generateRandomInt(listOfOrganizationName.size())).getText().trim();
//        if(!StartsWith.isEmpty()){
//            StartsWith = StartsWith.toLowerCase();
//            for(WebElement orgName: listOfOrganizationName){
//                String str = orgName.getText().trim().toLowerCase();
//                if(str.startsWith(StartsWith)){
//                    organizationName = orgName.getText().trim();
//                    break;
//                }
//            }
//        }
//        while(organizationName.isEmpty()){
//            organizationName = listOfOrganizationName.get(generateRandomInt(listOfOrganizationName.size())).getText().trim();
//        }
//        if(organizationSelectButton.equals(Button))organizationName = "doglapan Training - Air Cargo"; //temporary:: after activating select option in route, this will be remove
//        selectDropdownByText(SelectOption, organizationName);
//        return organizationName;
//    }
//    public String selectAssetFromAvailableAssets(String StartsWith){
//        List<WebElement> listOfAssets = getElement(availableAssetsSection).findElements(By.xpath(".//ancestor::div/following-sibling::div[contains(@class, 'scrollbar')]//div"));
//        StartsWith = StartsWith.toLowerCase();
//        for(WebElement assets: listOfAssets){
//            String spr = assets.findElement(By.xpath(".//label")).getText().trim();
//            String str = spr.toLowerCase();
//            if(str.startsWith(StartsWith)){
//                assets.findElement(By.xpath(".//button")).click();
//                clickOnPlusIconToAddAvailableAssets();
//                return spr;
//            }
//        }
//        return "";
//    }
//
//    // Todo: check
//    public String SelectRandomShipmentTypeFromNewShipmentButton(){
//        clickOnAddNewShipment();
//        List<String> listOfShipmentType = Arrays.asList(//"Bulk Air Cargo",
//                "Parcel"//,
//                //        "Cell and Gene"
//        );
//        String shipmentType = listOfShipmentType.get(generateRandomInt(listOfShipmentType.size()));
//        selectDropdownByText(selectNewShipment, shipmentType);
//        return shipmentType;
//    }
//    public String setTrackingID(){
//        String trackingID = generateRandomString(5, "auto");
//        waitForVisibility(assetTrackingID).sendKeys(trackingID);
//        return trackingID;
//    }
//
//    public void selectedRules(int numberOfSelectedRules, String startingWith) {
//        WebElement rulesContainer = getElement(shipmentRules); // Get the container for rules
//        List<WebElement> rulesLabels = rulesContainer.findElements(By.xpath(".//label")); // Find all labels within the container
//
//        // Ensure the number of selected rules does not exceed available rules
//        if (numberOfSelectedRules > rulesLabels.size()) {
//            throw new IllegalArgumentException("Number of selected rules exceeds available rules.");
//        }
//
//        for (int i = 0; i < numberOfSelectedRules; i++) {
//            String ruleText = rulesLabels.get(i).getText(); // Get the text of the current label
//            if (ruleText.startsWith(startingWith)) {
//                // Find the corresponding checkbox and check it if necessary
//                WebElement checkbox = rulesContainer.findElements(By.xpath(".//button")).get(i);
//                if (!checkbox.isSelected()) {
//                    checkbox.click(); // Click the checkbox if it's not already selected
//                }
//            }
//        }
//        clickOnPlusIconToAddAvailableRules();
//    }

    public void clickOnShipmentDownload(String shipmentName) {
        String shipmentPDFDownloadIcon = "//*[text()='" + shipmentName + "']/following-sibling::button[2]";
        click(xpath(shipmentPDFDownloadIcon));
    }

    public String waitForDownload(String downloadPath, String extension) {
        Path path = Paths.get(downloadPath);
        long timeout = 60_000;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*" + extension)) {
                for (Path entry : stream) {
                    if (Files.isRegularFile(entry)) {
                        return entry.toString();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sleep(1000);
        }
        throw new RuntimeException("File download timed out!");
    }

    public String downloadShipmentPDF(String shipmentName) {
        // Step 1: Click the Download PDF button
        clickOnShipmentDownload(shipmentName);

        // Step 2: Wait for ZIP download to complete
        String zipFilePath = waitForDownload(downloadDir, ".zip");
        System.out.println("Downloaded ZIP: " + zipFilePath);
        return zipFilePath;
    }

    public Map<String, String> extractZip(String zipFilePath) throws IOException {
        Map<String, String> extractedFiles = new HashMap<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String fileName = entry.getName();
                String filePath = downloadDir + File.separator + fileName;

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                extractedFiles.put(fileName, filePath);
                System.out.println("Extracted: " + filePath);
            }
        }
        return extractedFiles;
    }

    public String readPDF(String pdfFilePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(pdfFilePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // Helper method to extract data using regex patterns
    private static String extractByPattern(String text, String pattern, String defaultValue) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1).trim() : defaultValue;
    }

    // Extracts the number of pages by searching for "Page X of Y"
    private static int extractNumberOfPages(String text) {
        Pattern pagePattern = Pattern.compile("Page \\d+ of (\\d+)");
        Matcher matcher = pagePattern.matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 1;
    }

    // Extracts asset information and returns it as a JSONArray
    private static JSONArray extractIncludedAssets(String text) {
        JSONArray includedAssets = new JSONArray();

        // Improved regex for capturing tracking number
        Pattern assetPattern = Pattern.compile(
                "(?:Asset Name Asset Type Tracking Number\\s+)(\\w+)\\s+([\\w\\s]+)\\s+(N/A|[\\w\\d-]+)");

        Matcher matcher = assetPattern.matcher(text);

        while (matcher.find()) {
            JSONObject asset = new JSONObject();
            asset.put("assetName", matcher.group(1).trim());      // autoTest002
            asset.put("assetType", matcher.group(2).trim());      // doglapan RKN
            asset.put("trackingNumber", matcher.group(3).trim()); // N/A
            includedAssets.put(asset);
        }

        return includedAssets;
    }

    // Extracts excursion data from the PDF
    private static JSONArray extractExcursions(String text) {
        JSONArray excursionArray = new JSONArray();

        // Locate the "Excursion" section
        Pattern excursionSectionPattern = Pattern.compile("Excursion(.*?)Shipment Summary Report", Pattern.DOTALL);
        Matcher sectionMatcher = excursionSectionPattern.matcher(text);

        if (sectionMatcher.find()) {
            // Extract only the excursion section
            String excursionSection = sectionMatcher.group(1).trim();

            // Split by lines and capture pairs (asset name followed by status)
            String[] lines = excursionSection.split("\\R"); // \R matches all line breaks

            for (int i = 0; i < lines.length - 1; i += 2) {
                JSONObject excursion = new JSONObject();
                excursion.put("assetName", lines[i].trim());
                excursion.put("status", lines[i + 1].trim());
                excursionArray.put(excursion);
            }
        }

        return excursionArray;
    }

    // Extracts route details (origin, waypoints, destination)
    private static JSONArray extractRoute(String text) {
        JSONArray routeArray = new JSONArray();

        Pattern originPattern = Pattern.compile("A Origin\\s*(.*?)\\n(.*?)\\n", Pattern.DOTALL);
        Matcher originMatcher = originPattern.matcher(text);
        if (originMatcher.find()) {
            JSONObject origin = new JSONObject();
            origin.put("AOrigin", originMatcher.group(1).trim());
            origin.put("locationDetails", originMatcher.group(2).trim());
            routeArray.put(origin);
        }

        Pattern waypointPattern = Pattern.compile("(Waypoint \\d+)\\s+(.*?)\\n(.*?)\\n", Pattern.DOTALL);
        Matcher waypointMatcher = waypointPattern.matcher(text);
        while (waypointMatcher.find()) {
            JSONObject waypoint = new JSONObject();
            waypoint.put(waypointMatcher.group(1).replace(" ", "").toLowerCase(), waypointMatcher.group(2).trim());
            waypoint.put("locationDetails", waypointMatcher.group(3).trim());
            routeArray.put(waypoint);
        }

        Pattern destinationPattern = Pattern.compile("Z Destination\\s*(.*?)\\n(.*?)\\n", Pattern.DOTALL);
        Matcher destinationMatcher = destinationPattern.matcher(text);
        if (destinationMatcher.find()) {
            JSONObject destination = new JSONObject();
            destination.put("ZDestination", destinationMatcher.group(1).trim());
            destination.put("locationDetails", destinationMatcher.group(2).trim());
            routeArray.put(destination);
        }

        return routeArray;
    }

    public static List<String> extractLinksFromPDF(String pdfFilePath) throws IOException {
        List<String> links = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(new File(pdfFilePath))) {
            // Iterate through all pages and find link annotations
            document.getPages().forEach(page -> {
                try {
                    for (PDAnnotation annotation : page.getAnnotations()) {
                        if (annotation instanceof PDAnnotationLink) {
                            PDAnnotationLink link = (PDAnnotationLink) annotation;
                            if (link.getAction() instanceof PDActionURI) {
                                String uri = ((PDActionURI) link.getAction()).getURI();
                                if (uri != null && uri.startsWith("http")) {
                                    links.add(uri);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error extracting link annotations", e);
                }
            });
        }
        return links;
    }

    public JSONObject extractShipmentDataAsJson(String pdfFilePath) throws IOException {
        // Extract shipment links from the PDF
        List<String> links = extractLinksFromPDF(pdfFilePath);
        System.out.println(links);

        String pdfText = readPDF(pdfFilePath);
        System.out.println(pdfText);

        JSONObject shipmentJson = new JSONObject();
        // Extract basic shipment information
        shipmentJson.put("shipmentName", extractByPattern(pdfText, "Shipment Name\\s*(.*)", ""));
        shipmentJson.put("status", extractByPattern(pdfText, "Status\\s*(.*)", ""));
        shipmentJson.put("numberOfAssets", Integer.parseInt(extractByPattern(pdfText, "Number of Assets\\s*(\\d+)", "0")));
        shipmentJson.put("reportGeneratedAt", extractByPattern(pdfText, "Report Generated\\s*at\\s*(.*)", ""));
        shipmentJson.put("reportGeneratedTimeZone", extractByPattern(pdfText, "Time Zone:\\s*(.*)", ""));
        shipmentJson.put("reportGeneratedBy", extractByPattern(pdfText, "by\\s*(\\w+)", ""));
        // Todo
        String stringOfRules = extractByPattern(pdfText, "(?:Ruleset Applied|Rules Applied)\\s*([\\s\\S]*?)(?=\\s*Shipment Link)", "").replaceAll("\\R", " ");
        System.out.println(stringOfRules);
        List<String> listOfRules = Arrays.asList(stringOfRules.split("\\s*,\\s*"));
        shipmentJson.put("rulesApplied", listOfRules);
        if (!links.isEmpty()) {
            shipmentJson.put("shipmentLink", links.get(0));  // Assuming the first link is the shipment link
        } else {
            shipmentJson.put("shipmentLink", "N/A");
        }
        shipmentJson.put("shipperName", extractByPattern(pdfText, "Shipper Name\\s*(.*)", ""));
        shipmentJson.put("actualDeparture", extractByPattern(pdfText, "Actual Departure\\s*(.*)", ""));
        shipmentJson.put("actualArrival", extractByPattern(pdfText, "Actual Arrival\\s*(.*)", ""));
        shipmentJson.put("plannedDeparture", extractByPattern(pdfText, "Planned Departure\\s*(.*)", "N/A"));
        shipmentJson.put("plannedArrival", extractByPattern(pdfText, "Planned Arrival\\s*(.*)", "N/A"));
        shipmentJson.put("shipmentDuration", extractByPattern(pdfText, "Shipment Duration\\s*(.*)", ""));
        shipmentJson.put("customerSupportPhone", extractByPattern(pdfText, "Customer Support Phone:\\s*([+\\d\\s-]+)", ""));
        shipmentJson.put("numberOfPage", extractNumberOfPages(pdfText));

        // Extract included assets
        shipmentJson.put("includedAssets", extractIncludedAssets(pdfText));

        // Extract excursion details
        //shipmentJson.put("excursion", extractExcursions(pdfText));

        // Extract route information
        shipmentJson.put("route", extractRoute(pdfText));

        return shipmentJson;
    }

    public void forceCompleteFromShipmentDetailsPage(String shipmentName){
        clickShipmentPage();
        clickOnForceCompleteByName(shipmentName);
    }

    public boolean validateDataWithPDFData(Map<String, Object> extractedShipmentMap, Map<String, Object> expectedShipmentMap){
        if(!extractedShipmentMap.get("customerSupportPhone").equals(expectedShipmentMap.get("customerSupportPhone")))return false;
        if(!extractedShipmentMap.get("reportGeneratedBy").equals(expectedShipmentMap.get("reportGeneratedBy")))return false;
        if(!extractedShipmentMap.get("plannedDeparture").equals(expectedShipmentMap.get("plannedDeparture")))return false;
        if(!extractedShipmentMap.get("plannedArrival").equals(expectedShipmentMap.get("plannedArrival")))return false;
        if(!extractedShipmentMap.get("shipperName").equals(expectedShipmentMap.get("shipperName")))return false;
        if(!extractedShipmentMap.get("route").equals(expectedShipmentMap.get("route")))return false;
        if(!extractedShipmentMap.get("shipmentName").equals(expectedShipmentMap.get("shipmentName")))return false;
        return extractedShipmentMap.get("status").equals(expectedShipmentMap.get("status"));
    }






    // Reads PDF content using PDFBox
//    private String readPdfContent(String pdfFilePath) throws IOException {
//        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
//            return new PDFTextStripper().getText(document);
//        }
//    }

    // Validates PDF content against expected asset name
//    private void validatePdfContent(String assetName, String pdfContent) {
//        if (!pdfContent.contains(assetName)) {
//            throw new AssertionError("PDF content validation failed for: " + assetName);
//        }
//    }

    // Cleans up downloaded files (optional)
//    public void cleanUpDownloads() throws IOException {
//        Path downloadPath = Paths.get(downloadDir);
//        Files.walk(downloadPath)
//                .map(Path::toFile)
//                .forEach(File::delete);
//        System.out.println("Cleaned up downloads directory.");
//    }

//    public void validateShipmentPDF(Set<String> expectedAssets) throws Exception {
//    public void validateShipmentPDF(String ShipmentName, Set<String> expectedAssets) throws Exception {
//        String downloadDir = RunManager.getDownloadsDirectory();
//
//        // Step 1: Click the Download PDF button
//        clickOnShipmentDownload(ShipmentName);
//
//        // Step 2: Wait for ZIP download to complete
//        String zipFilePath = waitForDownload(downloadDir, ".zip");
//        String zipFilePath = downloadShipmentPDF(ShipmentName);
//
//        // Step 3: Extract ZIP and collect PDF files
//        Map<String, String> extractedFiles = extractZip(zipFilePath);
//
//        // Step 4: Validate shipment-summary-report.pdf
//        if (!extractedFiles.containsKey("shipment-summary-report.pdf")) {
//            throw new AssertionError("Missing shipment-summary-report.pdf in ZIP");
//        }
//
//        // Step 5: Validate asset PDFs
//        for (String asset : expectedAssets) {
//            String expectedPdf = asset + ".pdf";
//            if (!extractedFiles.containsKey(expectedPdf)) {
//                throw new AssertionError("Missing asset PDF: " + expectedPdf);
//            }
//            // Step 6: Validate PDF content for each asset
//            String pdfContent = readPdfContent(extractedFiles.get(expectedPdf));
//            validatePdfContent(asset, pdfContent);
//        }
//
//        System.out.println("✅ All PDF validations passed!");
//    }

}

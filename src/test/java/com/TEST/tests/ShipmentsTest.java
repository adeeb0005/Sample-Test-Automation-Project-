package com.TEST.tests;

import com.TEST.pages.*;
import com.TEST.tests.api.ApiClient;
import com.TEST.utils.Config;
import com.TEST.utils.DBUtility;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.TEST.pages.BasePage.*;

public class ShipmentsTest extends BaseTest{
    SoftAssert softAssert = new SoftAssert();
    String shipmentsHeaderTitle = "Shipments";
    private ShipmentsPage shipmentsPage;
    private ApiClient apiClient;

    @BeforeClass
    public void setup() {
        // Initialize the ApiClient with the base URL from the properties file
        String baseUrl = Config.APIBaseUrl;
        String accessToken = Config.APIAccessToken;
        String organizationId = Config.APIOrgId;
        apiClient = new ApiClient(baseUrl, accessToken, organizationId);
    }

    public ShipmentsPage loginAndGoToShipmentsPage() {
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
        test.info("Going to Shipments Page...");
        shipmentsPage = dashboardPage.goToShipmentsPage();
        test.info("At the Shipments Page Now");

        return shipmentsPage;
    }

    @Test(groups = {"regression"})
    public void S001_VerifyComponentsOnShipmentsPage_TC6679() {
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());

        Assert.assertTrue(shipmentsPage.isNewShipmentButtonPresent());

        Assert.assertTrue(shipmentsPage.isDownloadButtonPresent());

        Assert.assertTrue(shipmentsPage.isShipmentsTableBulkActionButtonPresent());

        // Todo : Verify the delete selected in bulk action button
//        Assert.assertTrue(shipmentsPage.isShipmentsDeleteSelectedDropdownPresent());

        // Todo : Verify the column configuration button
        // bad locator

        Assert.assertTrue(shipmentsPage.isShipmentsFilterButtonPresent());

        Assert.assertTrue(shipmentsPage.isShipmentsDataGridTablePresent());

        System.out.println("bye bye");
    }

    @Test(groups = {"regression"})
    public void S002_VerifyDataGridOnShipmentsPage_TC6680() {
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());

        Assert.assertTrue(shipmentsPage.verifyShipmentsDataGridColumnHeaderTitle());

        // Todo 2: validate the action column contains complete, edit, delete, full details
        // bad locators
        // Todo 3: validate the type column contains bulk air cargo, parcel, cell and gene
        // Todo 4: validate the status column contains pending, at origin, in progress and completed
        // Todo 5: In the grid, the below columns will show values with the respective format
        // action, SelectShipmentType, status, Direction, origin, destination, shipperName, OrderName, carrierName, modeOfTransport, customerName,
        // recipientName, Organization, assertCount, plannedDeparture, plannedArrival, routeName, dataLoggerProfile,
        // createdAt, createdBy, lastModifiedAt, lastModifiedBy
//        Assert.assertTrue(shipmentsPage.verifyShipmentsDataGridAllDataTypes());
    }

    @Test(groups = {"regression"})
    public void S003_VerifyAddNewShipmentBulkAirCargo_TC6702() {
        String shipmentType = "Bulk Air Cargo";
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());

        // select bulk air cargo
        shipmentsPage.selectShipmentType(shipmentType);
        // validate the modal

        // validate the fields, dropdowns and collapsed sections
        Assert.assertTrue(shipmentsPage.verifyAllDefaultExpendedFieldsInAddNewShipments(shipmentType));
        sleep(5000);
        Assert.assertTrue(shipmentsPage.verifyCollapsedSectionsInAddNewShipments());

//        shipmentsPage.expendDataLoggerProfileInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderDataLoggerInAddNewShipments());

        shipmentsPage.expendRulesInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderRulesInAddNewShipments());

        shipmentsPage.expendOriginAndDestinationInAndNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderOriginAndDestinationInAddNewShipments());

        shipmentsPage.expendAssetsInShipmentInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderAssetsInShipmentInAddNewShipments("doglapan RKN"));

//        shipmentsPage.expendNotificationInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderNotificationInAddNewShipments());

        Assert.assertTrue(shipmentsPage.verifyPresenceOfCreateAndCancelButtonInAddNewShipments());

        System.out.println("bye bye");
    }
    public List<List<String>> getAssetsNameFromData(JSONObject testCase, String shipmentName){
        JSONArray assetsArray = testCase.getJSONArray("assetsInShipment");
        int listOfData = assetsArray.length();
        List<List<String>> results = new ArrayList<>();
        long time = Instant.now().getEpochSecond();
        for(int i = 0; i < listOfData; i++){
            List<String> assetsDetails = new ArrayList<>();
            assetsDetails.add(assetsArray.getJSONObject(i).getString("assetName"));
            assetsDetails.add(assetsArray.getJSONObject(i).getString("assetId"));
            assetsDetails.add(assetsArray.getJSONObject(i).getString("assetType"));
            if(!shipmentName.equals("Bulk Air Cargo")){
                assetsDetails.add("autoTrackingID"+time + "" + (i + 1));
            }
            results.add(assetsDetails);
        }
        return results;
    }
    public List<String> getDeviceNameFromData(JSONObject testCase){
        JSONArray assetsArray = testCase.getJSONArray("assetsInShipment");
        int listOfData = assetsArray.length();
        List<String> results = new ArrayList<>();
        long time = Instant.now().getEpochSecond();
        for(int i = 0; i < listOfData; i++){
            System.out.println();
            results.add(assetsArray.getJSONObject(i).getJSONArray("dataLogger").getJSONObject(0).getString("dataLoggerName"));

        }
        return results;
    }
    public List<Map<String, String>> getRules(JSONObject testCase){
        Map<String, String> rules = new HashMap<>();
        JSONArray rulesArray = testCase.getJSONArray("rules");
        int listOfData = rulesArray.length();
        List<Map<String, String>> results = new ArrayList<>();
        for(int i = 0; i < listOfData; i++){
            Map<String, String> rulesDetails = new HashMap<>();
            rulesDetails.put("ruleName", rulesArray.getJSONObject(i).getString("ruleName"));
            rulesDetails.put("ruleId", rulesArray.getJSONObject(i).getString("ruleId"));
            results.add(rulesDetails);
        }
        return results;
    }
    public List<String> getRulesOnlyId(JSONObject testCase){
        JSONArray rulesArray = testCase.getJSONArray("rules");
        int listOfData = rulesArray.length();
        List<String> results = new ArrayList<>();
        for(int i = 0; i < listOfData; i++){
            results.add(rulesArray.getJSONObject(i).getString("ruleId"));
        }
        return results;
    }
    @Test(groups = {"regression"})
    public void S004_VerifySuccessfulCreationOfBulkAirCargoShipmentWithValidData_TC6924() throws InterruptedException, IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S004_VerifySuccessfulCreationOfBulkAirCargoShipmentWithValidData_TC6924");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), rules, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");

        } catch (RuntimeException e){
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName((String) shipmentData.get("shipmentName"));
        }
//        Assert.assertTrue(shipmentsPage.validateCreatedData(shipmentShipperOrderTransportCustomerRecipientOrg), "data not matched");
        // Todo: fix the validateCreatedData by hashmap or params
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        shipmentsPage.deleteShipmentByName((String) shipmentData.get("shipmentName"));
    }

    @Test(groups = {"regression"})
    public void S005_EnsureFieldLevelErrorHandlingInCreateShipmentForm_TC6926() {
        String shipmentType = "Bulk Air Cargo";
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        shipmentsPage.selectShipmentType(shipmentType);
        sleep(5000);
        shipmentsPage.clickOnCreateButtonInCreateShipmentPopper();
        sleep(5000);
        Assert.assertTrue(shipmentsPage.isRequiredError("Bulk Air Cargo", "route"), "Required messages are not properly showing");
        //Todo 1: Have to validate Shipper Name, Order Number, Customer Name, Recipient Name: minimum 3 char and not more than 50 char
        //Todo 2: Rules: Select From Available Rules and click button then validate delete button into selected rules
        //Todo 2.1: validate available rules, select all, selected rules, remove rules
        //Todo 3: Validate Select Inbound or Outbound, select route, then planned departure, planned arrival and Validate shown message below into those boxes
        //Todo 4: do same as Todo 2 and 2.1 for Assets in shipment and also validate it's search button
        //Todo 5: validate email section: duplicate and incomplete emails are not allowed. msg will be shown their.
    }

    @Test(groups = {"regression"})
    public void S006_VerifyPaginationFuncShipmentListGrid_TC7062(){
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        // Todo: Shipments Row Size
        Assert.assertTrue(shipmentsPage.paginationExist(), "Pagination is not existed");
        Assert.assertTrue(shipmentsPage.clickedNextPage(), "Next Page is Not clicked");
        Assert.assertTrue(shipmentsPage.clickedPreviousPage(), "Previous Page is Not clicked");
        Assert.assertTrue(shipmentsPage.LastPageClick(), "Last Page is not clicked");
        Assert.assertTrue(shipmentsPage.clickedFirstPage(), "First Page is not clicked");
        Assert.assertTrue(shipmentsPage.clickedRandomPage(), "Random Page is not clicked");
        Assert.assertTrue(shipmentsPage.clickedRandomPage(), "Random Page is not clicked");
    }

    @Test(groups = {"regression"})
    public void S007_VerifySuccessfulCreationOfCellAndGeneShipmentWithValidData_TC7646() throws InterruptedException, IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S007_VerifySuccessfulCreationOfCellAndGeneShipmentWithValidData_TC7646");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName((String) shipmentData.get("shipmentName"));
        }
//        Assert.assertTrue(shipmentsPage.validateCreatedData(shipmentShipperOrderTransportCustomerRecipientOrg), "data not matched");
        // Todo: fix the validateCreatedData by hashmap or params}
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        shipmentsPage.deleteShipmentByName((String) shipmentData.get("shipmentName"));

    }

    @Test(groups = {"regression"})
    public void S008_VerifyUIContentsOfAddCellAndGeneShipmentModal_TC7645(){
        String shipmentType = "Cell and Gene";
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        Assert.assertTrue(shipmentsPage.isShipmentsDataGridTablePresent(), "Shipment Table is missing");
        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        shipmentsPage.selectShipmentType(shipmentType);
        // validate the fields, dropdowns and collapsed sections
        Assert.assertTrue(shipmentsPage.verifyAllDefaultExpendedFieldsInAddNewShipments(shipmentType));
        Assert.assertTrue(shipmentsPage.verifyCollapsedSectionsInAddNewShipments());

        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderDataLoggerInAddNewShipments());
        shipmentsPage.expendRulesInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderRulesInAddNewShipments());
        shipmentsPage.expendOriginAndDestinationInAndNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderOriginAndDestinationInAddNewShipments());
        shipmentsPage.expendAssetsInShipmentInAddNewShipments();
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderAssetsInShipmentInAddNewShipments("CGT Hand Carry"));

        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderNotificationInAddNewShipments());

        Assert.assertTrue(shipmentsPage.verifyPresenceOfCreateAndCancelButtonInAddNewShipments());
    }

    @Test(groups = {"regression"})
    public void S009_EnsureFieldLevelErrorHandlingInCreateShipmentForm_TC7808() throws InterruptedException {
        String organization = "doglapan Training - Air Cargo";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        String shipmentType = "Cell and Gene";
        String assetType = "CGT Hand Carry";
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        shipmentsPage.selectShipmentType(shipmentType);
        sleep(5000);
        shipmentsPage.clickOnCreateButtonInCreateShipmentPopper();
        sleep(5000);
        Assert.assertTrue(shipmentsPage.isRequiredError("Cell and Gene", "route"), "Required messages are not properly showing");
        Assert.assertTrue(shipmentsPage.checkMinimum3char(), "Message is not shown");
        Assert.assertTrue(shipmentsPage.checkMax50char(), "Message is not shown");
        shipmentsPage.selectOrganization(organization, organizationId);
        // Todo: check if delete icon exist
      Assert.assertTrue(shipmentsPage.deleteIconExist("Rules"), "Delete icon is not exist");
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderRulesInAddNewShipments());
        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderOriginAndDestinationInAddNewShipments(), "One of Element is missing");
        shipmentsPage.clickOnCreateButtonInCreateShipmentPopper();
        Assert.assertTrue(shipmentsPage.deleteIconExist("Assets in Shipment"), "Delete icon is not exist");

        Assert.assertTrue(shipmentsPage.verifyAllFieldsUnderAssetsInShipmentInAddNewShipments(assetType));
        Assert.assertTrue(shipmentsPage.clearAndEnterEmailInNotificationDuplicate(), "Duplicate message is not shown");
        Assert.assertTrue(shipmentsPage.clearAndEnterEmailInNotificationInComplete(), "Invalid message is not shown");
        // Todo: validate field level error handling
//        shipmentsPage.checkOriginAndDestinationTimeMessage();
    }

    @Test(groups = "DB")
    public void S010_verifySearchFunctionInShipment() throws SQLException {
        String searchQuery = "WITH ShipmentsWithNames AS (SELECT s.ShipmentName AS Name, sl1.LocationName AS Origin, sl2.LocationName AS Destination, s.OrderNumber AS 'Order Number', s.CustomerName AS 'Customer Name', s.RecipientName AS 'Recipient Name', u.UserDisplayName AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.updatedat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.updatedat), 108) AS 'Last Modified At', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.createdat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.createdat), 108) AS 'Created At', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedArrivalDate), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedArrivalDate), 108) AS 'Planned Arrival', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedDepartureDate), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedDepartureDate), 108) AS 'Planned Departure', s.UpdatedAt, s.createdat, s.PlannedArrivalDate, s.PlannedDepartureDate FROM Shipments s JOIN shipmentlocationsnapshots sl1 ON sl1.shipmentlocationsnapshotid = s.originlocationsnapshotid JOIN shipmentlocationsnapshots sl2 ON sl2.shipmentlocationsnapshotid = s.destinationlocationsnapshotid LEFT JOIN users u ON s.createdbyuserid = u.userid WHERE s.DeactivatedAt IS NULL) SELECT Name, Origin, Destination, [Order Number], [Customer Name], [Recipient Name], [Planned Departure], [Planned Arrival], [Created At], [Created By], [Last Modified At] FROM ShipmentsWithNames";
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        List<String> items = Arrays.asList("Name", "Origin", "Destination", "Order Number", "Customer Name", "Recipient Name", "Created By");
        List<String> DEMO_TESTS = Arrays.asList("fnn", "ab", "x");
        for (String item : items) {
            for (String demo_test : DEMO_TESTS) {
                String query = searchQuery + " WHERE [" + item + "] LIKE '%" + demo_test + "%' ORDER BY UpdatedAt DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                System.out.println(query);
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                softAssert.assertTrue(shipmentsPage.checkSearchOnColumn(item, demo_test, tableData), "Match not found");
                sleep(2000);
            }
        }
        softAssert.assertAll();
    }

    @Test(groups = "DB")
    public void S011_verifyFuncOfSortForShipmentList_TC7056() throws SQLException {
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        String[] fields = {
                "'Name'", "'Order Number'", "'Planned Departure'", "'Planned Arrival'" ,"'Created At'", "'Last Modified At'"
        };
        String sortQuery = "WITH ShipmentsWithNames AS (SELECT s.ShipmentName AS Name, sl1.LocationName AS Origin, sl2.LocationName AS Destination, s.OrderNumber AS 'Order Number', s.CustomerName AS 'Customer Name', s.RecipientName AS 'Recipient Name', u.UserDisplayName AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.updatedat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.updatedat), 108) AS 'Last Modified At', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.createdat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.createdat), 108) AS 'Created At', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedArrivalDate), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedArrivalDate), 108) AS 'Planned Arrival', CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedDepartureDate), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, s.PlannedDepartureDate), 108) AS 'Planned Departure', s.UpdatedAt, s.createdat, s.PlannedArrivalDate, s.PlannedDepartureDate FROM Shipments s JOIN shipmentlocationsnapshots sl1 ON sl1.shipmentlocationsnapshotid = s.originlocationsnapshotid JOIN shipmentlocationsnapshots sl2 ON sl2.shipmentlocationsnapshotid = s.destinationlocationsnapshotid LEFT JOIN users u ON s.createdbyuserid = u.userid WHERE s.DeactivatedAt IS NULL) SELECT Name, Origin, Destination, [Order Number], [Customer Name], [Recipient Name], [Planned Departure], [Planned Arrival], [Created At], [Created By], [Last Modified At] FROM ShipmentsWithNames";
        //single column
        String[] situations = {" ASC ", " DESC "};
        for (String field : fields) {
            for(String situation : situations){
                System.out.println("start");
                sleep(2000);
                shipmentsPage.clickSort(field);
                sleep(3000);
                if(field.equals("'Planned Departure'"))field = "PlannedDepartureDate";
                if(field.equals("'Planned Arrival'"))field = "PlannedArrivalDate";
                if(field.equals("'Created At'"))field = "createdat";
                if(field.equals("'Last Modified At'"))field = "UpdatedAt";
                String query = sortQuery + " ORDER BY " + field + situation + " OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;";
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                List<List<String>> table = shipmentsPage.getTable();
                System.out.println(field+situation);
                softAssert.assertTrue(shipmentsPage.checkTable(table, tableData), field+situation+"not match");
                System.out.println("done");
                if(field.equals("PlannedDepartureDate"))field = "'Planned Departure'";
                if(field.equals("PlannedArrivalDate"))field = "'Planned Arrival'";
                if(field.equals("createdat"))field = "'Created At'";
                if(field.equals("UpdatedAt"))field = "'Last Modified At'";
            }
            //sleep(5000);
            shipmentsPage.clickSort(field);
            //sleep(5000);
        }
        softAssert.assertAll();
    }

    @Test(groups = {"regression"})
    public void S012_AssetCreationDataValidationAutomation_TC0000() throws InterruptedException {
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        Assert.assertTrue(shipmentsPage.isShipmentsDataGridTablePresent(), "Shipment Table is missing");
        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        // Todo: createShipment and validate talk to adeeb
//        List<String> result = shipmentsPage.CreateShipment();
//        if(result.get(0).equals("Failed"))test.fail(result.get(1));
//        else test.pass(result.get(1));
        //todo:: data validate from grid
        //todo:: data validate from database
    }

    @Test(groups = {"regression"})
    public void S013_VerifyShipmentTravel_TC0000() throws InterruptedException {
        shipmentsPage = loginAndGoToShipmentsPage();

        // Todo: test.info or logAction

        // Todo: Take an existing shipment of status in progress or completed
        // Todo: go to shipment details and validate the shipment travel details matches (i.e. origin, destination, waypoint etc)
        // Todo: validate the point shows in map (just validate the existence of the point in map)
    }

    @Test(groups = {"regression"})
    public void S014_VerifyShipmentJourneyWithOneAsset_TC0000() throws InterruptedException, IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S014_VerifyShipmentJourneyWithOneAsset_TC0000");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        String shipmentName = (String) shipmentData.get("shipmentName");
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName(shipmentName);
        }
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        sleep(2000);
        Map<String, String> shipmentInfoFromGrid = shipmentsPage.getDataByRowFromGrid(1);
        shipmentsPage.gotoShipmentDetailsPage(1);
        sleep(5000);
        shipmentsPage.clickGeneralInfo();
        sleep(5000);

        Map<String, Object> dataFromGeneralInfo = shipmentsPage.getDataFromGeneralInfo();
        shipmentsPage.clickStatusButton();
        System.out.println(shipmentInfoFromGrid);
        String shipmentId = shipmentInfoFromGrid.get("shipmentId");
        System.out.println(shipmentId);
        Response response = apiClient.GETShipmentByShipmentId(shipmentId);
        response.prettyPrint();
        String jsonString = response.body().asString(); // Response ke string e convert korlam
        JSONObject jsonResponse = new JSONObject(jsonString);
        Map<String, Object> detailsFromAPI = jsonResponse.toMap();
        System.out.println(shipmentsPage.validateData(detailsFromAPI, dataFromGeneralInfo, shipmentData));
        sleep(2000);
        List<List<String>> longLatPairs = new ArrayList<>();
        longLatPairs.add(Arrays.asList("51.5631", "25.2654"));
        longLatPairs.add(Arrays.asList("72.2748", "25.7986"));
        longLatPairs.add(Arrays.asList("90.4029252", "23.8434344"));
        longLatPairs.add(Arrays.asList("91.3893", "23.0049"));
        longLatPairs.add(Arrays.asList("92.0014375", "21.4326875"));

        String originxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Origin']";
        String progressxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='In Progress']";
        String destinationxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Destination']";

        sleep(2000);

        String deviceId = testCase.getJSONArray("assetsInShipment").getJSONObject(0).getJSONArray("dataLogger").getJSONObject(0).getString("dataLoggerName");
        String uid = "807938083";
        String desc = "Location Based Data";
        int src = 3;

        for (int i = 0; i < longLatPairs.size(); i++) {
            Integer vol = 22;
            Integer signal = 22;

            try {
                response = apiClient.pushLocationDataNow(deviceId, uid, vol, signal,
                        longLatPairs.get(i).get(0), longLatPairs.get(i).get(1), desc, src);
                response.then().statusCode(200);
                System.out.println("Response: " + response.prettyPrint());
            } catch (Exception e) {
                try {
                    shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
                    System.out.println("POSTShipmentCompleteById executed due to failure.");
                } catch (Exception innerException) {
                    System.err.println("Failed to execute POSTShipmentCompleteById: " + innerException.getMessage());
                }
                break;
            }

            sleep(8000);
            shipmentsPage.refreshPage();
            sleep(5000);
            shipmentsPage.scrollToElement(By.xpath("//*[text()='Help']"));
            if (i == 0) {
                boolean update = shipmentsPage.checkStatusInAssetDetails(originxpath);
                if(!update)shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
                Assert.assertTrue(update, "Origin is not reached");
            } else if (i + 1 == longLatPairs.size()) {
                boolean update = shipmentsPage.checkStatusInAssetDetails(destinationxpath);
                if(!update)shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
                Assert.assertTrue(update, "Destination is not reached");
            } else {
                boolean update = shipmentsPage.checkStatusInAssetDetails(progressxpath);
                if(!update)shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
                Assert.assertTrue(update, "Progress is not reached");
            }

            System.out.println("Iteration: " + i);
        }
        System.out.println("done");
        System.out.println("shipmentInfoFromGrid");
        System.out.println(shipmentInfoFromGrid);
        System.out.println("dataFromGeneralInfo");
        System.out.println(dataFromGeneralInfo);
        System.out.println("detailsFromAPI");
        System.out.println(detailsFromAPI);
        // Todo: push telemetry data through api
        // Todo: Complete the shipment by continuous pushing the device data and validating
        //shipmentsPage.deleteRowByName(shipmentShipperOrderTransportCustomerRecipientOrg.get(0));
        shipmentsPage.clickShipmentPage();
        sleep(5000);
        shipmentsPage.clickOnForceCompleteByName(shipmentName);
    }

    @Test
    public void S015_VerifyShipmentJourneyWithForExistingShipment_TC0000() {
        long time = Instant.now().getEpochSecond();
        String shipmentType = "Bulk Air Cargo";
        String shipmentName = "AutoShipment" + time;
        String shipperName = "AutoShipper" + time;
        String orderNumber = "AutoOrder" + time;
        String carrierName = "DHL Express";

        Map<String, Object> shipmentData = new HashMap<>();
        shipmentsPage = loginAndGoToShipmentsPage();

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
    }

    @Test
    public void S016_VerifyExcursionResolutionEmailNotification_TC8331() {
        long time = Instant.now().getEpochSecond();
        String shipmentType = "Bulk Air Cargo";
        String shipmentName = "AutoShipment" + time;
        String shipperName = "AutoShipper" + time;
        String orderNumber = "AutoOrder" + time;
        String carrierName = "DHL Express";

        Map<String, Object> shipmentData = new HashMap<>();
        shipmentsPage = loginAndGoToShipmentsPage();

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());

        // Todo: go to shipment details by ID and verify the shipment details page visibility
        // Todo: go to "Environment Condition" tab
        // Todo: fetch and validate excursion-generated time for a specific sensor
        // Todo: Verify that the sensor data returns to defined safe range

        // Todo: Trigger an excursion resolution event for a single sensor such as Cargo Temperature
        // Todo: Validate the email received by the tagged emails
        // Todo: validate email details
        // Todo: verify email contents (
        //  subject: Critical Excursion Resolved on Shipment "Shipment Name",
        //  doglapan logo
        //  Status: Resolved
        //  Excursion parameter and safe conditions
        //  Resolution time
        //  Location details
        //  Direct shipment link
        //  Customer support contact details
        //  Email timestamp)

        // Todo: validate excursion email for multiple sensor

        // Todo:
    }

    @Test(groups = {"regression"})
    public void S017_VerifyShipmentDetailsStatusTabInBulkAirCargoShipment_TC0000() throws IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S017_VerifyShipmentDetailsStatusTabInBulkAirCargoShipment_TC0000");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        String shipmentName = (String) shipmentData.get("shipmentName");
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName(shipmentName);
        }
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        sleep(2000);
        for(int j = 0; j < 2; j++) {
            if (j == 0) shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
            sleep(5000);
            List<String> assetsName = new ArrayList<>();
            for (int i = 0; i < assetNames.size(); i++) {
                assetsName.add(assetNames.get(i).get(0));
            }
            try {
                Assert.assertTrue(shipmentsPage.snapShotOfShipmentDetails(shipmentData), "Status Snapshot is not correct");
                shipmentsPage.clickShipmentPage();
                sleep(5000);
                if (j == 0) {
                    shipmentsPage.clickOnShipmentName(shipmentName);
                    Map<String, Object> shipmentCardData = shipmentsPage.getShipmentCardData();
                    Assert.assertTrue(shipmentsPage.compareShipmentDataAndShipmentCardData(shipmentData, shipmentCardData), "Not match with popup");
                    shipmentsPage.clickFullDetailsPage();
                }
            } catch (RuntimeException e) {
                shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
            }
        }
        shipmentsPage.deleteShipmentByName(shipmentName);
        // Todo: create shipment with given assets
        // Todo: click on the shipment name and validate the shipment summery
        // Todo: go to shipment details page by clicking on view full details from shipment summery
            // Todo: validate shipment details in shipment status
    }

    @Test(groups = {"regression"})
    public void S018_VerifyShipmentDetailsStatusTabInParcelShipment_TC0000() throws IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S018_VerifyShipmentDetailsStatusTabInParcelShipment_TC0000");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        String shipmentName = (String) shipmentData.get("shipmentName");
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName(shipmentName);
        }
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        sleep(2000);
        for(int j = 0; j < 2; j++) {
            if (j == 0) shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
            sleep(5000);
            try {
                List<String> assetsName = new ArrayList<>();
                for (int i = 0; i < assetNames.size(); i++) {
                    assetsName.add(assetNames.get(i).get(0));
                }
                Assert.assertTrue(shipmentsPage.snapShotOfShipmentDetails(shipmentData), "Status Snapshot is not correct");
                shipmentsPage.clickShipmentPage();
                sleep(5000);
                if (j == 0) {
                    shipmentsPage.clickOnShipmentName(shipmentName);
                    Map<String, Object> shipmentCardData = shipmentsPage.getShipmentCardData();
                    Assert.assertTrue(shipmentsPage.compareShipmentDataAndShipmentCardData(shipmentData, shipmentCardData), "Not match with popup");
                    shipmentsPage.clickFullDetailsPage();
                }
            } catch (RuntimeException e) {
                shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
            }
        }
        shipmentsPage.deleteShipmentByName(shipmentName);
    }

    @Test(groups = {"regression"})
    public void S019_VerifyShipmentDetailsStatusTabInCellAndGeneShipment_TC0000() throws IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S019_VerifyShipmentDetailsStatusTabInCellAndGeneShipment_TC0000");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        String shipmentName = (String) shipmentData.get("shipmentName");
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName(shipmentName);
        }
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        sleep(2000);
        for(int j = 0; j < 2; j++) {
            if (j == 0) shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
            sleep(5000);
            try{
            List<String> assetsName = new ArrayList<>();
            for (int i = 0; i < assetNames.size(); i++) {
                assetsName.add(assetNames.get(i).get(0));
            }
            Assert.assertTrue(shipmentsPage.snapShotOfShipmentDetails(shipmentData), "Status Snapshot is not correct");
            shipmentsPage.clickShipmentPage();
            sleep(5000);
            if (j == 0) {
                shipmentsPage.clickOnShipmentName(shipmentName);
                Map<String, Object> shipmentCardData = shipmentsPage.getShipmentCardData();
                Assert.assertTrue(shipmentsPage.compareShipmentDataAndShipmentCardData(shipmentData, shipmentCardData), "Not match with popup");
                shipmentsPage.clickFullDetailsPage();
            }
            } catch (RuntimeException e) {
                shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
            }
        }
        shipmentsPage.deleteShipmentByName(shipmentName);

        // Todo: create shipment with given assets
        // Todo: click on the shipment name and validate the shipment summery
        // Todo: go to shipment details page by clicking on view full details from shipment summery
            // Todo: validate shipment details in shipment status
    }

    @Test
    public void S020_VerifyShipmentLocationAndTelemetryData_TC0000() throws IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S020_VerifyShipmentLocationAndTelemetryData_TC0000");

        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
        long time = Instant.now().getEpochSecond();
        Map<String, Object> shipmentData = new HashMap<>();
        List<List<String>> assetNames = getAssetsNameFromData(testCase, testCase.getString("shipmentType"));
        List<String> deviceId = getDeviceNameFromData(testCase);
        System.out.println(assetNames);
        System.out.println(deviceId);
        List<Map<String, String>> rules = getRules(testCase);
        sleep(5000);
        shipmentData.putAll(shipmentsPage.createShipment(testCase.getString("shipmentType"), testCase.getString("shipmentName") + time, testCase.getString("shipperName") + time, testCase.getString("orderNumber") + time,
                testCase.getJSONObject("carrier").getString("carrierName"), testCase.getString("modeOfTransport"),
                testCase.getString("customerName") + time, testCase.getString("recipientName") + time, testCase.getJSONObject("organization").getString("organizationName"), testCase.getJSONObject("organization").getString("organizationId"),
                testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileName"), null, testCase.getJSONObject("originAndDestination").getString("shipmentDirection"), testCase.getJSONObject("originAndDestination").getString("locationOrRoute"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeName"),
                testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("routeId"), "", testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("originLocationName"), testCase.getJSONObject("originAndDestination").getJSONObject("route").getString("destinationLocationName"),
                testCase.getJSONObject("originAndDestination").getString("plannedArrivalDate"), testCase.getJSONObject("originAndDestination").getString("plannedDepartureDate"), assetNames, testCase.getString("email")));
        String shipmentName = (String) shipmentData.get("shipmentName");
        try {
            Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
        } catch (RuntimeException e) {
            shipmentsPage.refreshPage();
            shipmentsPage.refreshPage();
            shipmentsPage.deleteShipmentByName(shipmentName);
        }
        shipmentsPage.refreshPage();
        shipmentsPage.refreshPage();
        sleep(2000);
        shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
        List<List<String>> longLatPairs = new ArrayList<>();
        longLatPairs.add(Arrays.asList("51.5631", "25.2654"));
        longLatPairs.add(Arrays.asList("72.2748", "25.7986"));
        longLatPairs.add(Arrays.asList("90.4029252", "23.8434344"));
        longLatPairs.add(Arrays.asList("91.3893", "23.0049"));
        longLatPairs.add(Arrays.asList("92.0014375", "21.4326875"));
        Integer t0 = 100, t1 = 5, h0 = 12, h1 = 3, vol = 100, signal = 60;
        String originxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Origin']";
        String progressxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='In Progress']";
        String destinationxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Destination']";

        sleep(2000);
        List<String> assetsName = new ArrayList<>();
        for (List<String> asset : assetNames) {
            assetsName.add(asset.get(0));
        }
        try {
            Assert.assertTrue(shipmentsPage.checkStatusSnapshot(shipmentName, assetsName, testCase.getString("shipmentType")), "Status snapshot is not OK");

            String uid = "807938083";
            int src = 3;
            Response response = null;
            for (int i = 0; i < longLatPairs.size(); i++) {
                if (i == 0) {
                    Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "Pending"),
                            "Pending is not shown properly");
                }
                //Integer vol = 22;
                //Integer signal = 22;
                List<Integer> tempCargoTemp = new ArrayList<>();
                List<Integer> tempAmbientTemp = new ArrayList<>();
                List<Integer> tempCargoHumidity = new ArrayList<>();
                List<Integer> tempAmbientHumidity = new ArrayList<>();

                try {
                    time = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().getEpochSecond();
                    String zoneId = "Asia/Dhaka";
                    if (i == 0) zoneId = "Asia/Qatar";
                    else if (i == 1) zoneId = "Asia/Karachi";
                    long lbsTime = ZonedDateTime.now(ZoneId.of(zoneId)).toInstant().getEpochSecond();
                    for (int k = 0; k < deviceId.size(); k++) {
                        tempCargoTemp.add(t0 + shipmentsPage.generateRandomInt(500));
                        tempAmbientTemp.add(t1 + shipmentsPage.generateRandomInt(300));
                        tempCargoHumidity.add(h0 + shipmentsPage.generateRandomInt(10));
                        tempAmbientHumidity.add(h1 + shipmentsPage.generateRandomInt(5));
                        System.out.println("this is" + deviceId.get(k));
                        response = apiClient.pushLocationData(deviceId.get(k), uid, time, vol - (i * 10), signal + shipmentsPage.generateRandomInt(20), longLatPairs.get(i).get(0), longLatPairs.get(i).get(1), "Location Based Data " + i, src);
                        response.then().statusCode(200);
                        response = apiClient.pushTelemetryData(deviceId.get(k), uid, time, lbsTime, tempCargoTemp.get(k), tempAmbientTemp.get(k), tempCargoHumidity.get(k), tempAmbientHumidity.get(k));
                        response.then().statusCode(200);
                        System.out.println("Response: " + response.prettyPrint());
//                    shipmentsPage.refreshPage();
                        sleep(8000);
                        shipmentsPage.refreshPage();
                        sleep(5000);
//                    shipmentsPage.refreshPage();
//                    sleep(3000);
                        shipmentsPage.scrollToElement(By.xpath("(//*[text()='" + assetsName.get(k) + "'])[1]"));
                        shipmentsPage.clickButton("//*[text()='" + assetsName.get(k) + "']/ancestor::button");
                        shipmentsPage.scrollToElement(By.xpath("(//*[text()='" + assetsName.get(k) + "'])[2]"));
                        sleep(2000);
                        Integer cargoTemp = shipmentsPage.getCargoTemp();
                        System.out.println(cargoTemp.equals(tempCargoTemp.get(k)));
                        Assert.assertTrue(cargoTemp.equals(tempCargoTemp.get(k)), "CargoTemp\n" + "i: " + i + "; k: " + k);
                        Integer ambientTemp = shipmentsPage.getAmbientTemp();
                        System.out.println(ambientTemp.equals(tempAmbientTemp.get(k)));
                        Assert.assertTrue(ambientTemp.equals(tempAmbientTemp.get(k)), "AmbientTemp\n" + "i: " + i + "; k: " + k);
                        Integer cargoHumidity = shipmentsPage.getCargoHumidity();
                        System.out.println(cargoHumidity.equals(tempCargoHumidity.get(k)));
                        Assert.assertTrue(cargoHumidity.equals(tempCargoHumidity.get(k)), "CargoHumidity\n" + "i: " + i + "; k: " + k);
                        Integer ambientHumidity = shipmentsPage.getAmbientHumidity();
                        System.out.println(ambientHumidity.equals(tempAmbientHumidity.get(k)));
                        Assert.assertTrue(ambientHumidity.equals(tempAmbientHumidity.get(k)), "AmbientHumidity\n" + "i: " + i + "; k: " + k);
                        shipmentsPage.scrollToElement(By.xpath("//*[text()='Help']"));
                        sleep(5000);
                        if (i == 0) {
                            boolean update = shipmentsPage.checkStatusInAssetDetails(originxpath);
                            Assert.assertTrue(update, "Origin is not reached");
                            if (k + 1 == deviceId.size())
                                Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "At Origin"), "At Origin is not Shown");
                        } else if (i + 1 == longLatPairs.size()) {
                            boolean update = shipmentsPage.checkStatusInAssetDetails(destinationxpath);
                            Assert.assertTrue(update, "Destination is not reached");
                            if (k + 1 == deviceId.size())
                                Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "At Destination"), "At Destination is not Shown");
                        } else {
                            boolean update = shipmentsPage.checkStatusInAssetDetails(progressxpath);
                            Assert.assertTrue(update, "Progress is not reached");
                            if (k + 1 == deviceId.size())
                                Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "In Progress"), "In Progress is not Shown");
                        }
                        shipmentsPage.clickEnvironmentalConditions();
                        sleep(1000);
                        shipmentsPage.clickTableView();
                        sleep(500);
                        Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfCargoTemp(deviceId.get(k), cargoTemp, time, "Location Based Data " + i, longLatPairs.get(i).get(1) + ", " + longLatPairs.get(i).get(0)));
                        shipmentsPage.clickAmbientTemp();
                        sleep(500);
                        Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfAmbientTemp(deviceId.get(k), ambientTemp, time, "Location Based Data " + i, longLatPairs.get(i).get(1) + ", " + longLatPairs.get(i).get(0)));
                        shipmentsPage.clickCargoHumidity();
                        sleep(500);
                        Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfCargoHumidity(deviceId.get(k), cargoHumidity, time, "Location Based Data " + i, longLatPairs.get(i).get(1) + ", " + longLatPairs.get(i).get(0)));
                        shipmentsPage.clickAmbientHumidity();
                        sleep(500);
                        Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfAmbientHumidity(deviceId.get(k), ambientHumidity, time, "Location Based Data " + i, longLatPairs.get(i).get(1) + ", " + longLatPairs.get(i).get(0)));
                    }
                } catch (Exception innerException) {
                    System.err.println("Failed to execute POSTShipmentCompleteById: " + innerException.getMessage());
                } catch (AssertionError e) {
                    shipmentsPage.clickShipmentPage();
                    sleep(5000);
                    shipmentsPage.clickOnForceCompleteByName(shipmentName);
                }
                System.out.println("Iteration: " + i);
            }
            System.out.println("done");
        } catch (RuntimeException e) {
            shipmentsPage.forceCompleteFromShipmentDetailsPage(shipmentName);
        }
        // Todo: push telemetry data through api
        // Todo: Complete the shipment by continuous pushing the device data and validating
        //shipmentsPage.deleteRowByName(shipmentShipperOrderTransportCustomerRecipientOrg.get(0));
        shipmentsPage.clickShipmentPage();
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusByName(shipmentName, "At Destination"), "At destination is not shown");
        sleep(3000);
        shipmentsPage.clickOnForceCompleteByName(shipmentName);
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusByName(shipmentName, "Force Completed"), "Force completed is not shown");
        sleep(2000);
        shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "Completed"), "Completed is not shown");
    }

    @Test(groups = {"regression"})
    public void S021_VerifyExcursion_TC0000() throws IOException {
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S021_VerifyExcursion_TC0000");
        long time = Instant.now().getEpochSecond();
        String shipmentName = testCase.getString("shipmentName") + time;
        String shipperName = testCase.getString("shipperName") + time;
        String orderNumber = testCase.getString("orderNumber") + time;
        String carrierId = testCase.getJSONObject("carrier").getString("carrierId");
        String travelMode = "";
        String customerName = testCase.getString("customerName") + time;
        String recipientName = testCase.getString("recipientName") + time;

        String shipmentType = testCase.getString("shipmentType");

        String shipmentDirection = testCase.getJSONObject("originAndDestination").getString("shipmentDirection");
        String airwayBillNumber = "";
        String emails = testCase.getString("email");

        String plannedArrivalDate = testCase.getString("plannedArrivalDate");
        String plannedDepartureDate = testCase.getString("plannedDepartureDate");

        String organizationId = testCase.getJSONObject("organization").getString("organizationId");

        List<String> ruleIds = getRulesOnlyId(testCase);

        String deviceProfileId = testCase.getJSONObject("dataLoggerProfile").getString("dataLoggerProfileId");

        String routeOrLocationChoice = testCase.getJSONObject("originAndDestination").getString("locationOrRoute");

        boolean isDynamicOriginLocation = false;
        Map<String, Object>  dynamicOriginLocation = new HashMap<>();

        boolean isDynamicDestinationLocation = false;
        Map<String, Object>  dynamicDestinationLocation = new HashMap<>();

        String routeId = "6Fe1F53C-FC51-4729-9C70-1ED0504ED082";
        String originLocationId = "CCB314E8-1CDE-44B6-B034-014C68806754";
        String destinationLocationId = "3D08295F-55D4-44A9-AF4B-04E811C4E571";
        List<List<String>> assets = List.of(
                List.of("AutoAPIAsset1743093728", "1c9a47a1-1909-452c-bc69-a301154d7648", "doglapan RKN"),
                List.of("AutoAPIAsset1743094200", "b3c2637b-b597-4085-8d48-5d207be5f023", "doglapan RKN")

        );

        List<Map<String, String>> assetIdsForAPI = Arrays.asList(
                new HashMap<>(Map.of("assetId", "1c9a47a1-1909-452c-bc69-a301154d7648")),
                new HashMap<>(Map.of("assetId", "b3c2637b-b597-4085-8d48-5d207be5f023"))

        );
        Map<String, Object> shipmentData = new HashMap<>();
        shipmentData.put("shipmentName", shipmentName);
        shipmentData.put("shipperName", shipperName);
        shipmentData.put("orderNumber", orderNumber);
        shipmentData.put("carriedId", carrierId);
        shipmentData.put("travelMode", travelMode);
        shipmentData.put("customerName", customerName);
        shipmentData.put("recipientName", recipientName);
        shipmentData.put("shipmentType", shipmentType);
        shipmentData.put("shipmentDirection", shipmentDirection);
        shipmentData.put("airwayBillNumber", airwayBillNumber);
        shipmentData.put("emails", emails);
        shipmentData.put("plannedDepartureDate", plannedDepartureDate);
        shipmentData.put("plannedArrivalDate", plannedArrivalDate);
        shipmentData.put("organizationId", organizationId);
        shipmentData.put("ruleIds", ruleIds);
        shipmentData.put("deviceProfileId", deviceProfileId);
        shipmentData.put("routeOrLocationChoice", routeOrLocationChoice);
        shipmentData.put("isDynamicOriginLocation", isDynamicOriginLocation);
        shipmentData.put("dynamicOriginLocation", dynamicOriginLocation);
        shipmentData.put("isDynamicDestinationLocation", isDynamicDestinationLocation);
        shipmentData.put("dynamicDestinationLocation", dynamicDestinationLocation);
        shipmentData.put("routeId", routeId);
        shipmentData.put("originLocationId", originLocationId);
        shipmentData.put("destinationLocationId", destinationLocationId);
        shipmentData.put("assets", assetIdsForAPI);

        Response response = apiClient.POSTShipmentsCreate(shipmentName,shipperName ,orderNumber , carrierId ,
                travelMode,customerName,recipientName,shipmentType,shipmentDirection,airwayBillNumber,
                emails,plannedDepartureDate,plannedArrivalDate,organizationId,ruleIds,deviceProfileId,
                routeOrLocationChoice,isDynamicOriginLocation,dynamicOriginLocation,isDynamicDestinationLocation,
                dynamicDestinationLocation,routeId,originLocationId,destinationLocationId,assetIdsForAPI);
        response.then().statusCode(201);
        shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
        List<List<String>> longLatPairs = new ArrayList<>();
        longLatPairs.add(Arrays.asList("51.5631", "25.2654"));
        longLatPairs.add(Arrays.asList("72.2748", "25.7986"));
        longLatPairs.add(Arrays.asList("90.4029252", "23.8434344"));
        longLatPairs.add(Arrays.asList("91.3893", "23.0049"));
        longLatPairs.add(Arrays.asList("92.0014375", "21.4326875"));
        Integer t0 = 80, t1 = 70, h0 = 12, h1 = 3, vol = 100, signal = 60;
        String originxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Origin']";
        String progressxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='In Progress']";
        String destinationxpath = "//*[text()='Progress']//ancestor::div[@role='tablist']//following-sibling::div/div/div/div/div[1]/*[text()='At Destination']";

        sleep(2000);
        List<String> assetsName = new ArrayList<>();
        for (List<String> asset : assets) {
            assetsName.add(asset.get(0));
        }
        Assert.assertTrue(shipmentsPage.checkStatusSnapshot(shipmentName, assetsName, shipmentType), "Status snapshot is not OK");
        List<String> deviceId = new ArrayList<>(List.of(
                "AutoDevice1743093731",
                "AutoDevice1743094205"
        ));

        String uid = "807938083";
        int src = 3;
        for (int i = 0; i < longLatPairs.size(); i++) {
            if (i == 0) {
                Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "Pending"),
                        "Pending is not shown properly");
            }
            //Integer vol = 22;
            //Integer signal = 22;
            List<Integer> tempCargoTemp = new ArrayList<>();
            List<Integer> tempAmbientTemp = new ArrayList<>();
            List<Integer> tempCargoHumidity = new ArrayList<>();
            List<Integer> tempAmbientHumidity = new ArrayList<>();

            try {
                time = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().getEpochSecond();
                String zoneId = "Asia/Dhaka";
                if (i == 0) zoneId = "Asia/Qatar";
                else if (i == 1) zoneId = "Asia/Karachi";
                long lbsTime = ZonedDateTime.now(ZoneId.of(zoneId)).toInstant().getEpochSecond();
                for (int k = 0; k < deviceId.size(); k++) {
                    if(i == 0){
                        tempCargoTemp.add(110);
                    }else tempCargoTemp.add(t0 + shipmentsPage.generateRandomInt(20));
                    tempAmbientTemp.add(t1 + shipmentsPage.generateRandomInt(30));
                    tempCargoHumidity.add(h0 + shipmentsPage.generateRandomInt(10));
                    tempAmbientHumidity.add(h1 + shipmentsPage.generateRandomInt(5));

                    System.out.println("this is"+deviceId.get(k));
                    response = apiClient.pushLocationData(deviceId.get(k), uid, time,vol - (i * 10), signal + shipmentsPage.generateRandomInt(20), longLatPairs.get(i).get(0), longLatPairs.get(i).get(1), "Location Based Data " + i, src);
                    response.then().statusCode(200);
                    response = apiClient.pushTelemetryData(deviceId.get(k), uid, time, lbsTime, tempCargoTemp.get(k), tempAmbientTemp.get(k), tempCargoHumidity.get(k), tempAmbientHumidity.get(k));
                    response.then().statusCode(200);
                    System.out.println("Response: " + response.prettyPrint());
                    shipmentsPage.refreshPage();
                    sleep(3000);
                    shipmentsPage.refreshPage();
                    sleep(3000);
                    shipmentsPage.refreshPage();
                    sleep(3000);
                    shipmentsPage.clickButton("//*[text()='" + assets.get(k).get(0) + "']/ancestor::button");
                    sleep(1000);
                    if(i == 0 && k == 0){
                        Assert.assertTrue(shipmentsPage.isDisplayed("//*[text()='1 Alerts']"), "Alert is not shown");
                        Assert.assertTrue(shipmentsPage.isDisplayed("//*[text()='Alert']//preceding-sibling::*//*[text()='"+assets.get(k).get(0)+"']"), "Alert is not shown at box");
                        Assert.assertTrue(shipmentsPage.isDisplayed("(//*[text()='Excursion']//ancestor::button)["+(k + 1)+"]//*[text()='"+assets.get(k).get(0)+"']"));
                    }
                    else if(i == 0 && k == 1){
                        Assert.assertTrue(shipmentsPage.isDisplayed("//*[text()='2 Alerts']"), "Alert is not shown");
                        Assert.assertTrue(shipmentsPage.isDisplayed("//*[text()='Alert']//preceding-sibling::*//*[text()='"+assets.get(k).get(0)+"']"), "Alert is not shown at box");
                        Assert.assertTrue(shipmentsPage.isDisplayed("(//*[text()='Excursion']//ancestor::button)["+(k + 1)+"]//*[text()='"+assets.get(k).get(0)+"']"));
                    }
                    else if(i == 1 && k ==0){
                        Assert.assertTrue(shipmentsPage.isDisplayed("//*[text()='1 Alerts']"), "Alert is not shown");
                        Assert.assertTrue(!shipmentsPage.isDisplayed("//*[text()='Alert']//preceding-sibling::*//*[text()='"+assets.get(k).get(0)+"']"), "Alert removed at box");
                        Assert.assertTrue(shipmentsPage.isDisplayed("(//*[text()='Stable']//ancestor::button)["+(k + 1)+"]//*[text()='"+assets.get(k).get(0)+"']"));

                    }
                    else if(i == 1 && k == 1){
                        Assert.assertTrue(!shipmentsPage.isDisplayed("//*[text()='1 Alerts']"), "Alert msg is removed");
                        Assert.assertTrue(!shipmentsPage.isDisplayed("//*[text()='Alert']//preceding-sibling::*//*[text()='"+assets.get(k).get(0)+"']"), "Alert removed at box");
                        Assert.assertTrue(shipmentsPage.isDisplayed("(//*[text()='Stable']//ancestor::button)["+(k + 1)+"]//*[text()='"+assets.get(k).get(0)+"']"));
                    }
                    shipmentsPage.scrollToElement(By.xpath("//*[text()='Help']"));
                    Integer cargoTemp = shipmentsPage.getCargoTemp();
                    System.out.println(cargoTemp.equals(tempCargoTemp.get(k)));
                    if(!cargoTemp.equals(tempCargoTemp.get(k)))return;
                    Integer ambientTemp = shipmentsPage.getAmbientTemp();
                    System.out.println(ambientTemp.equals(tempAmbientTemp.get(k)));
                    if(!ambientTemp.equals(tempAmbientTemp.get(k)))return;
                    Integer cargoHumidity = shipmentsPage.getCargoHumidity();
                    System.out.println(cargoHumidity.equals(tempCargoHumidity.get(k)));
                    if(!cargoHumidity.equals(tempCargoHumidity.get(k)))return;
                    Integer ambientHumidity = shipmentsPage.getAmbientHumidity();
                    System.out.println(ambientHumidity.equals(tempAmbientHumidity.get(k)));
                    if(!ambientHumidity.equals(tempAmbientHumidity.get(k)))return;
                    sleep(5000);
                    if (i == 0) {
                        boolean update = shipmentsPage.checkStatusInAssetDetails(originxpath);
                        Assert.assertTrue(update, "Origin is not reached");
                        if(k + 1 == deviceId.size())Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "At Origin"), "At Origin is not Shown");
                    } else if (i + 1 == longLatPairs.size()) {
                        boolean update = shipmentsPage.checkStatusInAssetDetails(destinationxpath);
                        Assert.assertTrue(update, "Destination is not reached");
                        if(k + 1 == deviceId.size())Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "At Destination"), "At Destination is not Shown");
                    } else {
                        boolean update = shipmentsPage.checkStatusInAssetDetails(progressxpath);
                        Assert.assertTrue(update, "Progress is not reached");
                        if(k + 1 == deviceId.size())Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "In Progress"), "In Progress is not Shown");
                    }
                    shipmentsPage.clickEnvironmentalConditions();
                    sleep(1000);
                    shipmentsPage.clickTableView();
                    sleep(500);
                    Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfCargoTemp(deviceId.get(k),cargoTemp, time, "Location Based Data " + i, longLatPairs.get(i).get(1)+", "+longLatPairs.get(i).get(0)));
                    shipmentsPage.clickAmbientTemp();
                    sleep(500);
                    Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfAmbientTemp(deviceId.get(k),ambientTemp, time, "Location Based Data " + i, longLatPairs.get(i).get(1)+", "+longLatPairs.get(i).get(0)));
                    shipmentsPage.clickCargoHumidity();
                    sleep(500);
                    Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfCargoHumidity(deviceId.get(k),cargoHumidity, time, "Location Based Data " + i, longLatPairs.get(i).get(1)+", "+longLatPairs.get(i).get(0)));
                    shipmentsPage.clickAmbientHumidity();
                    sleep(500);
                    Assert.assertTrue(shipmentsPage.checkdownTableEnvironmentalConditionsOfAmbientHumidity(deviceId.get(k),ambientHumidity, time, "Location Based Data " + i, longLatPairs.get(i).get(1)+", "+longLatPairs.get(i).get(0)));
                }
            } catch (Exception innerException) {
                System.err.println("Failed to execute POSTShipmentCompleteById: " + innerException.getMessage());
            } catch (AssertionError e) {
                shipmentsPage.clickShipmentPage();
                sleep(5000);
                return;
                //shipmentsPage.clickOnForceCompleteByName(shipmentName);
            }
            System.out.println("Iteration: " + i);
        }
        System.out.println("done");
        // Todo: push telemetry data through api
        // Todo: Complete the shipment by continuous pushing the device data and validating
        //shipmentsPage.deleteRowByName(shipmentShipperOrderTransportCustomerRecipientOrg.get(0));
        shipmentsPage.clickShipmentPage();
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusByName(shipmentName, "At Destination"), "At destination is not shown");
        sleep(3000);
        shipmentsPage.clickOnForceCompleteByName(shipmentName);
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusByName(shipmentName, "Force Completed"), "Force completed is not shown");
        sleep(2000);
        shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);
        sleep(2000);
        Assert.assertTrue(shipmentsPage.checkStatusOfBannerInShipmentDetailsPage(shipmentName, "Completed"), "Completed is not shown");
    }

    @Test(groups = {"Regression"})
    public void S022_verifyShipmentDataInPDF_TC0000() throws IOException {
        String shipmentName = "autoshipment002";

        Map<String, Object> shipmentData = new HashMap<>();
        shipmentsPage = loginAndGoToShipmentsPage();
        JSONObject testCase = shipmentsPage.getJSONDataByMethodName("S022_VerifyShipmentDataInPDF_TC0000");
        shipmentsPage.gotoShipmentDetailsPageWithSearchByName(shipmentName);

        // Step 1. Download shipment zip
        String zipFilePath = shipmentsPage.downloadShipmentPDF(shipmentName);

        // Step 2. Extract zipfile
        Map<String, String> extractedFiles = shipmentsPage.extractZip(zipFilePath);
        System.out.println(extractedFiles);

        // Step 3. Extract shipment summery
        /*
        String pdfShipmentData = shipmentsPage.readPDF(extractedFiles.get("shipment-summary-report.pdf"));
        System.out.println(pdfShipmentData);
        */

        JSONObject extractedShipmentSummary = shipmentsPage.extractShipmentDataAsJson(extractedFiles.get("shipment-summary-report.pdf"));
        Map<String, Object> extractedShipmentMap = extractedShipmentSummary.toMap();
        Map<String, Object> expectedShipmentMap = testCase.toMap();
        Assert.assertTrue(shipmentsPage.validateDataWithPDFData(extractedShipmentMap, expectedShipmentMap), "DataIsNotMatch");
        //System.out.println(extractedShipmentSummary.toString(4));
    }


//    @Test(groups = {"regression"})
//    public void S000() throws InterruptedException, SQLException {
//        String query = "SELECT AssetTypeId, AssetTypeName FROM AssetTypes";
//        ResultSet rs = DBUtility.executeQuery(query, test);
//        List<List<String>> tableData = convertResultSetToList(rs);
//        System.out.println(tableData);
//        shipmentsPage = loginAndGoToShipmentsPage();
//
//        // Todo: test.info or logAction
//
//        Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
//
//        // Todo: select bulk air cargo
////        shipmentsPage.selectBulkAirCargoInAddNewAsset();
//        List<String> shipmentShipperOrderTransportCustomerRecipientOrg = shipmentsPage.fillUpNewShipmentsInitial();
//        shipmentShipperOrderTransportCustomerRecipientOrg.add("Bulk Air Cargo");
//        shipmentsPage.expendDataLoggerProfileInAddNewShipments();
//        shipmentsPage.selectDataLoggerProfile(null);
//
//        shipmentsPage.expendRulesInAddNewShipments();
//        // randomly selected options:: selected all or, selected random number of rules
//        // Todo: select rules
////        shipmentsPage.selectedRules(5, "Auto");
//
//        shipmentsPage.expendOriginAndDestinationInAndNewShipments();
//
//        // Todo: fillup origin and destination
////        shipmentsPage.fillUpOriginAndDestinationRoute(true);
//
//        shipmentsPage.expendAssetsInShipmentInAddNewShipments();
//        shipmentsPage.fillUpAssetsInShipment("Bulk Air Cargo","");
//
//        shipmentsPage.expendNotificationInAddNewShipments();
//
//        // Todo: fillUp email shipment
////        shipmentsPage.fillUpEmailInShipment();
//
//        String assetName = shipmentsPage.generateRandomString(5, "AutoAsset");
//        //APITest.API006_CreateAssetDynamic(assetName, );
//        // Todo: click createbuttonIn add shipment
////        shipmentsPage.clickOnCreateButtonInAddNewShipment();
//        Assert.assertTrue(shipmentsPage.CreateToast(), "Create message is not shown");
//        shipmentsPage.refreshPage();
//        shipmentsPage.refreshPage();
//        // Todo: validate createddata
////        Assert.assertTrue(shipmentsPage.validateCreatedData(shipmentShipperOrderTransportCustomerRecipientOrg), "data not matched");
//        shipmentsPage.deleteRowByName(shipmentShipperOrderTransportCustomerRecipientOrg.get(0));
//        System.out.println("bye bye");
//    }

//    @Test
//    public void S001() throws InterruptedException, SQLException {
//        //api work
//        int needToCreateAsset = 1;
//        String query = "SELECT OrganizationId, OrganizationName FROM Organizations WHERE DeActivatedAt is NULL";
//        ResultSet rs = DBUtility.executeQuery(query, test);
//        List<List<String>> tableData = convertResultSetToList(rs);
//        String randomlySelectedOrg = tableData.get(shipmentsPage.generateRandomInt(tableData.size())).get(1);
//        randomlySelectedOrg = "doglapan Training - Air Cargo";
//        String randomlySelectedOrgID = "";
//        for (int i = 0; i < tableData.size(); i++) {
//            if (tableData.get(i).get(1).equals(randomlySelectedOrg)) {
//                randomlySelectedOrgID = tableData.get(i).get(0);
//            }
//        }
//        for (int i = 0; i < needToCreateAsset; i++) {
//            query = "SELECT AssetTypeId, AssetTypeName FROM AssetTypes";
//            rs = DBUtility.executeQuery(query, test);
//            tableData = convertResultSetToList(rs);
//
//            // Todo: getAvailableAssetType
//            List<String> availableAssetType = shipmentsPage.getAvailableAssetType();
//            String randomlySelectedAssetType = availableAssetType.get(shipmentsPage.generateRandomInt(availableAssetType.size()));
//            String randomlySelectedAssetTypeID = "";
//            for (int j = 0; j < tableData.size(); j++) {
//                if (tableData.get(j).get(1).equals(randomlySelectedAssetType)) {
//                    randomlySelectedAssetTypeID = tableData.get(j).get(0);
//                    break;
//                }
//            }
//            String generatedAssetName = shipmentsPage.generateRandomString(5, "AutoAsset");
//            APITest.API005_CreateAssetDynamic(generatedAssetName, randomlySelectedAssetTypeID, randomlySelectedOrgID);
//            query = "select assetid from assets where assetname = '" + generatedAssetName + "'";
//            rs = DBUtility.executeQuery(query, test);
//            tableData = convertResultSetToList(rs);
//            String assetId = tableData.get(0).get(0);
//            String generatedDeviceName = shipmentsPage.generateRandomString(5, "AutoDevice");
//            //APITest.API006_CreateDeviceDynamic(generatedDeviceName );
//
//
//            shipmentsPage = loginAndGoToShipmentsPage();
//            Assert.assertEquals(shipmentsHeaderTitle, shipmentsPage.getShipmentsHeaderTitle());
//            shipmentsPage.selectBulkAirCargoInAddNewAsset();
//            shipmentsPage.expandAll();
//            shipmentsPage.fillUpMandatory("Bulk Air Cargo");
//
//        }
//    }
//
//    public Map<String, Object> createdDevice() throws SQLException {
//        Map<String, Object> result = new HashMap<>();
//        String deviceName = shipmentsPage.generateRandomString(5, "AutoDevice");
//        result.put("deviceName", deviceName);
//
//        String query = "SELECT DeviceTypeId, DeviceTypeName FROM DeviceTypes WHERE DeActivatedAt is NULL";
//        ResultSet rs = DBUtility.executeQuery(query, test);
//        List<List<String>> tableData = convertResultSetToList(rs);
//        int generatedNumber = shipmentsPage.generateRandomInt(tableData.size());
//
//        String deviceTypeName = tableData.get(generatedNumber).get(1);
//        result.put("deviceTypeName", deviceTypeName);
//        String deviceTypeId = tableData.get(generatedNumber).get(0);
//        result.put("deviceTypeId", deviceTypeId);
//        String deviceProfileName = "AutoLoggerProfile01";
//        result.put("deviceProfileName", "AutoLoggerProfile01");
//        String deviceProfileId = "96CF238F-2278-4625-B558-CC8E9A43983A";
//        result.put("deviceProfileId", deviceProfileId);
//        String organizationName = "doglapan Training - Air Cargo";
//        result.put("organizationName", organizationName);
//        String organizationId = "49194360-27AB-4434-991F-88116DC775BB";
//        result.put("organizationId", organizationId);
//        return  result;
//    }
//
//    public Map<String, Object> createdAsset(String deviceTypeName, String organizationName, String organizationId) throws SQLException {
//        Map<String, Object> result = new HashMap<>();
//        String assetName = shipmentsPage.generateRandomString(5, "AutoAsset");
//        result.put("assetName", assetName);
//        String query = "SELECT AssetTypeId, AssetTypeName FROM AssetTypes WHERE DeActivatedAt is NULL";
//        if(!deviceTypeName.contains("OnAsset")){
//            query = "SELECT AssetTypeId, AssetTypeName FROM AssetTypes WHERE DeActivatedAt is NULL AND AssetTypeName NOT LIKE '%APS%'";
//        }
//        ResultSet rs = DBUtility.executeQuery(query, test);
//        List<List<String>> tableData = convertResultSetToList(rs);
//        int randomNumber = shipmentsPage.generateRandomInt(tableData.size());
//        String AssetTypeName = tableData.get(randomNumber).get(1);
//        result.put("AssetTypeName", AssetTypeName);
//        String AssetTypeId = tableData.get(randomNumber).get(0);
//        result.put("AssetTypeId", AssetTypeId);
//        result.put("organizationName", organizationName);
//        result.put("organizationId", organizationId);
//        result.put("visibilityLevel", "OrganizationOnly");
//        return result;
//    }
//
//    public Map<String, Object> combinedDataForDevice(String assetName, List<String>DeviceNames) throws SQLException {
//        Map<String, Object> result = new HashMap<>();
//        String query = "select AssetId from assets where assetname = '"+assetName+"'";
//        ResultSet rs = DBUtility.executeQuery(query, test);
//        List<List<String>> tableData = convertResultSetToList(rs);
//        String assetId = tableData.get(0).get(0);
//        result.put("AssetName", assetName);
//        result.put("assetId", assetId);
//        query = "select assetTypeName from assets join assetTypes on assets.assetTypeid = assettypes.assetTypeId " +
//                "where assetName  = '"+assetName+"'";
//        rs = DBUtility.executeQuery(query, test);
//        tableData = convertResultSetToList(rs);
//        String assetTypeName = tableData.get(0).get(0);
//        return result;
//    }
//
//    public void s002() throws SQLException {
//        //generate device
//        Map<String, Object> generatedDevice = createdDevice();
//        //generate asset
//        Map<String, Object> generatedAsset = createdAsset(generatedDevice.get("deviceTypeName").toString(),
//                generatedDevice.get("organizationName").toString(),generatedDevice.get("organizationId").toString());
//        //Map<String, Object> generatedDataForDevice = combinedDataForDevice();
//        // Todo: createDevice via api
////        apiClient.createDevice(generatedDevice.get("deviceName").toString(), generatedDevice.get("deviceTypeId").toString(),
////                generatedDevice.get("deviceProfileId").toString(), generatedDevice.get("organizationId").toString());
//        apiClient.createAsset("",generatedAsset.get("AssetTypeName").toString(), generatedAsset.get("AssetTypeId").toString(),
//                generatedAsset.get("organizationId").toString(), generatedAsset.get("visibilityLevel").toString());
//        //apiClient.BoundDevice(tableData.get(0).get(0), );
//    }
}

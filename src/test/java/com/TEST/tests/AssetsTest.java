package com.TEST.tests;

import com.TEST.pages.AssetsPage;
import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import io.restassured.response.Response;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.TEST.pages.BasePage.sleep;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class AssetsTest extends BaseTest {
    private AssetsPage assetPage;

    public AssetsPage loginAndGoToAssetsPage() {
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
        test.info("Going to Assets Page...");
        assetPage = dashboardPage.goToAssetPage();
        test.info("At the Assets Page Now.");

        return assetPage;
    }

    @Test(groups = {"regression"})
    public void A001_VerifyComponentsOfAddNewAsset_TC4568() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        //Scenario 1
        //list of asset types

        String selectedAsset = assetPage.selectedAssetType();
        Assert.assertTrue(assetPage.selectDropdownOptionForAsset(selectedAsset), "Dropdown 'New Asset' is not Displayed and Clicked");
        //Scenario 2
        Assert.assertTrue(assetPage.isPopupDisplayed(), "Popup is not displayed");
        //Scenario 3
        Assert.assertTrue(assetPage.isCardTitleCorrect(selectedAsset), "Card title is not Correct");
        //Scenario 4
        Assert.assertTrue(assetPage.isAssetNameHeaderDisplayed(), "Asset Name header is not displayed.");
        Assert.assertTrue(assetPage.isOrganizationHeaderDisplayed(), "Organization header is not displayed.");
        Assert.assertTrue(assetPage.isCreateButtonDisplayed(), "Create Button is not displayed");
        Assert.assertTrue(assetPage.isCancelButtonDisplayed(), "Cancel Button is not displayed");
        //Scenario 5
        Assert.assertTrue(assetPage.clickCancelButton(), "Clicked Cancel button");
    }

    @Test(groups = {"regression"})
    public void A002_VerifyComponentsOfAssetPage_TC4542() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        // Scenario 1
        Assert.assertTrue(assetPage.checkPageTitle(), "Page title is not found");
        // Scenario 2
        Assert.assertTrue(assetPage.checkNewAssetButton(), "New Asset is not found");
        // Scenario 3
        Assert.assertTrue(assetPage.checkBulkActions(), "Bulk Action Icon is not found");
        Assert.assertTrue(assetPage.checkBulkOptions(), "Bulk Options is missing");
        // Scenario 4
        Assert.assertTrue(assetPage.checkDownloadIcon(), "Download icon is missing");
        // Scenario 5
        Assert.assertTrue(assetPage.checkMultipleDeleteButton(), "Multiple Delete Button is missing");
        Assert.assertTrue(assetPage.checkDeleteSelectedFromMultipleDeleteClick(), "Delete Selecting is not found");
        // Scenario 6
        Assert.assertTrue(assetPage.tableConfigIconButton(), "Table Config Icon is missing");
        // Scenario 7
        Assert.assertTrue(assetPage.filterButton(), "Filter Dropdown Button is missing");
        // Scenario 8
        Assert.assertTrue(assetPage.checkGridHeader(), "Grid header is missing");
        // Scenario 9
        Assert.assertTrue(assetPage.checkSearchIcon(), "Search icon status mismatch");
    }

    @Test(groups = {"regression"})
    public void A003_VerifyDataGridOnAssetPage_TC4543() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        //Scenario 1
        Assert.assertTrue(assetPage.checkGridHeaderSequenceVerify(), "Headers are not in the correct sequence");
        //Scenario 2
        Assert.assertTrue(assetPage.checkActionsIcons(), "One of Actions icon is missing");
        //Scenario 3
        Assert.assertTrue(assetPage.checkFirstPage(), "Any of option is missing");
    }

    @Test(groups = {"regression"})
    public void A004_VerifyTheComponentsOnTheAssetDetails_GeneralInfo_TC4544() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        String nameA = assetPage.GetRandomName();
        String nameB;
        do {
            nameB = assetPage.GetRandomName();
        } while (nameA.equals(nameB));
        //asset details page
        for (int i = 0; i < 2; i++) {
            String str;
            if (i == 1) str = nameB;
            else str = nameA;
            assetPage.ClickExpandOnName(str);
            sleep(3000);
            //Scenario 1
            Assert.assertTrue(assetPage.checkAssetDetailsPageTitle(str), "Name or Assets word is missing");
            //Scenario 2
            Assert.assertTrue(assetPage.checkGerneralInfoActive(), "General info is not activate");
            //Scenario 3
            Assert.assertTrue(assetPage.checkLeftAlignImage(), "Left Align image is missing");
            //Scenario 4
            Assert.assertTrue(assetPage.checkRightAlignlabels(), "Right Align labels are not correct");
            if (i == 0) assetPage.backToAssetPage();
        }
    }

    @Test (groups = {"regression"})
    public void A005_VerifyTheCorrespondingDataOnTheAssetDetails_GenInfo_TC4558() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        String name = assetPage.GetRandomName();
        String AssetTypeOnAssetPage = assetPage.getCellDataByName(name, "Asset Type");
        String MonitoredStatus = assetPage.getCellDataByNameForMonitoredStatus(name);
        String Geolocation = assetPage.getCellDataByName(name, "Geolocation");
        String Created_At = assetPage.getCellDataByName(name, "Created At");
        String Created_By = assetPage.getCellDataByName(name, "Created By");
        String Last_Modified_At = assetPage.getCellDataByName(name, "Last Modified At");
        String Last_Modified_By = assetPage.getCellDataByName(name, "Last Modified By");
        assetPage.ClickExpandOnName(name);
        sleep(5000);
        String AssetName_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Asset Name");
        String AssetType_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Asset Type");
        String MonitoredStatus_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Monitored Status");
        //String Geolocation_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Geolocation");
        String LastLocation_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Last Location");
        String CreatedAt_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Created At");
        String CreatedBy_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Created By");
        String LastModifiedAt_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Last Modified At");
        String LastModifiedBy_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Last Modified By");
        //Scenario 1
        Assert.assertTrue(assetPage.checkStringType(AssetName_GI), "Not A String");
        Assert.assertTrue(assetPage.checkStringType(AssetType_GI), "Not A String");
        Assert.assertTrue(assetPage.checkStringType(MonitoredStatus_GI), "Not A String");
        //Assert.assertTrue(assetPage.checkStringType(Geolocation_GI), "Not A String");
        Assert.assertTrue(assetPage.checkStringType(LastLocation_GI), "Not A String");
        Assert.assertTrue(assetPage.checkDateTimeType(CreatedAt_GI), "Not DateTime Type");
        Assert.assertTrue(assetPage.checkStringType(CreatedBy_GI), "Not A String");
        Assert.assertTrue(assetPage.checkStringType(LastModifiedAt_GI), "Not DateTime Type");
        Assert.assertTrue(assetPage.checkStringType(LastModifiedBy_GI), "Not A String");
        //Scenario 2
        Assert.assertTrue(assetPage.checkString(name, AssetName_GI), "Asset Name Not Match");
        //Scenario 3
        Assert.assertTrue(assetPage.checkString(AssetTypeOnAssetPage, AssetType_GI), "Asset Type Not Match");
        //Scenario 4
        Assert.assertTrue(assetPage.checkString(MonitoredStatus, MonitoredStatus_GI), "Monitored Status Not Match");
        //Scenario 5
        //Assert.assertTrue(assetPage.checkString(Geolocation, Geolocation_GI), "Geolocation Not Match");
        //Scenario 6
        Assert.assertTrue(assetPage.checkString(Created_At, CreatedAt_GI), "Created At Not Match");
        //Scenario 7
        Assert.assertTrue(assetPage.checkString(Created_By, CreatedBy_GI), "Created By Not Match");
        //Scenario 8
        Assert.assertTrue(assetPage.checkString(Last_Modified_At, LastModifiedAt_GI), "Last Modified At Not Match");
        //Scenario 9
        Assert.assertTrue(assetPage.checkString(Last_Modified_By, LastModifiedBy_GI), "Last Modified By Not Match");

        //        System.out.println(AssetTypeOnAssetPage);
        //        System.out.println(MonitoredStatus);
        //        System.out.println(Geolocation);
        //        System.out.println(Created_At);
        //        System.out.println(Created_By);
        //        System.out.println(Last_Modified_At);
        //        System.out.println(Last_Modified_By);
    }

    @Test(groups = {"regression"})
    public void A006_VerifyTheFieldLevelOnNewAssetModal_TC4653() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        String name = assetPage.GetRandomName();
        assetPage.clickNewAsset();
        String assetType = assetPage.SelectRandomAssetType();
        //Scenario 1
        Assert.assertTrue(assetPage.isAssetNameHeaderDisplayed(), "Asset Name with * is missing");
        //Scenario 2
        Assert.assertTrue(assetPage.isOrganizationHeaderDisplayed(), "Organization with * is missing");
        assetPage.clickCreateButton();
        //Scenario 3
        Assert.assertTrue(assetPage.AssetNameIsRequireMessage(), "Message is missing");
        //Scenario 4
        //Assert.assertTrue(assetPage.OrganizationIsRequireMessage(), "Message is missing");
        //Scenario 5
        Assert.assertTrue(assetPage.AssetNameCharacter3isRequireMessage(), "Message is missing");
        //Scenario 6
        Assert.assertTrue(assetPage.AssetNameBelowCharacter50isRequireMessage(), "Message is missing");
        assetPage.clickCancelButton();
        //Scenario 7 & 8
        assetPage.clickNewAsset();
        assetType = assetPage.SelectRandomAssetType();
        Assert.assertTrue(assetPage.checkDuplicate(), "Existing message is not shown");
    }

    @Test(groups = {"regression"})
    public void A007_VerifyAssetsGridAfterAdditionOfNewAsset_TC4655() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        List<String> NameOrganizationAssetTypeTimeUserName = assetPage.createAssetMethod();
        //Scenario 1 & Scenario 2
        Assert.assertTrue(assetPage.AssetAccept(), "Message is missing");
        System.out.println(NameOrganizationAssetTypeTimeUserName);
        sleep(5000);
        assetPage.refreshPage();
        //Scenario 3
        List<WebElement> observeData = assetPage.obtainPageData();
        //Scenario 4
        System.out.println(observeData);
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData,1, "Name", NameOrganizationAssetTypeTimeUserName.get(0)), "Name is not correct");
        //Scenario 4
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Organization", NameOrganizationAssetTypeTimeUserName.get(1)), "Organization is not correct");
        //Scenario 5 & 6
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Created By", NameOrganizationAssetTypeTimeUserName.get(4)), "Created By is not correct");
        //Scenario 5 & 6
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Last Modified By", NameOrganizationAssetTypeTimeUserName.get(4)), "Last Modified By is not correct");
        //Scenario 7 & 8
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Created At", NameOrganizationAssetTypeTimeUserName.get(3)), "Created At is not correct");
        //Scenario 7 & 8
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Last Modified At", NameOrganizationAssetTypeTimeUserName.get(3)), "Last Modified At is not correct");
        //Scenario 9
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1,"Asset Type", NameOrganizationAssetTypeTimeUserName.get(2)), "Asset Type is not correct");
        //Scenario 10
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Bound Data Logger(s)", "N/A"), "Bound Data Logger(s) is not correct");
        //Scenario 11
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Monitored Status", "Unmonitored"));
        //Scenario 12
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData, 1, "Geolocation", "Out of Coverage"));
        //Scenario 13
        Assert.assertTrue(assetPage.checkElementFromSpecificCell(observeData,1, "On Shipment", "No"));
        //deleting created data
        assetPage.deleteAssetByName(NameOrganizationAssetTypeTimeUserName.get(0));
        //todo:: have to validate from database
    }

    @Test(groups = {"regression"})
    public void A008_VerifyComponentsOnBoundDataLoggers_TC6040() {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        //String name = assetPage.GetRandomName();
        for (int i = 0; i < 2; i++) {
            String assetType = "doglapan APS-S";
            if(i == 1)assetType = "doglapan APS-D";
            assetPage.searchByName(assetType, "Asset Type");
            assetPage.searchByName("doglapan Training - Air Cargo", "Organization");
            String name = assetPage.GetRandomNameAPStype();

            while (true) {
                boolean situation = assetPage.haveBoundDataLoggers(name);
                if (situation) break;
                name = assetPage.GetRandomName();

            }

            assetPage.ClickExpandOnName(name);
            //Scenario 1
            Assert.assertTrue(assetPage.verifyBoundDataLoggers(), "Bound Data Logger(s) is header is missing");
            //Scenario 2
            Assert.assertTrue(assetPage.clickBoundDataLoggers(), "Bound Data Logger(s) is not clicked");
            //Scenario 3
            Assert.assertTrue(assetPage.verifyCurrentlyBound(), "Currently bound is not exist");
            //Scenario 4
            Assert.assertTrue(assetPage.verifyAvailableDataLoggers(), "Available Data Logger(s) is not exist");
            //Scenario 5
            Assert.assertTrue(assetPage.verifyMiddleSectionButtonIcon(), "Middle Section Button Icon is not exist");
            //Scenario 6
            int AvailableDataLoggers = assetPage.numberOfAvailableDataLoggers();
            //Scenario 7
            int CurrentlyBound = assetPage.numberOfCurrentlyBound();
            System.out.println(AvailableDataLoggers + " " + CurrentlyBound);
            //Scenario 8
            Assert.assertTrue(assetPage.verifyColumnNamesOfCurrentlyBound(), "Column Names Are not Accurate or missing");
            //Scenario 9
            Assert.assertTrue(assetPage.observeActionColumnContains(), "Unbind icon is missing");
            //Scenario 10
            Assert.assertTrue(assetPage.SearchIconInAvailableDataLoggers(), "Search icon is missing");
            //Scenario 11
            Assert.assertTrue(assetPage.verifyColumnNamesOfdDataLoggers(), "Column Names Are not Accurate or missing");
            //Scenario 12 & 13
            Assert.assertTrue(assetPage.APSdropingOptionBoundDataLogger(assetType), "Options are not correctly showing");
            assetPage.clickAssetsFromAssetDetails();
            sleep(2000);
        }
    }
    @Test(groups = {"regression"})
    public void A009_verifyPaginationFunctionalityOnAssetListGrid_TC6046(){
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        //Scenario 1 & 2
        Assert.assertTrue(assetPage.paginationExist(), "Pagination is not existed");
        Assert.assertTrue(assetPage.AssetGridSize(), "Size is not Match");
        //Scenario 3
        Assert.assertTrue(assetPage.clickedNextPage(), "Next Page is Not clicked");
        Assert.assertTrue(assetPage.clickedPreviousPage(), "Previous Page is Not clicked");
        Assert.assertTrue(assetPage.LastPageClick(), "Last Page is not clicked");
        Assert.assertTrue(assetPage.clickedFirstPage(), "First Page is not clicked");
        Assert.assertTrue(assetPage.clickedRandomPage(), "Random Page is not clicked");
        Assert.assertTrue(assetPage.clickedRandomPage(), "Random Page is not clicked");
    }

    @Test(groups = {"regression"})
    public void A010_VerifyGridConfigFuncOnAssetListGrid_TC6047() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        assetPage.configIcon();
        //Scenario 1&2
        Assert.assertTrue(!assetPage.clickOnElementsOfColumnConfig("Geolocation"), "Geolocation is existed");
        Assert.assertTrue(!assetPage.clickOnElementsOfColumnConfig("Bound Data Logger(s)"), "Bound Data Logger(s) is existed");
        Assert.assertTrue(!assetPage.clickOnElementsOfColumnConfig("Organization"), "Organization is existed");
        Assert.assertTrue(!assetPage.clickOnElementsOfColumnConfig("Created By"), "Created By is existed");
        //Scenario 1&3
        Assert.assertTrue(assetPage.clickOnElementsOfColumnConfig("Organization"), "Organization is not existed");
        Assert.assertTrue(assetPage.clickOnElementsOfColumnConfig("Bound Data Logger(s)"), "Bound Data Logger(s) is not existed");
        Assert.assertTrue(assetPage.clickOnElementsOfColumnConfig("Geolocation"), "Geolocation is not existed");
        Assert.assertTrue(assetPage.clickOnElementsOfColumnConfig("Created By"), "Created By is not existed");
    }

    @Test
    public void A011_VerifyFuncFilteringAssetsList_TC6432() {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        //Scenario 1
        assetPage.clickFilter();
        Assert.assertTrue(assetPage.verifyFilterColumnHeaders(), "Filter Column Headers is not correct");
        //Scenario 2
        sleep(5000);
        Assert.assertTrue(assetPage.clickRandomAssetsTypeFromFilters(), "Filter option is missing or not clicked");
        assetPage.clickClearFilters();
        //Scenario 3
        Assert.assertTrue(assetPage.clickRandomItemsFromFilters(), "Filter is not work properly");
        //Scenario 4
        // ToDo: Have to Validate using database
        //Scenario 5
        assetPage.clickFilter();
        Assert.assertTrue(assetPage.clickCancelButton(), "Cancel Button is not worked");
        //Scenario 6
        Assert.assertTrue(assetPage.clickARandomCrossButtonInFilter(), "A random cross button is not clicked in filter");
        sleep(10000);
        //Scenario 7
        Assert.assertTrue(assetPage.clickClearFilters(), "Clear Filters Button is not Found");
    }

    @Test(groups = {"regression"})
    public void A012_A013_AndExtendedVerifyAssetDelete_UpdateActionWhenAShipmentStatusIsActiveInActiveExtended_TC7655AndTC7781(){
        assetPage = loginAndGoToAssetsPage();
        String assetName = "";
        for(int i = 0; i < 3; i++){
            if(i == 0)assetName = "AutoAssetXYZ01";
            else if(i == 1)assetName = "AutoAssetXYZ02";
            else if(i == 2)assetName = "AutoAssetXYZ03";
            assetPage.searchByName(assetName, "Name");
            sleep(5000);
            String onShipment = assetPage.getCellDataByName(assetName, "On Shipment");
            String monitoredStatus = assetPage.getCellDataByNameForMonitoredStatus(assetName);
            System.out.println(onShipment + " "+monitoredStatus);
            if(i == 0){
                Assert.assertEquals(onShipment, "No");
                Assert.assertEquals(monitoredStatus, "Monitored");
                Assert.assertTrue(assetPage.ActionsSituation(monitoredStatus, onShipment, assetName), "Something is not correct");
            }
            else if(i == 1){
                Assert.assertEquals(onShipment, "No");
                Assert.assertEquals(monitoredStatus, "Unmonitored");
                Assert.assertTrue(assetPage.ActionsSituation(monitoredStatus, onShipment, assetName), "Something is not correct");
            }
            else if(i == 2){
                Assert.assertEquals(onShipment, "Yes");
                Assert.assertEquals(monitoredStatus, "Monitored");
                Assert.assertTrue(assetPage.ActionsSituation(monitoredStatus, onShipment, assetName), "Something is not correct");
            }
            sleep(2000);
        }
    }

    @Test(groups = {"regression"})
    public void A014_VerifyBindAssetFuncByClickBindIcon_TC6320() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction


        Assert.assertTrue(assetPage.checkBindButton(), "Bound Data Logger(s) tab is not open into Asset Details Page");
    }

    @Test(groups = {"regression"})
    public void A015_VerifyUnbindAssetFuncByClickUnBindIcon_TC6321() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction


        Assert.assertTrue(assetPage.checkUnBindButton(), "Bound Data Logger(s) tab is not open into Asset Details Page");
    }

    @Test(groups = {"regression"})
    public void A016_VerifyAssetSummaryPopupWithCorrectDetails_TC6355() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        String Astr = "Softbox Asset 001";
        String Bstr = "Asset001";
        sleep(5000);
        assetPage.searchByName(Astr, "Name");
        List<String> DataOfAstr = assetPage.getAllDataByName(Astr);
        System.out.println(DataOfAstr);
        Assert.assertTrue(assetPage.clickAndCheckPopupByname(DataOfAstr), "It's not working properly");
        assetPage.searchByName(Bstr, "Name");
        List<String> DataOfBstr = assetPage.getAllDataByName(Bstr);
        Assert.assertTrue(assetPage.clickAndCheckPopupByname(DataOfBstr), "It's not working properly");
    }

    @Test(groups = {"regression"})
    public void A017_NavigateToFullAssetDetailsPageAndVerifyAssetSummary_TC6356() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction

        Assert.assertTrue(assetPage.clickAndCheckAssetDetailsPageByName(),"Match not found");
    }

    @Test
    public void A018_searchAssetFromAssetList_TC6438() throws InterruptedException {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction


        // Create a Map to hold the asset details
        Map<String, String> assetDetails = new HashMap<>();

        // Add key-value pairs to the Map
        assetDetails.put("Name", "Test Asset");
        assetDetails.put("Asset Type", "doglapan RKN");
        assetDetails.put("Bound Data Logger(s)", "OBD_s100O");
        assetDetails.put("Geolocation", "Dhaka (WH) Updated");
//        assetDetails.put("Organization", "doglapan");

        // Iterate through each entry in the map
        for (Map.Entry<String, String> entry : assetDetails.entrySet()) {
            String columnKey = entry.getKey();
            String searchValue = entry.getValue();

            System.out.println("Searching for: " + columnKey + " = " + searchValue);


            Assert.assertTrue(assetPage.isDataContainInColumn(searchValue, columnKey));
            // Clear filters before the next iteration
            assetPage.clickClearFilters();

//            // Perform the search by column
//            List<List<String>> tableValueAfterSearch = assetPage.searchByColumn(searchValue, columnKey);
//
//            // Verify if the search returned any results
//            if (tableValueAfterSearch.isEmpty()) {
//                System.out.println("No results found for " + columnKey + " = " + searchValue);
//                continue; // Move to the next entry in the map
//            }
//
//            // Print the table rows after the search
//            for (List<String> row : tableValueAfterSearch) {
//                System.out.println(row);
//            }
//
//            // Get the column index for verification
//            int columnIndex = assetPage.getColumnIndex(columnKey);
//
//            // Verify the results
//            boolean allMatch = true;
//            for (int i = 1; i < tableValueAfterSearch.size(); i++) { // Skip the header row
//                List<String> row = tableValueAfterSearch.get(i);
//                String cellValue = row.get(columnIndex).toLowerCase();
//
//                if (!cellValue.contains(searchValue.toLowerCase())) {
//                    System.out.println("Mismatch found: " + cellValue + " does not contain " + searchValue);
//                    allMatch = false;
//                }
//            }
//
//            if (allMatch) {
//                System.out.println("All rows match for " + columnKey + " = " + searchValue);
//            } else {
//                System.out.println("Some rows did not match for " + columnKey + " = " + searchValue);
//            }
//
//            // Clear filters before the next iteration
//            assetPage.clickClearFilters();
//            Thread.sleep(2000); // Small delay to allow UI to reset
        }
    }

    @Test(groups = {"regression"})
    public void A019_verifySortingInAssetDataGrid_TC6056() {
        assetPage = loginAndGoToAssetsPage();

        // Todo: Need to add test.info log or logAction


        String[] assetDataGridFields = {
                "Name", "Asset Type", "Bound Data Logger(s)", "Geolocation", "Organization",
                "Created At", "Created By", "Last Modified At", "Last Modified By"
        };

        // Todo: verify default asset list sorting

        // Todo: verify the ascending order

        // Todo: verify the descending order


    }

    @Test(groups = {"regression"})
    public void A020_verifyValueGeolocationLastLocationAfterBindAndUnbindDL_TC9137(){
        assetPage = loginAndGoToAssetsPage();
        sleep(5000);
        List<String> NameOrganizationAssetTypeTimeUserName = assetPage.createAssetMethod();
        System.out.println(NameOrganizationAssetTypeTimeUserName.get(0));
        sleep(5000);
        String geoLocationStatus = assetPage.getDataFromGridUsingRowNumber(1, "Geolocation");
        String Name = assetPage.getDataFromGridUsingRowNumber(1, "Name");
        Assert.assertTrue(Name.equals(NameOrganizationAssetTypeTimeUserName.get(0)), "Name is not matched");
        Assert.assertTrue(geoLocationStatus.equals("Out of Coverage"), "GeoLocationStatus is not matched");
        assetPage.clickOnName(NameOrganizationAssetTypeTimeUserName.get(0));
        String geoLocationStatusOnPopup = assetPage.getDataFromPopupByColumnName("Geolocation");
        Assert.assertTrue(geoLocationStatusOnPopup.equals("Out of Coverage"), "GeoLocationStatusOnPopup is not matched");
        assetPage.clickFullDetailsPageFromPopup();
        sleep(5000);

        //Todo:: Have to push data and verify.
        
        String lastLocation_GI = assetPage.getCellDataByNameFromAssetDetailsPage_GI("Last Location");
        Assert.assertTrue(lastLocation_GI.equals("Out of Coverage"), "Last Location is not matched");
    }

    @Test
    public void A021_VerifyAssetDetailsEnvironmentalDataAndCondition_TC6352() {
        assetPage = loginAndGoToAssetsPage();

        // Todo: search and go to a specific asset's details page
        // Todo: verify page title
        // Todo: Check environmental Condition tab
        // Todo: Validate Data logger values (Cargo temp, ambient temp, door, shock, tilt, pressure, humidity, light)
        // Todo: if no data then "No Data Available"
        // Todo: Verify graphical presence of all the data
        // Todo: Verify the timestamp
        // Todo: verify the missing data
        // Todo: Check active shipment condition
    }

    @Test
    public void A022_VerifyDuplicateDataLoggerPrevention_TC9404() {
        assetPage = loginAndGoToAssetsPage();

        // Todo: search and go to a specific asset's details page
        // Todo: verify page title
        // Todo: Go to bind datalogger tab
        // Todo: verify all the devices presence
        // Todo: Select and add a device to bind
        // Todo: Verify the success
        // Todo: select same component and verify error message
    }

    @Test
    public void A023_verifyAssetMovementAndAssetData(){
        assetPage = loginAndGoToAssetsPage();
        sleep(2000);
        assetPage.searchByName("AutoAPIAsset1738149935", "Name");
        sleep(2000);
        assetPage.clickOnFullDetailsPageFromGrid("AutoAPIAsset1738149935");
        sleep(2000);
        List<List<String>> longLatPairs = new ArrayList<>();
        longLatPairs.add(Arrays.asList("51.5631", "25.2654"));
        longLatPairs.add(Arrays.asList("72.2748", "25.7986"));
        longLatPairs.add(Arrays.asList("90.4029252", "23.8434344"));
        longLatPairs.add(Arrays.asList("91.3893", "23.0049"));
        longLatPairs.add(Arrays.asList("92.0014375", "21.4326875"));
        String uid = "807938083";
        String deviceId = "AutoAPIDevice1738149935";
        int src = 3;
        List<Integer> tempCargoTemp = new ArrayList<>();
        List<Integer> tempAmbientTemp = new ArrayList<>();
        List<Integer> tempCargoHumidity = new ArrayList<>();
        List<Integer> tempAmbientHumidity = new ArrayList<>();
        Integer t0 = 80, t1 = 70, h0 = 12, h1 = 3, vol = 100, signal = 60;
        for(int i = 0; i < longLatPairs.size(); i++){
            long time = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().getEpochSecond();
            String zoneId = "Asia/Dhaka";
            if (i == 0) zoneId = "Asia/Qatar";
            else if (i == 1) zoneId = "Asia/Karachi";
            long lbsTime = ZonedDateTime.now(ZoneId.of(zoneId)).toInstant().getEpochSecond();
            tempCargoTemp.add(t0 + assetPage.generateRandomInt(20));
            tempAmbientTemp.add(t1 + assetPage.generateRandomInt(30));
            tempCargoHumidity.add(h0 + assetPage.generateRandomInt(10));
            tempAmbientHumidity.add(h1 + assetPage.generateRandomInt(5));
            Response response = apiClient.pushLocationDataNow(deviceId, uid,vol - (i * 10), signal + assetPage.generateRandomInt(20), longLatPairs.get(i).get(0), longLatPairs.get(i).get(1), "Location Based Data " + i, src);
            response.then().statusCode(200);
            response = apiClient.pushTelemetryData(deviceId, uid, time, lbsTime, tempCargoTemp.get(i), tempAmbientTemp.get(i), tempCargoHumidity.get(i), tempAmbientHumidity.get(i));
            response.then().statusCode(200);
            for(int k = 0; k < 3; k++){
                sleep(2000);
                assetPage.refreshPage();
            }
            sleep(2000);
            assetPage.clickOnLocation();
            sleep(2000);
            Assert.assertTrue(assetPage.ValidatePushedLocationData(longLatPairs.get(i).get(0), longLatPairs.get(i).get(1)), "data not matched");
            sleep(2000);
            assetPage.clickOnEnvironMentalConditions();
            Assert.assertTrue(assetPage.validatePushedEnvironmentalConditionData(tempCargoTemp.get(i), tempAmbientTemp.get(i), tempCargoHumidity.get(i), tempAmbientHumidity.get(i)));
        }
        System.out.println(1000);
    }
}

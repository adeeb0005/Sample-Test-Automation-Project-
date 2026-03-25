package com.TEST.tests;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.DataLoggersPage;
import com.TEST.pages.LoginPage;
import com.TEST.utils.DBUtility;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
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

import static com.TEST.pages.BasePage.sleep;
import static com.TEST.pages.BasePage.takeDataFromRange;
import static com.TEST.pages.DataLoggersPage.convertResultSetToList;

public class DataLoggersTest extends BaseTest {
    private DataLoggersPage dataLoggersPage;
    SoftAssert softAssert = new SoftAssert();
    String dataLoggerQuery = "WITH FilteredDevices AS (SELECT d.devicename AS [Data Logger ID], dt.DeviceTypeName AS [Data Logger Type], dp.DeviceProfileName AS [Data Logger Profile], ass.assetname AS [Asset Name], orr.OrganizationName AS Organization, dmf.devicemanufacturername AS [Data Logger Manufacturer], CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 108) AS [Created At], cu.Userdisplayname AS [Created By], CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 108) AS [Last Modified At], uu.Userdisplayname AS [Last Modified By], d.updatedat AS UpdatedAt, d.deviceid, d.createdat FROM dbo.Devices d JOIN dbo.DeviceTypes dt ON d.DeviceTypeId = dt.DeviceTypeId LEFT JOIN dbo.assets ass ON ass.assetid = d.assetid LEFT JOIN dbo.DeviceProfiles dp ON d.DeviceProfileId = dp.DeviceProfileId JOIN dbo.Organizations orr ON d.organizationid = orr.organizationid JOIN dbo.devicemanufacturers dmf ON dt.devicemanufacturerid = dmf.devicemanufacturerid JOIN dbo.Users cu ON d.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON d.UpdatedByUserId = uu.UserId WHERE d.DeactivatedAt IS NULL) SELECT [Data Logger ID], [Data Logger Type], [Data Logger Profile], [Asset Name], Organization, [Data Logger Manufacturer], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilteredDevices";

    public DataLoggersPage loginAndGoToDataLoggersPage() {
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
        test.info("Going to Data Loggers Page...");
        dataLoggersPage = dashboardPage.goToDataLoggerPage();
        test.info("At the Data Loggers Page.");

        return dataLoggersPage;
    };

    @Test(groups = "DB")
    public void DL001_verifyFuncOfDLSorting_TC6684() throws SQLException {
        dataLoggersPage = loginAndGoToDataLoggersPage();

        // Todo: test.info or logAction

        sleep(5000);  // Wait for the data to load
        String[] fields = {
                "'Data Logger Profile'", "'Asset Name'", "'Organization'", "'Created At'", "'Last Modified At'"
        };
        //single column
        String[] situations = {" ASC ", " DESC "};
        for (String field : fields) {
            //if(field.equals("'Created At'") && situations.equals(" ASC "))continue;
            for(String situation : situations){
                System.out.println("start");
                sleep(2000);
                dataLoggersPage.clickSort(field);
                sleep(3000);
                String temp = field;
                if(field.equals("'Created At'"))field = "createdAt";
                if(field.equals("'Last Modified At'"))field = "UpdatedAt";
                String query = dataLoggerQuery + " ORDER BY " + field + situation + "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                if(field.equals("'Asset Name'") && situation.equals(" ASC ")){
                    query = dataLoggerQuery + " ORDER BY "+ "CASE WHEN [Asset Name] IS NULL THEN 0 ELSE 1 END, "+
                            field + situation + ", deviceid "+"OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                }
                else if(field.equals("'Data Logger Profile'") && situation.equals(" ASC ")){
                    query = dataLoggerQuery + " ORDER BY "+ "CASE WHEN [Data Logger Profile] IS NULL THEN 0 ELSE 1 END, "+
                            field + situation + ", deviceid "+"OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                }
                System.out.println(query);
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                List<List<String>> top20tableData = takeDataFromRange(tableData, 1, 20);
                sleep(2000);
                System.out.println(top20tableData);
                List<List<String>> table = dataLoggersPage.getTable();
                System.out.println(table);
                field = temp;
                System.out.println(field+situation);
                System.out.println(table);
                softAssert.assertTrue(dataLoggersPage.checkColumn(table, top20tableData, field), field+situation+"not match");
                //softAssert.assertTrue(dataLoggersPage.checkTable(table, top20tableData), );
                System.out.println("done");
            }
            //sleep(5000);
            dataLoggersPage.clickSort(field);
            //sleep(5000);
        }
        softAssert.assertAll();
        // todo:: double column sorting have to implement
    }

    @Test(groups = "DB")
    public void DL002_verifyDataLoggerListSearch_TC7727() throws SQLException {
        dataLoggersPage = loginAndGoToDataLoggersPage();

        // Todo: test.info or logAction

        //Scenario 1, 2, 3,
//todo        softAssert.assertTrue(dataLoggersPage.checkSearchButton(), "Search Button is not working");
        //Scenario 4 to 22
        List<String> searchingItems = Arrays.asList("Data Logger ID", "Data Logger Type", "Data Logger Profile", "Asset Name", "Organization", "Data Logger Manufacturer", "Created By", "Last Modified By");
        Set<String> random2Items = new HashSet<>();
        while(random2Items.size() < 2){
            random2Items.add(searchingItems.get(dataLoggersPage.generateRandomInt(searchingItems.size())));
        }
        List <String> items = new ArrayList<>(random2Items);
        //List<String> items = Arrays.asList("Data Logger ID", "Data Logger Type", "Data Logger Profile", "Asset Name", "Organization", "Data Logger Manufacturer", "Created By", "Last Modified By");
        List<String> DEMO_TESTS = Arrays.asList("DL", "Test", "Asset", "ka", "ma");
        for (String item : items) {
            for (String demo_test : DEMO_TESTS) {
                String query = dataLoggerQuery + " WHERE [" + item + "] LIKE '%" + demo_test + "%' ORDER BY  UpdatedAt DESC " + "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                System.out.println(query);
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                //System.out.println(tableData);
                softAssert.assertTrue(dataLoggersPage.checkSearchOnColumn(item, demo_test, tableData), "Match not found");
                sleep(2000);
            }
        }
        softAssert.assertAll();
    }

    @Test(groups = "DB")
    public void DL003_CreateNewDeviceAndVerifyDeviceInfoFromListInExndFromDB_TC6053() throws SQLException {
        dataLoggersPage = loginAndGoToDataLoggersPage();
        // Todo: test.info or logAction
        String dataLoggerType = "Tradoglapan Max";
        Map<String, String>data = dataLoggersPage.createNewDataLogger(dataLoggerType);
        sleep(5000);
        dataLoggersPage.refreshPage();
        dataLoggersPage.refreshPage();
        //data create to frontend validation
        sleep(500);
        softAssert.assertTrue(dataLoggersPage.checkRowByColumn("Data Logger ID", 1, data.get("dataLoggerID")), "Not Match");
        //Extra:: frontend to database validation
        String FirstRowQuery = "WITH FilteredDevices AS (SELECT d.devicename AS [Data Logger ID], dt.DeviceTypeName AS [Data Logger Type], dp.DeviceProfileName AS [Data Logger Profile], ass.assetname AS [Asset Name], CASE WHEN DATEDIFF(HOUR, d.lastlocationupdatedat, GETDATE()) < 48 THEN 'Online' ELSE 'Offline' END AS Status, orr.OrganizationName AS Organization, dmf.devicemanufacturername AS [Data Logger Manufacturer], CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.createdat), 108) AS [Created At], cu.Userdisplayname AS [Created By], CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, d.updatedat), 108) AS [Last Modified At], uu.Userdisplayname AS [Last Modified By], d.updatedat AS UpdatedAt FROM dbo.Devices d LEFT JOIN dbo.DeviceTypes dt ON d.DeviceTypeId = dt.DeviceTypeId LEFT JOIN dbo.assets ass ON ass.assetid = d.assetid LEFT JOIN dbo.DeviceProfiles dp ON d.DeviceProfileId = dp.DeviceProfileId LEFT JOIN dbo.Organizations orr ON d.organizationid = orr.organizationid JOIN dbo.devicemanufacturers dmf ON dt.devicemanufacturerid = dmf.devicemanufacturerid LEFT JOIN dbo.Users cu ON d.CreatedByUserId = cu.UserId LEFT JOIN dbo.Users uu ON d.UpdatedByUserId = uu.UserId WHERE d.DeactivatedAt IS NULL) SELECT [Data Logger ID], [Data Logger Type], [Data Logger Profile], [Asset Name], Status, Organization, [Data Logger Manufacturer], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilteredDevices ORDER BY UpdatedAt DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        ResultSet rs = DBUtility.executeQuery(FirstRowQuery, test);
        List<List<String>> tableData = convertResultSetToList(rs);
        List<List<String>> table = dataLoggersPage.getTable();
        softAssert.assertTrue(tableData.get(1).equals(table.get(1)), "Not Match");
        //delete created dl
        dataLoggersPage.deleteDLByName(data.get("dataLoggerID"));
    }

    @Test(groups = "regression")
    public void DL004_verifyCreatingDataLoggerTypeWithDataLoggerProfile_TC0000(){
        dataLoggersPage = loginAndGoToDataLoggersPage();
        dataLoggersPage.validation();
    }

    @Test
    public void DL005_createUpdateSearchPopupCardDetailsPage_TC0000(){
        dataLoggersPage = loginAndGoToDataLoggersPage();
        // Todo: test.info or logAction
        Map<String, String> DLData = dataLoggersPage.createNewDataLogger("Tradoglapan Max");
        sleep(5000);
        dataLoggersPage.refreshPage();
        dataLoggersPage.refreshPage();
        sleep(2000);
        dataLoggersPage.searchDLByName(DLData.get("dataLoggerID"));
        sleep(2000);
        dataLoggersPage.clickOnDLName(DLData.get("dataLoggerID"));
        sleep(1000);
        Map<String, String> dataFromDLSummary = dataLoggersPage.getDataFromDLSummary();
        System.out.println(dataFromDLSummary);
        System.out.println(DLData);
        Assert.assertTrue(dataLoggersPage.checkSummaryDataWithGivenData(dataFromDLSummary, DLData), "Data is not match");
        dataLoggersPage.clickFullDetails();
        sleep(2000);
        Assert.assertTrue(dataLoggersPage.validateDLSnapShot(DLData), "Data is not match");
        dataLoggersPage.clickDLForGridPage();
        sleep(2000);
        dataLoggersPage.searchDLByName(DLData.get("dataLoggerID"));
        sleep(2000);
        DLData = dataLoggersPage.updateDLByName(DLData);
        sleep(2000);
        dataLoggersPage.deleteDLByName(DLData.get("dataLoggerID"));
    }

    @Test
    public void DL006_dataLoggerProfileValidate_TC0000() throws IOException {
        JSONObject testCase = dataLoggersPage.getJSONDataByMethodName("DLP005_verifyAssignedDevicesWithDeviceProfile_TC0000");
        JSONArray devicesArray = testCase.getJSONArray("devicesAndDeviceProfiles");
        int listOfData = devicesArray.length();
        Set<String> dataLoggerProfiles = new HashSet<>();
        Set<String> dataLoggers = new HashSet<>();
        for(int i = 0; i < listOfData; i++){
            JSONObject testData = devicesArray.getJSONObject(i);
            String deviceProfileName = testData.getString("deviceProfileName");
            String deviceName = testData.getString("deviceName");
            dataLoggerProfiles.add(deviceProfileName);
            dataLoggers.add(deviceName);
        }
        Assert.assertTrue(dataLoggerProfiles.size() == 1, "Data is changed");
        dataLoggersPage = loginAndGoToDataLoggersPage();
        List<List<String>> longLatPairs = new ArrayList<>();
        longLatPairs.add(Arrays.asList("51.5631", "25.2654"));
        longLatPairs.add(Arrays.asList("72.2748", "25.7986"));
        longLatPairs.add(Arrays.asList("90.4029252", "23.8434344"));
        longLatPairs.add(Arrays.asList("91.3893", "23.0049"));
        longLatPairs.add(Arrays.asList("92.0014375", "21.4326875"));
        String uid = "807938083";
        int src = 3;
        for(String dataLogger :  dataLoggers){
            Integer vol = 100, signal = 60;
            sleep(2000);
            dataLoggersPage.searchDLByName(dataLogger);
            sleep(2000);
            Assert.assertTrue(dataLoggersPage.validateColumnDataByName("Data Logger Profile", dataLogger, new ArrayList<>(dataLoggerProfiles).get(0)),
                    "Data is not match");
            dataLoggersPage.clickOnDLName(dataLogger);
            sleep(2000);
            dataLoggersPage.clickFullDetails();
            sleep(2000);
            for (int i = 0; i < longLatPairs.size(); i++){
                Response response = apiClient.pushLocationDataNow(dataLogger, uid,vol - (i * 10), signal + dataLoggersPage.generateRandomInt(20), longLatPairs.get(i).get(0), longLatPairs.get(i).get(1), "Location Based Data " + i, src);
                response.then().statusCode(200);
                for(int k = 0; k < 3; k++){
                    sleep(2000);
                    dataLoggersPage.refreshPage();
                }
                sleep(2000);
                dataLoggersPage.clickOnInMap();
                sleep(2000);
                Assert.assertTrue(dataLoggersPage.ValidatePushedLocationData(longLatPairs.get(i).get(0), longLatPairs.get(i).get(1)), "data not matched");
            }
            sleep(2000);
            dataLoggersPage.clickDLForGridPage();
            sleep(2000);
        }
    }
}

package com.TEST.tests;

import com.TEST.pages.*;
import com.TEST.utils.DBUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

import static com.TEST.pages.BasePage.convertResultSetToList;
import static com.TEST.pages.BasePage.sleep;

public class DataLoggersProfileTest extends BaseTest{
    private DataLoggersProfilePage dataLoggersProfilePage;

    SoftAssert softAssert = new SoftAssert();
    String DataLoggerProfileListQuery = "WITH FilterDataLoggerProfiles AS (SELECT dp.DeviceProfileName AS 'Name', o.OrganizationName AS 'Organization', dt.DeviceTypeName AS 'Data Logger Type', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 108) AS 'Created At', cu.Userdisplayname AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 108) AS 'Last Modified At', uu.Userdisplayname AS 'Last Modified By', dp.UpdatedAt, dp.CreatedAt FROM dbo.DeviceProfiles dp JOIN dbo.Organizations o ON dp.OrganizationId = o.OrganizationId JOIN dbo.DeviceTypes dt ON dp.DeviceTypeId = dt.DeviceTypeId JOIN dbo.Users cu ON dp.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON dp.UpdatedByUserId = uu.UserId WHERE dp.DeActivatedAt IS NULL) SELECT [Name], [Organization], [Data Logger Type], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilterDataLoggerProfiles";

    public DataLoggersProfilePage loginAndGoToDataLoggerProfilePage() {
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
        test.info("Going to Data Logger Profile Page...");
        dataLoggersProfilePage = dashboardPage.goToDataLoggerProfilePage();
        test.info("At the Data Logger Page.");

        return dataLoggersProfilePage;
    }

    @Test(groups = {"DB"})
    public void DLP001_verifyFuncOfDLSorting_TC8789() throws SQLException {
        dataLoggersProfilePage = loginAndGoToDataLoggerProfilePage();

        // Todo: test.info or logAction

        sleep(5000);  // Wait for the data to load
        String[] fields = {"'Name'", "'Organization'", "'Data Logger Type'", "'Created At'", "'Created By'", "'Last Modified At'", "'Last Modified By'"};
        //single column
        String[] situations = {" ASC ", " DESC "};
        for (String field : fields) {
            for(String situation : situations){
                System.out.println("start");
                sleep(2000);
                dataLoggersProfilePage.clickSort(field);
                sleep(3000);
                String temp = field;
                if(field.equals("'Created At'"))field = "CreatedAt";
                if(field.equals("'Last Modified At'"))field = "UpdatedAt";
                String query = DataLoggerProfileListQuery + " ORDER BY " + field + situation + " OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;";
                System.out.println(query);
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                List<List<String>> table = dataLoggersProfilePage.getTable();
                System.out.println(field+situation);
                System.out.println(table);
                softAssert.assertTrue(dataLoggersProfilePage.checkTable(table, tableData), field+situation+"not match");
                System.out.println("done");
                field = temp;
            }
            //sleep(5000);
            dataLoggersProfilePage.clickSort(field);
            sleep(5000);
        }
        dataLoggersProfilePage.clickSort("'Created By'");
        dataLoggersProfilePage.clickSort("Name");
        sleep(5000);
        List<List<String>> table = dataLoggersProfilePage.getTable();
        String query = DataLoggerProfileListQuery + " ORDER BY 'Created By' ASC, 'Name' ASC"+ " OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;";
        System.out.println(query);
        ResultSet rs = DBUtility.executeQuery(query, test);
        List<List<String>> tableData = convertResultSetToList(rs);
        softAssert.assertTrue(dataLoggersProfilePage.checkTable(table, tableData), "'Created By' ASC, 'Name' ASC "+"not match");
        softAssert.assertAll();
        // todo:: double column sorting have to implement
    }

    @Test(groups = {"DB"})
    public void DLP002_verifyDLPListSearch_TC9336() throws SQLException {
        String searchQuery = "WITH FilterDataLoggerProfiles AS (SELECT dp.DeviceProfileName AS 'Name', o.OrganizationName AS 'Organization', dt.DeviceTypeName AS 'Data Logger Type', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 108) AS 'Created At', cu.Userdisplayname AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 108) AS 'Last Modified At', uu.Userdisplayname AS 'Last Modified By', dp.UpdatedAt, dp.DeviceProfileName FROM dbo.DeviceProfiles dp LEFT JOIN dbo.Organizations o ON dp.OrganizationId = o.OrganizationId LEFT JOIN dbo.DeviceTypes dt ON dp.DeviceTypeId = dt.DeviceTypeId JOIN dbo.Users cu ON dp.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON dp.UpdatedByUserId = uu.UserId WHERE dp.DeActivatedAt IS NULL) SELECT [Name], [Organization], [Data Logger Type], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilterDataLoggerProfiles";
        dataLoggersProfilePage = loginAndGoToDataLoggerProfilePage();

        // Todo: test.info or logAction

        softAssert.assertTrue(dataLoggersProfilePage.checkSearchButton(), "Search Button is not working");
        //Scenario 1 to 3
        List<String> items = Arrays.asList("Name", "Organization", "Data Logger Type", "Created By", "Last Modified By");
        List<String> DEMO_TESTS = Arrays.asList("auto", "EFASC", "sajid", "ab");
        for (String item : items) {
            for (String demo_test : DEMO_TESTS) {
                String query = searchQuery + " WHERE [" + item + "] LIKE '%" + demo_test + "%' ORDER BY  UpdatedAt DESC " + "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                softAssert.assertTrue(dataLoggersProfilePage.checkSearchOnColumn(item, demo_test, tableData), "Match not found");
                sleep(2000);
            }
        }
        softAssert.assertAll();
    }

    @Test(groups = {"DB"})
    public void DLP003_CreateNewDeviceAndVerifyDeviceInfoFromListInExndFromDB() throws SQLException {
        dataLoggersProfilePage = loginAndGoToDataLoggerProfilePage();

        // Todo: test.info or logAction

        int n = dataLoggersProfilePage.generateRandomInt(2);
        boolean status = true;
        if(n == 0) status = false;
        Map<String, String> data = dataLoggersProfilePage.createNewDataLogger(status);
        sleep(5000);
        dataLoggersProfilePage.refreshPage();
        dataLoggersProfilePage.refreshPage();
        //data create to frontend validation
        softAssert.assertTrue(dataLoggersProfilePage.checkRowByColumn("Name", 1, data.get("Name")), "Not Match");
        //Extra:: frontend to database validation

        String FirstRowQuery = "WITH FilterDataLoggerProfiles AS (SELECT dp.DeviceProfileName AS 'Name', o.OrganizationName AS 'Organization', dt.DeviceTypeName AS 'Data Logger Type', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.CreatedAt), 108) AS 'Created At', cu.Userdisplayname AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, dp.UpdatedAt), 108) AS 'Last Modified At', uu.Userdisplayname AS 'Last Modified By', dp.UpdatedAt, dp.DeviceProfileName FROM dbo.DeviceProfiles dp LEFT JOIN dbo.Organizations o ON dp.OrganizationId = o.OrganizationId LEFT JOIN dbo.DeviceTypes dt ON dp.DeviceTypeId = dt.DeviceTypeId JOIN dbo.Users cu ON dp.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON dp.UpdatedByUserId = uu.UserId WHERE dp.DeActivatedAt IS NULL) SELECT [Name], [Organization], [Data Logger Type], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilterDataLoggerProfiles ORDER BY UpdatedAt DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        ResultSet rs = DBUtility.executeQuery(FirstRowQuery, test);
        List<List<String>> tableData = convertResultSetToList(rs);
        List<List<String>> table = dataLoggersProfilePage.getTable();
        softAssert.assertTrue(tableData.get(1).equals(table.get(1)), "Not Match");
        dataLoggersProfilePage.deleteDLPByName(data.get("Name"));
    }

    @Test
    public void DLP004_createUpdateConfigSearchValidate(){
        dataLoggersProfilePage = loginAndGoToDataLoggerProfilePage();

        // Todo: test.info or logAction

        int n = dataLoggersProfilePage.generateRandomInt(2);
        boolean status = true;
        if(n == 0) status = false;
        Map<String, String>dataDLP = dataLoggersProfilePage.createNewDataLogger(status);
        sleep(3000);
        dataLoggersProfilePage.refreshPage();
        dataLoggersProfilePage.refreshPage();
        System.out.println(dataDLP);
        sleep(2000);
        dataLoggersProfilePage.searchDLPByName(dataDLP.get("Name"));
        System.out.println(2000);
        dataLoggersProfilePage.clickOnDLPName(dataDLP.get("Name"));
        sleep(1000);
        Map<String, String> dataFromDLPSummary = dataLoggersProfilePage.getDataFromDLPSummary(status);
        System.out.println(dataFromDLPSummary);
        System.out.println(dataDLP);
        Assert.assertTrue(dataLoggersProfilePage.checkSummaryDataWithGivenData(dataFromDLPSummary, dataDLP), "Data is not match");
        dataLoggersProfilePage.clickFullDetails();
        sleep(2000);
        Assert.assertTrue(dataLoggersProfilePage.validateDLPSnapShot(dataDLP), "Data is not matched");
        dataLoggersProfilePage.clickConfigButtonInDLPDetailsPage();
        sleep(2000);
        Assert.assertTrue(dataLoggersProfilePage.configPageValidation(dataDLP), "Config is not correct");
        dataDLP = dataLoggersProfilePage.updateDataIntoConfigFile(dataDLP);
        if(status)status = false;
        else status = true;
        sleep(2000);
        System.out.println(dataDLP);
        Assert.assertTrue(dataLoggersProfilePage.configPageValidation(dataDLP), "Config is not correct");
        dataLoggersProfilePage.clickForDLPPage();
        sleep(2000);
        dataLoggersProfilePage.searchDLPByName(dataDLP.get("Name"));
        System.out.println(2000);
        dataLoggersProfilePage.clickOnDLPName(dataDLP.get("Name"));
        sleep(1000);
        dataFromDLPSummary = dataLoggersProfilePage.getDataFromDLPSummary(status);
        System.out.println(dataFromDLPSummary);
        System.out.println(dataDLP);
        Assert.assertTrue(dataLoggersProfilePage.checkSummaryDataWithGivenData(dataFromDLPSummary, dataDLP), "Data is not match");
        dataLoggersProfilePage.clickCrossButton();
        sleep(2000);
        dataDLP = dataLoggersProfilePage.updateFromGrid(dataDLP);
        sleep(2000);
        dataLoggersProfilePage.searchDLPByName(dataDLP.get("Name"));
        sleep(2000);
        dataLoggersProfilePage.clickFullDetailsIconByName(dataDLP.get("Name"));
        sleep(2000);
        Assert.assertTrue(dataLoggersProfilePage.validateDLPSnapShot(dataDLP), "Data is not matched");
        dataLoggersProfilePage.clickForDLPPage();
        sleep(2000);
        dataLoggersProfilePage.deleteDLPByName(dataDLP.get("Name"));
    }

    @Test
    public void DLP005_verifyAssignedDevicesWithDeviceProfile_TC0000() throws IOException {
        JSONObject testCase = dataLoggersProfilePage.getJSONDataByMethodName("DLP005_verifyAssignedDevicesWithDeviceProfile_TC0000");
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
        dataLoggersProfilePage = loginAndGoToDataLoggerProfilePage();
        sleep(2000);
        dataLoggersProfilePage.searchDLPByName(new ArrayList<>(dataLoggerProfiles).get(0));
        sleep(2000);
        dataLoggersProfilePage.clickFullDetailsIconByName(new ArrayList<>(dataLoggerProfiles).get(0));
        sleep(2000);
        dataLoggersProfilePage.clickAssignToInDetailsPage();
        sleep(2000);
        Assert.assertTrue(dataLoggersProfilePage.activeAssignTo(), "Assign To is not active");
        Assert.assertTrue(dataLoggersProfilePage.profileAssignedToActive(), "Profile Assigned To is not shown");
        Assert.assertTrue(dataLoggersProfilePage.tableHeaderShownInAssignToShown(), "Table Header Shown is not shown");
        Assert.assertTrue(dataLoggersProfilePage.tableDeviceDataCheck(dataLoggers), "Data Loggers is not matched");
    }
}

package com.TEST.tests;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import com.TEST.pages.RulesPage;
import com.TEST.utils.DBUtility;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.TEST.pages.BasePage.convertResultSetToList;
import static com.TEST.pages.BasePage.sleep;

public class RulesTest extends BaseTest {
    private RulesPage rulesPage;
    SoftAssert softAssert = new SoftAssert();
    String rulesListQuery = "WITH FiltertedRule AS (SELECT rs.RuleSetName AS Name, o.OrganizationName AS Organization, CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.CreatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.CreatedAt), 108) AS 'Created At', cu.Userdisplayname AS 'Created By', CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.UpdatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.UpdatedAt), 108) AS 'Last Modified At', uu.Userdisplayname AS 'Last Modified By', rs.CreatedAt, rs.UpdatedAt FROM dbo.RuleSets rs JOIN dbo.Organizations o ON rs.OrganizationId = o.OrganizationId JOIN dbo.Users cu ON rs.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON rs.UpdatedByUserId = uu.UserId WHERE rs.DeActivatedAt IS NULL AND rs.RuleSetTypeId IS NULL) SELECT [Name], [Organization], [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FiltertedRule";

    public RulesPage loginAndGoToRulesPage() {
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
        test.info("Going to Rules Page...");
        rulesPage = dashboardPage.goToRulesPage();
        test.info("At the Rules Page Now");

        return rulesPage;
    }

    @Test(groups = {"DB"})
    public void RL001_verifyFuncOfDLSorting_TC7652() throws SQLException {
        rulesPage = loginAndGoToRulesPage();

        // Todo: test.info or logAction

        sleep(5000);  // Wait for the data to load
        String[] fields = {"'Name'", "'Organization'", "'Created At'", "'Created By'", "'Last Modified At'", "'Last Modified By'"};
        //single column
        String[] situations = {" ASC ", " DESC "};
        for (String field : fields) {
            for(String situation : situations){
                System.out.println("start");
                sleep(2000);
                rulesPage.clickSort(field);
                sleep(3000);
                String temp = field;
                if(field.equals("'Created At'")){
                    field = "CreatedAt";
                }
                if(field.equals("'Last Modified At'")){
                    field = "UpdatedAt";
                }
                String query = rulesListQuery + " ORDER BY " + field + situation + " OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY;";
                System.out.println(query);
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                List<List<String>> table = rulesPage.getTable();
                System.out.println(field+situation);
                field = temp;
                softAssert.assertTrue(rulesPage.checkColumn(table, tableData, field), field+situation+"not match");
                System.out.println("done");
            }
            //sleep(5000);
            rulesPage.clickSort(field);
            //sleep(5000);
        }
        softAssert.assertAll();
        // todo:: double column sorting have to implement
    }

    @Test(groups = {"DB"})
    public void RL002_verifyRulesListSearch_TC7673() throws SQLException {
        String searchQuery = "WITH FilteredRules AS(" +
                "SELECT rs.ruleSetId AS RuleSetID,rs.RuleSetName AS Name, o.OrganizationName AS Organization, " +
                "CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.CreatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.CreatedAt), 108) AS 'Created At', cu.Userdisplayname as 'Created By', " +
                "CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.UpdatedAt), 101) + ' ' + CONVERT(VARCHAR, DATEADD(HOUR, 6, rs.UpdatedAt), 108) AS 'Last Modified At', uu.Userdisplayname as 'Last Modified By', rs.UpdatedAt AS updatedAt " +
                "FROM dbo.RuleSets rs LEFT JOIN dbo.Organizations o ON rs.OrganizationId = o.OrganizationId JOIN dbo.Users cu ON rs.CreatedByUserId = cu.UserId JOIN dbo.Users uu ON rs.UpdatedByUserId = uu.UserId " +
                "WHERE rs.DeActivatedAt IS NULL AND rs.RuleSetTypeId IS NULL " +
                ") " +
                "SELECT Name, Organization, [Created At], [Created By], [Last Modified At], [Last Modified By] FROM FilteredRules";
        rulesPage = loginAndGoToRulesPage();

        // Todo: test.info or logAction
        sleep(2000);
        //softAssert.assertTrue(rulesPage.checkSearchButton(), "Search Button is not working");
        //Scenario 1 to 3
        List<String> items = Arrays.asList("Name", "Organization", "Created By", "Last Modified By");
        List<String> DEMO_TESTS = Arrays.asList("rule", "doglapan","fline", "sajid", "ab");
        for (String item : items) {
            for (String demo_test : DEMO_TESTS) {
                String query = searchQuery + " WHERE [" + item + "] LIKE '%" + demo_test + "%' ORDER BY  UpdatedAt DESC " + "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
                ResultSet rs = DBUtility.executeQuery(query, test);
                List<List<String>> tableData = convertResultSetToList(rs);
                System.out.println(tableData);
                softAssert.assertTrue(rulesPage.checkSearchOnColumn(item, demo_test, tableData), item+" "+demo_test+" "+"Match not found");
                System.out.println("pass "+item+" "+demo_test);
                sleep(3000);
            }
        }
        softAssert.assertAll();
    }

    @Test(groups = {"regression"})
    public void RL003_verifyGlobalRulesCreation_TC8246() throws InterruptedException {
        rulesPage = loginAndGoToRulesPage();

        // Todo: test.info or logAction

        test.info("Creating New Rule...");
        Map<String, String> ruleData = rulesPage.createNewRule();
        test.info("Created New Rule");
        rulesPage.refreshPage();
        rulesPage.refreshPage();
        sleep(2000);
        String ruleName = ruleData.get("ruleName");
        rulesPage.deleteRuleByName(ruleName);
    }

    @Test(groups = {"regression"})
    public void RL004_verifyUpdateRulesFunctionality_TC8272() throws InterruptedException {
        rulesPage = loginAndGoToRulesPage();
        Map<String, String> ruleData = rulesPage.createNewRule();
        String ruleName = ruleData.get("ruleName");
        // Todo: test.info or logAction
        rulesPage.refreshPage();
        rulesPage.refreshPage();
        sleep(2000);
        Assert.assertTrue(rulesPage.updateRulesFunctionality(ruleName));
        Thread.sleep(2000);
        rulesPage.deleteRuleByName(ruleName);
    }

    @Test(groups = {"regression"})
    public void RL005_rulesCreateSearchUpdateTestCaseCompilation_TC0000() throws InterruptedException{
        rulesPage = loginAndGoToRulesPage();
        Map<String, String> ruleData = rulesPage.createNewRule();
        System.out.println(ruleData);
        String ruleName = ruleData.get("ruleName");
        // Todo: test.info or logAction
        rulesPage.refreshPage();
        rulesPage.refreshPage();
        sleep(2000);
        rulesPage.searchRuleByName(ruleName);
        sleep(1000);
        rulesPage.clickOnRuleName(ruleName);
        sleep(2000);
        System.out.println("boom");
        Map<String, String> dataFromRuleSummary = rulesPage.getDataFromRuleSummary();
        Assert.assertTrue(rulesPage.checkSummaryDataWithGivenData(dataFromRuleSummary, ruleData), "data is not match");
        Map<String, String >updatedRuleData = rulesPage.updateRulesByNameWithSummaryEditButton(ruleData);
        ruleName = updatedRuleData.get("ruleName");
        System.out.println(ruleName);
        sleep(5000);
        rulesPage.searchRuleByName(ruleName);
        sleep(2000);
        rulesPage.clickOnRuleName(ruleName);
        sleep(2000);
        System.out.println("boom");
        dataFromRuleSummary = rulesPage.getDataFromRuleSummary();
        Assert.assertTrue(rulesPage.checkSummaryDataWithGivenData(dataFromRuleSummary, updatedRuleData), "data is not match");
        rulesPage.clickCrossButton();
        sleep(2000);
        Map<String, String>updated2ndData = rulesPage.updateRulesByNameWithEditButtonFromGrid(updatedRuleData);
        sleep(5000);
        rulesPage.clickOnRuleName(ruleName);
        sleep(2000);
        System.out.println("boom");
        dataFromRuleSummary = rulesPage.getDataFromRuleSummary();
        System.out.println(dataFromRuleSummary);
        System.out.println(updated2ndData);
        Assert.assertTrue(rulesPage.checkSummaryDataWithGivenData(dataFromRuleSummary, updated2ndData), "data is not match");
        rulesPage.clickCrossButton();
        sleep(2000);
        rulesPage.deleteRuleByName(updatedRuleData.get("ruleName"));
    }
}

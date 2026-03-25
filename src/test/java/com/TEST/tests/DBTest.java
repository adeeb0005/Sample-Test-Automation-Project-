package com.TEST.tests;

import com.TEST.utils.DBUtility;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTest extends BaseTest{

    @Test(groups = {"DB"})
    public void DB001_VerifyUserExists_TC0000() throws SQLException {
        String query = "SELECT TOP(10) UserName FROM Users";
        ResultSet rs = DBUtility.executeQuery(query, test);

        boolean userFound = false;
        // String desiredUserName = "noemi.vannacci@bollore.com";
        // String desiredUserName = "hello";
        String desiredUserName = "kerryann.adams@dhl.com";

        while (rs.next()) {
            String userName = rs.getString("UserName");
            test.info("Fetched UserName: " + userName); // Log each row's data
            if (userName.equals(desiredUserName)) {
                userFound = true;
                break;
            }
        }

        if (userFound) {
            test.pass("User " + desiredUserName +  " exists in the database.");
        } else {
            test.fail("User " + desiredUserName + " does not exist in the database.");
        }
    }

}

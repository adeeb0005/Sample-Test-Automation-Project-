package com.TEST.utils;

import com.aventstack.extentreports.ExtentTest;

import java.sql.*;

public class DBUtility {

    private static Connection connection;

    // Connect to DB using details from the config file
//    public static void connectToDB() throws SQLException {
//        String url = Config.DBUrl;
//        String user = Config.DBUser;
//        String password = Config.DBPassword;
//
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            connection = DriverManager.getConnection(url, user, password);
//        } catch (ClassNotFoundException | SQLException e) {
//            throw new SQLException("SQL Server Driver not found.", e);
//        }
//    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    // Execute SQL query and return results
    public static ResultSet executeQuery(String query, ExtentTest test) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;

        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }

        try {
            test.info("Executing Query: " + query); // Log query execution
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            test.pass("Query executed successfully."); // Log success
        } catch (SQLException e) {
            test.fail("Query execution failed: " + e.getMessage()); // Log failure
            throw e; // Rethrow exception
        }

        return resultSet;
    }

    // Execute SQL update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query, ExtentTest test) throws SQLException {
        Statement statement = null;
        int rowsAffected = 0;

        if (connection == null || connection.isClosed()) {
            throw new SQLException("No active database connection.");
        }

        try {
            test.info("Executing Update Query: " + query); // Log query execution
            statement = connection.createStatement();
            rowsAffected = statement.executeUpdate(query);
            test.pass("Update query executed successfully. Rows affected: " + rowsAffected); // Log success
        } catch (SQLException e){
            test.fail("Update query execution failed: " + e.getMessage()); // Log failure
            throw e; // Rethrow exception
        }

        return rowsAffected;
    }

    // Close the DB connection
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

package com.ems.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection
 * 
 * This class manages the connection to the MySQL database `ems_db`.
 * It provides a reusable static method to obtain a database connection.
 */
public class DBConnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/ems_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Darshan"; 

    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * @return Connection object if successful, null if connection fails.
     */
    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Return DB connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}

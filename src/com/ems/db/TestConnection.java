package com.ems.db;

import java.sql.Connection;

/**
 * TestConnection
 * 
 * This is a simple utility class used to test the database connection.
 * It prints a success or failure message to the console.
 */
public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Connected to Database!");
        } else {
            System.out.println("❌ Connection Failed.");
        }
    }
}

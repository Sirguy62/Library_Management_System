package com.authapi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/creditrisk";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "secret";

    private DatabaseConnection() {}


    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to: " + URL);
        System.out.println("User: " + USER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
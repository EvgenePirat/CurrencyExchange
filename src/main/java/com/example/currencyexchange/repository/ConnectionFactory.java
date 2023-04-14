package com.example.currencyexchange.repository;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/currency_exchange";
    private static final String PASSWORD = "0000";
    private static final String USERNAME = "root";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        return connection;
    }
}

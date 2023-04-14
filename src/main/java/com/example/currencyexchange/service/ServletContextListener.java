package com.example.currencyexchange.service;

import com.example.currencyexchange.repository.ConnectionFactory;
import jakarta.servlet.ServletContextEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServletContextListener implements jakarta.servlet.ServletContextListener {
    private final String SCHEMA_CURRENCY = """
                CREATE TABLE IF NOT EXISTS currencies (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    code VARCHAR(255) NOT NULL UNIQUE,
                    full_name VARCHAR(255) DEFAULT NULL,
                    sign VARCHAR(255) NOT NULL
                );
            """;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(SCHEMA_CURRENCY);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("I can not get connection!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

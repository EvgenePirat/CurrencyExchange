package com.example.currencyexchange.service;

import com.example.currencyexchange.repository.ConnectionFactory;
import jakarta.servlet.ServletContextEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServletContextListener implements jakarta.servlet.ServletContextListener {
    private final String TABLE_CURRENCY = """
                CREATE TABLE IF NOT EXISTS currencies (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    code VARCHAR(255) NOT NULL UNIQUE,
                    full_name VARCHAR(255) NOT NULL,
                    sign VARCHAR(255) NOT NULL
                );
            """;

    private final String TABLE_EXCHANGE_RATES = """
                CREATE TABLE IF NOT EXISTS exchange_rates (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      base_currency_id INT NOT NULL,
                      target_currency_id INT NOT NULL,
                      rate DECIMAL(12,6) NOT NULL,
                      CONSTRAINT fk_base_currency FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
                      CONSTRAINT fk_target_currency FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
                      UNIQUE KEY idx_ExchangeRates_Base_Target (base_currency_id, target_currency_id)
                  );
            """;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(TABLE_CURRENCY);
            statement.execute(TABLE_EXCHANGE_RATES);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("I can not get connection!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

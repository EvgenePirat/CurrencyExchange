package com.example.currencyexchange.repository.impl;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.repository.ConnectionFactory;
import com.example.currencyexchange.repository.ExchangeRatesRepository;
import com.example.currencyexchange.repository.mappers.ExchangeRatesRowMapper;

import java.sql.*;
import java.util.List;

public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository {

    private final String INSERT_NEW_EXCHANGE_RATES = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)""";

    private final String GET_ALL_EXCHANGE_RATES = """
            SELECT 
                er.id, 
                JSON_OBJECT(
                    'id', bc.id,
                    'full_name', bc.full_name,
                    'code', bc.code,
                    'sign', bc.sign
                ) AS baseCurrency,
                JSON_OBJECT(
                    'id', tc.id,
                    'full_name', tc.full_name,
                    'code', tc.code,
                    'sign', tc.sign
                ) AS targetCurrency,
                er.rate
            FROM exchange_rates er
            JOIN currencies bc ON er.base_currency_id = bc.id
            JOIN currencies tc ON er.target_currency_id = tc.id; 
            """;

    @Override
    public ExchangeRates save(ExchangeRates exchangeRates) throws SQLException, ClassNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_EXCHANGE_RATES, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,exchangeRates.getBaseCurrencyId().getId());
            preparedStatement.setInt(2,exchangeRates.getTargetCurrencyId().getId());
            preparedStatement.setBigDecimal(3, exchangeRates.getRate());
            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                exchangeRates.setId(resultSet.getInt(1));
            }
            return exchangeRates;
        }
    }

    @Override
    public List<ExchangeRates> getAll() throws SQLException, ClassNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();
            try(ResultSet resultSet = statement.executeQuery(GET_ALL_EXCHANGE_RATES)){
                return ExchangeRatesRowMapper.mapRows(resultSet);
            }
        }
    }
}

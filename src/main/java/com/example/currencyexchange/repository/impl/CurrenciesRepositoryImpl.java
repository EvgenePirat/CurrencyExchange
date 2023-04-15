package com.example.currencyexchange.repository.impl;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.repository.ConnectionFactory;
import com.example.currencyexchange.repository.CurrenciesRepository;
import com.example.currencyexchange.repository.mappers.CurrencyRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CurrenciesRepositoryImpl implements CurrenciesRepository {

    private final String GET_CURRENCY_WITH_CODE = """
                SELECT id, code, full_name, sign FROM currencies WHERE code = ?
            """;
    private final String INSERT_CURRENCY = """
            INSERT INTO currencies (code, full_name, sign)
            VALUES (?, ?, ?)""";

    private final String GET_ALL_CURRENCIES = "SELECT id, code, full_name, sign FROM currencies";


    @Override
    public Currency getCurrencyWithCode(String code) throws SQLException, CurrencyNotFoundException, CurrencyCodeNotFoundException, ClassNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection();) {
            connection.setReadOnly(true);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CURRENCY_WITH_CODE);
            preparedStatement.setString(1,code);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return CurrencyRowMapper.mapRow(resultSet);
            }
        }
    }

    @Override
    public Currency save(Currency currency) throws SQLException, CurrencyAlreadyExistException, ClassNotFoundException {
        if(currency.getId() != -1) throw new CurrencyAlreadyExistException();
        try(Connection connection = ConnectionFactory.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CURRENCY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,currency.getCode());
            preparedStatement.setString(2,currency.getFullName());
            preparedStatement.setString(3,currency.getSign());
            preparedStatement.executeUpdate();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                currency.setId(resultSet.getInt(1));
            }
            return currency;
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws SQLException, ClassNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection();){
            connection.setReadOnly(true);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CURRENCIES);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return CurrencyRowMapper.mapRows(resultSet);
            }
        }
    }
}

package com.example.currencyexchange.repository.impl;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.config.ConnectionFactory;
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

    private final String DELETE_FROM_CURRENCIES = "DELETE FROM currencies WHERE id = ?;";

    private final String GET_CODE_WITH_CODE = "SELECT code FROM currencies WHERE code = ?";

    private final String GET_ID_WITH_ID_FOR_CHECK = "SELECT id FROM currencies WHERE id = ?";



    @Override
    public Currency getCurrencyWithCode(String code) throws SQLException, CurrencyNotFoundException, ClassNotFoundException, CurrencyCodeNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection()) {
            connection.setReadOnly(true);
            PreparedStatement preparedStatementForCheck = connection.prepareStatement(GET_CODE_WITH_CODE);
            preparedStatementForCheck.setString(1,code);
            ResultSet resultSetForCheck = preparedStatementForCheck.executeQuery();
            if(resultSetForCheck.next() == false) throw new CurrencyCodeNotFoundException();
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

    @Override
    public void delete(int idCurrency) throws SQLException, ClassNotFoundException, CurrencyNotFoundException{
        try(Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementForCheckId = connection.prepareStatement(GET_ID_WITH_ID_FOR_CHECK);
            preparedStatementForCheckId.setInt(1,idCurrency);
            ResultSet resultSetForCheckId = preparedStatementForCheckId.executeQuery();
            if(resultSetForCheckId.next() == false) throw new CurrencyNotFoundException();

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_CURRENCIES);
            preparedStatement.setInt(1,idCurrency);
            preparedStatement.execute();
            connection.commit();
        }
    }

    @Override
    public Currency update(Currency currencyForUpdate) throws SQLException, ClassNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ExchangeRatesRepositoryImpl.UPDATE_CURRENCY);
            preparedStatement.setString(1,currencyForUpdate.getCode());
            preparedStatement.setString(2,currencyForUpdate.getFullName());
            preparedStatement.setString(3,currencyForUpdate.getSign());
            preparedStatement.setInt(4,currencyForUpdate.getId());
            preparedStatement.execute();
        }
        return currencyForUpdate;
    }
}

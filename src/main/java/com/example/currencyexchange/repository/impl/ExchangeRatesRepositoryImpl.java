package com.example.currencyexchange.repository.impl;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;
import com.example.currencyexchange.config.ConnectionFactory;
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

    private final String GET_EXCHANGE_RATES_WITH_CODE = """
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
            JOIN currencies tc ON er.target_currency_id = tc.id
            WHERE bc.code = ? AND tc.code = ?; 
            """;
    private final String GET_EXCHANGE_RATES_FOR_CHECK = """
            SELECT er.id, er.base_currency_id, er.target_currency_id FROM exchange_rates er
            JOIN currencies bc ON er.base_currency_id = bc.id
            JOIN currencies tc ON er.target_currency_id = tc.id
            WHERE bc.code = ? AND tc.code = ?; 
            """;

    public static final String UPDATE_CURRENCY = """
            UPDATE currencies SET code = ?, full_name = ?, sign = ? WHERE id = ?;
            """;

    private final String UPDATE_EXCHANGE_RATES = """
            UPDATE exchange_rates er JOIN currencies bc ON er.base_currency_id = bc.id SET er.rate = ? WHERE er.id = ? AND er.base_currency_id = ?;
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
            connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            try(ResultSet resultSet = statement.executeQuery(GET_ALL_EXCHANGE_RATES)){
                return ExchangeRatesRowMapper.mapRows(resultSet);
            }
        }
    }

    @Override
    public ExchangeRates findWithCodePair(String codeBasic, String codeTarget) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection()){
            connection.setReadOnly(true);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_EXCHANGE_RATES_WITH_CODE);
            preparedStatement.setString(1,codeBasic);
            preparedStatement.setString(2,codeTarget);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return ExchangeRatesRowMapper.mapRow(resultSet);
            }
        }
    }

    @Override
    public ExchangeRates update(String codeBasic, String codeTarget, ExchangeRates exchangeRatesForUpdate) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException {
        try(Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementForCheck = connection.prepareStatement(GET_EXCHANGE_RATES_FOR_CHECK);
            preparedStatementForCheck.setString(1,codeBasic);
            preparedStatementForCheck.setString(2,codeTarget);
            int idForUpdate = 0, idBasicCurrency, idTargetCurrency;
            try(ResultSet resultSet = preparedStatementForCheck.executeQuery()) {
                if(resultSet.next()){
                    idForUpdate = resultSet.getInt(1);
                    idBasicCurrency = resultSet.getInt(2);
                    idTargetCurrency = resultSet.getInt(3);
                }else throw new ExchangeRateNotFoundException();
            }
            PreparedStatement preparedStatementForUpdateCurrency = connection.prepareStatement(UPDATE_CURRENCY);
            preparedStatementForUpdateCurrency = setAllParameterForCurrency(preparedStatementForUpdateCurrency,exchangeRatesForUpdate.getBaseCurrencyId().getCode(),exchangeRatesForUpdate.getBaseCurrencyId().getFullName(),exchangeRatesForUpdate.getBaseCurrencyId().getSign(),idBasicCurrency);
            preparedStatementForUpdateCurrency.execute();
            preparedStatementForUpdateCurrency = setAllParameterForCurrency(preparedStatementForUpdateCurrency,exchangeRatesForUpdate.getTargetCurrencyId().getCode(),exchangeRatesForUpdate.getTargetCurrencyId().getFullName(),exchangeRatesForUpdate.getTargetCurrencyId().getSign(),idTargetCurrency);
            preparedStatementForUpdateCurrency.execute();
            PreparedStatement preparedStatementForUpdateExchangeRates = connection.prepareStatement(UPDATE_EXCHANGE_RATES);
            preparedStatementForUpdateExchangeRates.setBigDecimal(1,exchangeRatesForUpdate.getRate());
            preparedStatementForUpdateExchangeRates.setInt(2,idForUpdate);
            preparedStatementForUpdateExchangeRates.setInt(3,idBasicCurrency);
            System.out.println(preparedStatementForUpdateExchangeRates);
            preparedStatementForUpdateExchangeRates.execute();
            connection.commit();
            return exchangeRatesForUpdate;
        }
    }

    private PreparedStatement setAllParameterForCurrency(PreparedStatement preparedStatementForUpdateCurrency, String code, String fullName, String sign, int id) throws SQLException {
        preparedStatementForUpdateCurrency.setString(1,code);
        preparedStatementForUpdateCurrency.setString(2,fullName);
        preparedStatementForUpdateCurrency.setString(3,sign);
        preparedStatementForUpdateCurrency.setInt(4,id);
        return preparedStatementForUpdateCurrency;
    }
}

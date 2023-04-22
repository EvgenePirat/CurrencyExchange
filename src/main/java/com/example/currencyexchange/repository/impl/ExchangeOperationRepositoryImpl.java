package com.example.currencyexchange.repository.impl;

import com.example.currencyexchange.entity.ExchangeOperation;
import com.example.currencyexchange.exception.ExchangeOperationNotFoundException;
import com.example.currencyexchange.config.ConnectionFactory;
import com.example.currencyexchange.repository.ExchangeOperationRepository;
import com.example.currencyexchange.repository.mappers.ExchangeOperationRowMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeOperationRepositoryImpl implements ExchangeOperationRepository {

    private final String GET_EXCHANGE_RATES_WITH_CODE = """
            SELECT 
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
            WHERE EXISTS(SELECT er.id, er.base_currency_id, er.target_currency_id FROM exchange_rates er
                JOIN currencies bc ON er.base_currency_id = bc.id
                JOIN currencies tc ON er.target_currency_id = tc.id
                WHERE bc.code = ? AND tc.code = ?) AND bc.code = ? AND tc.code = ?; 
            """;

    @Override
    public ExchangeOperation converted(String codeBaseCurrency, String codeTargetCurrency, double amount) throws SQLException, ClassNotFoundException, ExchangeOperationNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setReadOnly(true);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_EXCHANGE_RATES_WITH_CODE);
            preparedStatement = setDateInPS(preparedStatement,codeBaseCurrency,codeTargetCurrency,amount);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(checkResultSet(resultSet)){
                    return ExchangeOperationRowMap.mapRowRight(resultSet,amount);
                }else {
                    preparedStatement = setDateInPS(preparedStatement,codeTargetCurrency,codeBaseCurrency,amount);
                    try(ResultSet resultSetRevers = preparedStatement.executeQuery()) {
                        if(checkResultSet(resultSetRevers)){
                            return ExchangeOperationRowMap.mapRowReverse(resultSetRevers,amount);
                        }
                    }
                }
            }
        }
        throw new ExchangeOperationNotFoundException();
    }

    private boolean checkResultSet(ResultSet resultSet) throws SQLException {
        return resultSet.next() == true ? true : false;
    }

    private PreparedStatement setDateInPS(PreparedStatement preparedStatement,String codeBaseCurrency, String codeTargetCurrency, double amount) throws SQLException {
        preparedStatement.setString(1,codeBaseCurrency);
        preparedStatement.setString(2,codeTargetCurrency);
        preparedStatement.setString(3,codeBaseCurrency);
        preparedStatement.setString(4,codeTargetCurrency);
        return preparedStatement;
    }
}

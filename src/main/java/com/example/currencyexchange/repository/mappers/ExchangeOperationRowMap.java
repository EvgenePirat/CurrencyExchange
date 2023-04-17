package com.example.currencyexchange.repository.mappers;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.entity.ExchangeOperation;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeOperationRowMap {

    private static final Gson gson = new Gson();

    public static ExchangeOperation mapRowRight(ResultSet resultSet, double amount) throws SQLException {
        ExchangeOperation exchangeOperation = new ExchangeOperation();
        String baseCurrency = resultSet.getString(1);
        exchangeOperation.setBaseCurrency(gson.fromJson(baseCurrency, Currency.class));
        String targetCurrency = resultSet.getString(2);
        exchangeOperation.setTargetCurrency(gson.fromJson(targetCurrency, Currency.class));
        exchangeOperation.setAmount(BigDecimal.valueOf(amount));
        exchangeOperation.setRate(resultSet.getBigDecimal(3));
        exchangeOperation.setConvertedAmount(amount*exchangeOperation.getRate().doubleValue());
        return exchangeOperation;
    }

    public static ExchangeOperation mapRowReverse(ResultSet resultSet, double amount) throws SQLException {
        ExchangeOperation exchangeOperation = new ExchangeOperation();
        String baseCurrency = resultSet.getString(1);
        String targetCurrency = resultSet.getString(2);
        exchangeOperation.setBaseCurrency(gson.fromJson(targetCurrency, Currency.class));
        exchangeOperation.setTargetCurrency(gson.fromJson(baseCurrency, Currency.class));
        exchangeOperation.setAmount(BigDecimal.valueOf(amount));
        exchangeOperation.setRate(resultSet.getBigDecimal(3));
        exchangeOperation.setConvertedAmount(amount/exchangeOperation.getRate().doubleValue());
        return exchangeOperation;
    }
}

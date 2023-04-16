package com.example.currencyexchange.repository.mappers;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesRowMapper {

    private static final Gson gson = new Gson();

    public static ExchangeRates mapRow(ResultSet resultSet) throws SQLException, ExchangeRateNotFoundException {
        if(resultSet.next()){
            ExchangeRates exchangeRates = fullExchangeRate(resultSet);
            return exchangeRates;
        }else {
            throw new ExchangeRateNotFoundException();
        }
    }

    private static ExchangeRates fullExchangeRate(ResultSet resultSet) throws SQLException {
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setId(resultSet.getInt(1));
        String baseCurrency = resultSet.getString(2);
        exchangeRates.setBaseCurrencyId(gson.fromJson(baseCurrency, Currency.class));
        String targetCurrency = resultSet.getString(3);
        exchangeRates.setTargetCurrencyId(gson.fromJson(targetCurrency, Currency.class));
        exchangeRates.setRate(resultSet.getBigDecimal(4));
        return exchangeRates;
    }

    public static List<ExchangeRates> mapRows(ResultSet resultSet) throws SQLException {
        List<ExchangeRates> exchangeRatesList = new ArrayList<>();
        while (resultSet.next()){
            ExchangeRates exchangeRates = fullExchangeRate(resultSet);
            exchangeRatesList.add(exchangeRates);
        }
        return exchangeRatesList;
    }
}

package com.example.currencyexchange.repository.mappers;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRowMapper {

    public static Currency mapRow(ResultSet resultSet) throws SQLException, CurrencyNotFoundException {
        Currency currency = new Currency();
        if(resultSet.next()){
            currency.setId(resultSet.getInt("id"));
            currency.setCode(resultSet.getString("code"));
            currency.setFullName(resultSet.getString("full_name"));
            currency.setSign(resultSet.getString("sign"));
        }else {
            throw new CurrencyNotFoundException();
        }
        return currency;
    }

    public static List<Currency> mapRows(ResultSet resultSet) throws SQLException {
        List<Currency> currencyList = new ArrayList<>();
        while (resultSet.next()){
            Currency currency = new Currency();
            currency.setId(resultSet.getInt("id"));
            currency.setFullName(resultSet.getString("full_name"));
            currency.setCode(resultSet.getString("code"));
            currency.setSign(resultSet.getString("sign"));
            currencyList.add(currency);
        }
        return currencyList;
    }
}

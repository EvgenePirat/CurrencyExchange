package com.example.currencyexchange.repository.mappers;

import com.example.currencyexchange.entity.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRowMapper {

    public static Currency mapRow(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        if(resultSet.next()){
            currency.setId(resultSet.getLong("id"));
            currency.setCode(resultSet.getString("code"));
            currency.setFullName("full_name");
            currency.setSign(resultSet.getString("sign"));
        }
        return currency;
    }

    public static List<Currency> mapRows(ResultSet resultSet) throws SQLException {
        List<Currency> taskList = new ArrayList<>();
        while (resultSet.next()){
            Currency currency = new Currency();
            currency.setId(resultSet.getLong("id"));
            currency.setFullName(resultSet.getString("full_name"));
            currency.setCode(resultSet.getString("code"));
            currency.setSign(resultSet.getString("sign"));
            taskList.add(currency);
        }
        return taskList;
    }
}

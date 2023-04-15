package com.example.currencyexchange.repository;

import com.example.currencyexchange.entity.ExchangeRates;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRatesRepository {

    ExchangeRates save(ExchangeRates exchangeRates) throws SQLException, ClassNotFoundException;

    List<ExchangeRates> getAll() throws SQLException, ClassNotFoundException;

}

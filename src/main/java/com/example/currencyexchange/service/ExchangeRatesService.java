package com.example.currencyexchange.service;

import com.example.currencyexchange.entity.ExchangeRates;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRatesService {

    ExchangeRates save(ExchangeRates exchangeRates) throws SQLException, ClassNotFoundException;

    List<ExchangeRates> getAll() throws SQLException, ClassNotFoundException;

}

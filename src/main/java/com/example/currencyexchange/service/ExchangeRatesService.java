package com.example.currencyexchange.service;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRatesService {

    ExchangeRates save(ExchangeRates exchangeRates) throws SQLException, ClassNotFoundException;

    List<ExchangeRates> getAll() throws SQLException, ClassNotFoundException;

    ExchangeRates findWithCodePair(String codeBasic, String codeTarget) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException;

    ExchangeRates update(String codeBasic, String codeTarget, ExchangeRates exchangeRatesForUpdate) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException;

}

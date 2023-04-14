package com.example.currencyexchange.repository;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface CurrenciesRepository {

    Currency getCurrencyWithCode(String code) throws SQLException, CurrencyNotFoundException, CurrencyCodeNotFoundException, ClassNotFoundException;

    Currency save(Currency currency) throws SQLException, CurrencyAlreadyExistException, ClassNotFoundException;

    List<Currency> getAllCurrencies() throws SQLException, ClassNotFoundException;
}

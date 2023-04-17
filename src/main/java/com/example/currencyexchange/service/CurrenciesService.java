package com.example.currencyexchange.service;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface CurrenciesService {

    List<Currency> getAllCurrencies() throws SQLException, ClassNotFoundException;

    Currency getCurrencyWithCode(String code) throws SQLException, CurrencyNotFoundException, CurrencyCodeNotFoundException, ClassNotFoundException;

    Currency save(Currency currency) throws SQLException, CurrencyAlreadyExistException, ClassNotFoundException;

    void delete(int idCurrency) throws SQLException, ClassNotFoundException;

    Currency update(Currency currencyForUpdate) throws SQLException, ClassNotFoundException;
}

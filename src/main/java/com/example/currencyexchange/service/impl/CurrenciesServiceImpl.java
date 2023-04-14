package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.repository.CurrenciesRepository;
import com.example.currencyexchange.repository.impl.CurrenciesRepositoryImpl;
import com.example.currencyexchange.service.CurrenciesService;

import java.sql.SQLException;
import java.util.List;

public class CurrenciesServiceImpl implements CurrenciesService {

    private final CurrenciesRepository currenciesRepository = new CurrenciesRepositoryImpl();

    @Override
    public List<Currency> getAllCurrencies() throws SQLException, ClassNotFoundException {
        return currenciesRepository.getAllCurrencies();
    }

    @Override
    public Currency getCurrencyWithCode(String code) throws SQLException, CurrencyNotFoundException, CurrencyCodeNotFoundException, ClassNotFoundException {
        return currenciesRepository.getCurrencyWithCode(code);
    }

    @Override
    public Currency save(Currency currency) throws SQLException, CurrencyAlreadyExistException, ClassNotFoundException {
        return currenciesRepository.save(currency);
    }
}

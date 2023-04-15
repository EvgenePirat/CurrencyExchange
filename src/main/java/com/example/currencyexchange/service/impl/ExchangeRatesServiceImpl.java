package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.repository.ExchangeRatesRepository;
import com.example.currencyexchange.repository.impl.ExchangeRatesRepositoryImpl;
import com.example.currencyexchange.service.ExchangeRatesService;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private final ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepositoryImpl();

    @Override
    public ExchangeRates save(ExchangeRates exchangeRates) throws SQLException, ClassNotFoundException {
        return exchangeRatesRepository.save(exchangeRates);
    }

    @Override
    public List<ExchangeRates> getAll() throws SQLException, ClassNotFoundException {
        return exchangeRatesRepository.getAll();
    }
}

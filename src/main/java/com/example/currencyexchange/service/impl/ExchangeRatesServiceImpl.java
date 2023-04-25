package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;
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

    @Override
    public ExchangeRates findWithCodePair(String codeBasic, String codeTarget) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException {
        return exchangeRatesRepository.findWithCodePair(codeBasic,codeTarget);
    }

    @Override
    public ExchangeRates update(String codeBasic, String codeTarget, ExchangeRates exchangeRatesForUpdate) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException {
        return exchangeRatesRepository.update(codeBasic,codeTarget,exchangeRatesForUpdate);
    }

    @Override
    public void delete(int id) throws SQLException, ClassNotFoundException, ExchangeRateNotFoundException {
        exchangeRatesRepository.delete(id);
    }
}

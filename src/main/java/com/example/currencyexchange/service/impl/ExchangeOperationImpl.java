package com.example.currencyexchange.service.impl;

import com.example.currencyexchange.entity.ExchangeOperation;
import com.example.currencyexchange.exception.ExchangeOperationNotFoundException;
import com.example.currencyexchange.repository.ExchangeOperationRepository;
import com.example.currencyexchange.repository.impl.ExchangeOperationRepositoryImpl;
import com.example.currencyexchange.service.ExchangeOperationService;

import java.sql.SQLException;

public class ExchangeOperationImpl implements ExchangeOperationService {

    private ExchangeOperationRepository exchangeOperationRepository = new ExchangeOperationRepositoryImpl();

    @Override
    public ExchangeOperation converted(String codeBaseCurrency, String codeTargetCurrency, double amount) throws SQLException, ClassNotFoundException, ExchangeOperationNotFoundException {
        return exchangeOperationRepository.converted(codeBaseCurrency,codeTargetCurrency,amount);
    }
}

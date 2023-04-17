package com.example.currencyexchange.service;

import com.example.currencyexchange.entity.ExchangeOperation;
import com.example.currencyexchange.exception.ExchangeOperationNotFoundException;

import java.sql.SQLException;

public interface ExchangeOperationService {

    ExchangeOperation converted(String codeBaseCurrency, String codeTargetCurrency, double amount) throws SQLException, ClassNotFoundException, ExchangeOperationNotFoundException;
}

package com.example.currencyexchange.repository;

import com.example.currencyexchange.entity.ExchangeOperation;
import com.example.currencyexchange.exception.ExchangeOperationNotFoundException;

import java.sql.SQLException;

public interface ExchangeOperationRepository {

    ExchangeOperation converted(String codeBaseCurrency, String codeTargetCurrency, double amount) throws SQLException, ClassNotFoundException, ExchangeOperationNotFoundException;

}

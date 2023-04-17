package com.example.currencyexchange.controller;

import com.example.currencyexchange.entity.ExchangeOperation;
import com.example.currencyexchange.exception.ExceptionBody;
import com.example.currencyexchange.exception.ExchangeOperationNotFoundException;
import com.example.currencyexchange.service.ExchangeOperationService;
import com.example.currencyexchange.service.impl.ExchangeOperationImpl;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ExchangeOperationServlet extends HttpServlet {

    private ExchangeOperationService exchangeOperationService = new ExchangeOperationImpl();

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String codeBaseCurrency = request.getParameter("from");
        String codeTargetCurrency = request.getParameter("to");
        double amount = Double.valueOf(request.getParameter("amount"));
        try{
            ExchangeOperation exchangeOperation = exchangeOperationService.converted(codeBaseCurrency,codeTargetCurrency,amount);
            String exchangeOperationToJson = gson.toJson(exchangeOperation);
            response.setStatus(200);
            response.getWriter().write(exchangeOperationToJson);
        }catch (IOException e){
            response = getReadyResponse(400,"Required form field is missing", response);
        }catch (SQLException e){
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD", response);
        }catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }catch (ExchangeOperationNotFoundException e){
            response = getReadyResponse(404,"Exchange operation can not make because exchange rates not found!",response);
        }
    }

    private HttpServletResponse getReadyResponse(int code, String message, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        String responseError = gson.toJson(new ExceptionBody(message, code));
        response.getWriter().write(responseError);
        return response;
    }
}

package com.example.currencyexchange.controller;

import com.example.currencyexchange.entity.Currency;
import com.example.currencyexchange.exception.CurrencyAlreadyExistException;
import com.example.currencyexchange.exception.CurrencyCodeNotFoundException;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.ExceptionBody;
import com.example.currencyexchange.service.CurrenciesService;
import com.example.currencyexchange.service.impl.CurrenciesServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrenciesServlet extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesServiceImpl();

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            if(code == null){
                List<Currency> currencyList = currenciesService.getAllCurrencies();
                response.setStatus(200);
                String currencyInJson = gson.toJson(currencyList);
                response.getWriter().write(currencyInJson);
            }else {
                Currency foundCurrency = currenciesService.getCurrencyWithCode(code);
                response.setStatus(200);
                String currencyInJson = gson.toJson(foundCurrency);
                response.getWriter().write(currencyInJson);
            }
        }catch (SQLException e){
            response.setStatus(500);
            String responseError = gson.toJson(new ExceptionBody("Error with BD", 500));
            response.getWriter().write(responseError);
        }catch (CurrencyNotFoundException e){
            response.setStatus(404);
            String responseError = gson.toJson(new ExceptionBody("Currency not found!", 404));
            response.getWriter().write(responseError);
        }catch (CurrencyCodeNotFoundException e){
            response.setStatus(400);
            String responseError = gson.toJson(new ExceptionBody("Code not found!", 400));
            response.getWriter().write(responseError);
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            String responseError = gson.toJson(new ExceptionBody("Error with BD", 500));
            response.getWriter().write(responseError);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            Currency currencyForAdd = gson.fromJson(request.getReader(), Currency.class);
            Currency currencyAfterSave = currenciesService.save(currencyForAdd);
            response.setStatus(200);
            String currencyInJSON = gson.toJson(currencyAfterSave);
            response.getWriter().write(currencyInJSON);
        }catch (IOException e){
            response.setStatus(400);
            String responseError = gson.toJson(new ExceptionBody("Required form field is missing", 400));
            response.getWriter().write(responseError);
        }catch (CurrencyAlreadyExistException e){
            response.setStatus(409);
            String responseError = gson.toJson(new ExceptionBody("Currency with this code already exists", 409));
            response.getWriter().write(responseError);
        }catch (SQLException e){
            response.setStatus(500);
            String responseError = gson.toJson(new ExceptionBody("Error with BD", 500));
            response.getWriter().write(responseError);
        } catch (ClassNotFoundException e) {
            response.setStatus(500);
            String responseError = gson.toJson(new ExceptionBody("Error with BD", 500));
            response.getWriter().write(responseError);
        }
    }
}

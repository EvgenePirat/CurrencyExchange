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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            String code = request.getParameter("code");
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
            response = getReadyResponse(500,"Error with BD", response);
        }catch (CurrencyNotFoundException e){
            response = getReadyResponse(404,"Currency not found!",response);
        }catch (CurrencyCodeNotFoundException e){
            response = getReadyResponse(400,"Code not found!",response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            Currency currencyForAdd = (Currency) request.getAttribute("currency");
            Currency currencyAfterSave = currenciesService.save(currencyForAdd);
            response.setStatus(200);
            String currencyInJSON = gson.toJson(currencyAfterSave);
            response.getWriter().write(currencyInJSON);
        }catch (IOException e){
            response = getReadyResponse(400,"Required form field is missing", response);
        }catch (CurrencyAlreadyExistException e){
            response = getReadyResponse(409,"Currency with this code already exists",response);
        }catch (SQLException e){
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            Currency currency = (Currency) request.getAttribute("currency");
            Currency currencyAfterUpdate = currenciesService.update(currency);
            String currencyAfterUpdateToGson = gson.toJson(currencyAfterUpdate);
            response.getWriter().write(currencyAfterUpdateToGson);
        }catch (SQLException e){
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idCurrencies = request.getParameter("id");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            currenciesService.delete(Integer.valueOf(idCurrencies));
            response.setStatus(200);
            response = getReadyResponse(200,"deleted successfully",response);
        }catch (SQLException e){
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }

    }

    private HttpServletResponse getReadyResponse(int code, String message, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        String responseError = gson.toJson(new ExceptionBody(message, code));
        response.getWriter().write(responseError);
        return response;
    }
}

package com.example.currencyexchange.controller;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.ExceptionBody;
import com.example.currencyexchange.service.ExchangeRatesService;
import com.example.currencyexchange.service.impl.ExchangeRatesServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRatesServlet extends HttpServlet {

    private final Gson gson = new Gson();

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codePair = request.getParameter("codePare");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            if(codePair == null){
                List<ExchangeRates> exchangeRatesList = exchangeRatesService.getAll();
                String exchangeRatesListToJson = gson.toJson(exchangeRatesList);
                response.setStatus(200);
                response.getWriter().write(exchangeRatesListToJson);
            }
        }catch (IOException e){
            response.setStatus(400);
            String responseError = gson.toJson(new ExceptionBody("Required form field is missing", 400));
            response.getWriter().write(responseError);
        } catch (SQLException e) {
            response.setStatus(500);
            String responseError = gson.toJson(new ExceptionBody("Error with BD", 500));
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
        try{
            ExchangeRates exchangeRates = gson.fromJson(request.getReader(), ExchangeRates.class);
            ExchangeRates exchangeRatesAfterSave = exchangeRatesService.save(exchangeRates);
            response.setStatus(200);
            String exchangeRatesInStringJson = gson.toJson(exchangeRatesAfterSave);
            response.getWriter().write(exchangeRatesInStringJson);
        }catch (IOException e){
            response.setStatus(400);
            String responseError = gson.toJson(new ExceptionBody("Required form field is missing", 400));
            response.getWriter().write(responseError);
        } catch (SQLException e) {
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

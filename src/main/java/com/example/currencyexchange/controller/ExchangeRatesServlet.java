package com.example.currencyexchange.controller;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.CurrencyNotFoundException;
import com.example.currencyexchange.exception.ExceptionBody;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;
import com.example.currencyexchange.exception.ValidationException;
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
            }else {
                String codeBasic = codePair.substring(0,3);
                String codeTarget = codePair.substring(3,6);
                ExchangeRates exchangeRates = exchangeRatesService.findWithCodePair(codeBasic,codeTarget);
                String exchangeRatesInJson = gson.toJson(exchangeRates);
                response.getWriter().write(exchangeRatesInJson);
            }
        }catch (IOException e){
            response = getReadyResponse(400,"Required form field is missing",response);
        } catch (SQLException e) {
            response = getReadyResponse(500,"Error with BD",response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ExchangeRateNotFoundException e) {
            response = getReadyResponse(404,"Exchange rate for pair not found",response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            ExchangeRates exchangeRates = (ExchangeRates) request.getAttribute("exchangeRates");
            ExchangeRates exchangeRatesAfterSave = exchangeRatesService.save(exchangeRates);
            response.setStatus(200);
            String exchangeRatesInStringJson = gson.toJson(exchangeRatesAfterSave);
            response.getWriter().write(exchangeRatesInStringJson);
        }catch (SQLException e) {
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD",response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD", response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<String> errorMessages = (List<String>) request.getAttribute("error");
        try {
            if(errorMessages != null) throw new ValidationException();
            String codePair = request.getParameter("codePare");
            String codeBasic = codePair.substring(0,3);
            String codeTarget = codePair.substring(3,6);
            ExchangeRates exchangeRates = (ExchangeRates) request.getAttribute("exchangeRates");
            ExchangeRates exchangeRatesAfterUpdate = exchangeRatesService.update(codeBasic,codeTarget,exchangeRates);
            String exchangeRatesAfterUpdateInJson = gson.toJson(exchangeRatesAfterUpdate);
            response.getWriter().write(exchangeRatesAfterUpdateInJson);
        }catch (SQLException e) {
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD",response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ExchangeRateNotFoundException e) {
            response = getReadyResponse(404,"Exchange rate for pair not found",response);
        } catch (ValidationException e){
            response = getReadyResponse(400, errorMessages.toString(),response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Boolean error = (Boolean) request.getAttribute("error");
        try{
            if(error == null || error == true ) throw new ValidationException();
            exchangeRatesService.delete(Integer.parseInt(id));
            response.setStatus(200);
            response = getReadyResponse(200,"deleted successfully",response);
        }catch (SQLException e){
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ValidationException e){
            response = getReadyResponse(400, "id not sent", response);
        } catch (ExchangeRateNotFoundException e){
            response = getReadyResponse(400, "Exchange rate with id not found",response);
        }
    }

    private HttpServletResponse getReadyResponse(int code, String message, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        String responseError = gson.toJson(new ExceptionBody(message, code));
        response.getWriter().write(responseError);
        return response;
    }
}

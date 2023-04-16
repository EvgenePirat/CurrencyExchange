package com.example.currencyexchange.controller;

import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.exception.ExceptionBody;
import com.example.currencyexchange.exception.ExchangeRateNotFoundException;
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
            ExchangeRates exchangeRates = gson.fromJson(request.getReader(), ExchangeRates.class);
            ExchangeRates exchangeRatesAfterSave = exchangeRatesService.save(exchangeRates);
            response.setStatus(200);
            String exchangeRatesInStringJson = gson.toJson(exchangeRatesAfterSave);
            response.getWriter().write(exchangeRatesInStringJson);
        }catch (IOException e){
            response = getReadyResponse(400,"Required form field is missing",response);
        } catch (SQLException e) {
            response = getReadyResponse(500,"Error with BD",response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String codePair = request.getParameter("codePare");
            String codeBasic = codePair.substring(0,3);
            String codeTarget = codePair.substring(3,6);
            ExchangeRates exchangeRates = gson.fromJson(request.getReader(), ExchangeRates.class);
            ExchangeRates exchangeRatesAfterUpdate = exchangeRatesService.update(codeBasic,codeTarget,exchangeRates);
            String exchangeRatesAfterUpdateInJson = gson.toJson(exchangeRatesAfterUpdate);
            response.getWriter().write(exchangeRatesAfterUpdateInJson);
        }catch (IOException e) {
            response = getReadyResponse(400, "Required form field is missing", response);
        }catch (SQLException e) {
            e.printStackTrace();
            response = getReadyResponse(500,"Error with BD",response);
        } catch (ClassNotFoundException e) {
            response = getReadyResponse(500,"Error with BD", response);
        } catch (ExchangeRateNotFoundException e) {
            response = getReadyResponse(404,"Exchange rate for pair not found",response);
        }
    }

    private HttpServletResponse getReadyResponse(int code, String message, HttpServletResponse response) throws IOException {
        response.setStatus(code);
        String responseError = gson.toJson(new ExceptionBody(message, code));
        response.getWriter().write(responseError);
        return response;
    }
}

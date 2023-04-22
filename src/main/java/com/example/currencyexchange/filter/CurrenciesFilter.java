package com.example.currencyexchange.filter;

import com.example.currencyexchange.config.FactoryValidation;
import com.example.currencyexchange.config.validation.OnCreate;
import com.example.currencyexchange.config.validation.OnUpdate;
import com.example.currencyexchange.entity.Currency;
import com.google.gson.Gson;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CurrenciesFilter implements Filter {

    private final Gson gson = new Gson();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String method = ((HttpServletRequest) servletRequest).getMethod();
        if(method.equals("POST")){
            workingWithUpdateAndPut(servletRequest, servletResponse, filterChain, "POST");
        }else if(method.equals("PUT")){
            workingWithUpdateAndPut(servletRequest, servletResponse, filterChain, "PUT");
        }
    }


    private void workingWithUpdateAndPut(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain, String typeMethod) throws IOException, ServletException {
        Currency currency = gson.fromJson(servletRequest.getReader(), Currency.class);
        Validator validator = FactoryValidation.getValidator();
        Set<ConstraintViolation<Currency>> violations = null;
        if(typeMethod.equals("POST")){
            violations = validator.validate(currency, OnCreate.class);
        }else if(typeMethod.equals("PUT")){
            violations = validator.validate(currency, OnUpdate.class);
        }
        if(!violations.isEmpty()){
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<Currency> violation : violations) {
                errorMessages.add(violation.getMessage());
            }
            servletRequest.setAttribute("error", errorMessages);
            servletRequest.getRequestDispatcher("/page_error.jsp").forward(servletRequest, servletResponse);
        }else{
            servletRequest.setAttribute("currency", currency);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

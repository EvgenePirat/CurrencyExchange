package com.example.currencyexchange.filter;

import com.example.currencyexchange.config.FactoryValidation;
import com.example.currencyexchange.entity.ExchangeRates;
import com.example.currencyexchange.service.validation.OnCreate;
import com.example.currencyexchange.service.validation.OnUpdate;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExchangeRatesFilter implements Filter {

    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String method = ((HttpServletRequest) servletRequest).getMethod();
        if(method.equals("DELETE")){
            workingWithDelete(servletRequest, servletResponse, filterChain);
        }else if(method.equals("POST")){
            workingWithPostAndPut(servletRequest, servletResponse, filterChain,"POST");
        }else if(method.equals("PUT")){
            workingWithPostAndPut(servletRequest, servletResponse, filterChain,"PUT");
        }else if (method.equals("GET")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void workingWithPostAndPut(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain, String typeOperation) throws ServletException, IOException {
        try {
            ExchangeRates exchangeRates = gson.fromJson(servletRequest.getReader(), ExchangeRates.class);
            Validator validator = FactoryValidation.getValidator();
            Set<ConstraintViolation<ExchangeRates>> violations = null;
            if(typeOperation.equals("POST")){
                violations = validator.validate(exchangeRates, OnCreate.class);
            }else if(typeOperation.equals("PUT")){
                violations = validator.validate(exchangeRates, OnUpdate.class);
            }
            if(!violations.isEmpty()){
                List<String> errorMessages = new ArrayList<>();
                for (ConstraintViolation<ExchangeRates> violation : violations) {
                    errorMessages.add(violation.getMessage());
                }
                if(typeOperation.equals("POST")){
                    servletRequest.setAttribute("error", errorMessages);
                    servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
                }else{
                    servletRequest.setAttribute("error", errorMessages);
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }else{
                servletRequest.setAttribute("exchangeRates", exchangeRates);
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }catch (IOException e){
            servletRequest.setAttribute("error", "Required form field is missing");
        }
    }

    private void workingWithDelete(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String id = servletRequest.getParameter("id");
        if(id == null || id.length() == 0){
            servletRequest.setAttribute("error", true);
        }else{
            servletRequest.setAttribute("error", false);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

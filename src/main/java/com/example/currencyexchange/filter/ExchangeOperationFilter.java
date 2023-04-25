package com.example.currencyexchange.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeOperationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String method = ((HttpServletRequest) servletRequest).getMethod();
        if(method.equals("GET")){
            validationForGet(servletRequest,servletResponse,filterChain);
        }
    }

    private void validationForGet(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String codeBaseCurrency = servletRequest.getParameter("from");
        String codeTargetCurrency = servletRequest.getParameter("to");
        String amount = servletRequest.getParameter("amount");
        List<String> listError = new ArrayList<>();
        if(codeBaseCurrency == null || codeTargetCurrency == null || amount == null){
            if(codeBaseCurrency == null) addErrorInList(listError,"Code for base currency"," you not sent");
            if(codeTargetCurrency == null) addErrorInList(listError, "Code for target currency"," you not sent");
            if(amount == null) addErrorInList(listError, "Amount"," you not sent");
        }
        if(codeBaseCurrency != null && codeBaseCurrency.length() != 3) addErrorInList(listError, "Code for base currency"," sent incorrectly");
        if(codeTargetCurrency != null && codeTargetCurrency.length() != 3) addErrorInList(listError, "Code for target currency"," sent incorrectly");
        if(amount != null && amount.length() == 0) addErrorInList(listError, "amount "," sent incorrectly");
        if(amount != null && amount.length() > 0) {
            if(Double.valueOf(amount) < 0) addErrorInList(listError, "amount ", "must be more than zero!");
        }
        if(listError.size() > 0){
            servletRequest.setAttribute("error", listError);
            servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void addErrorInList(List<String> list, String nameField, String textError){
        list.add(nameField+textError);
    }
}

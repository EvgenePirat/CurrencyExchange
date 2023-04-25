package com.example.currencyexchange.entity;

import com.example.currencyexchange.service.validation.OnCreate;
import com.example.currencyexchange.service.validation.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ExchangeRates {

    private int id;

    @NotNull(message = "Base currency must be not null!", groups = {OnUpdate.class, OnCreate.class})
    private Currency BaseCurrencyId;

    @NotNull(message = "Target currency must be not null!", groups = {OnUpdate.class, OnCreate.class})
    private Currency TargetCurrencyId;

    @NotNull(message = "Rate must be not null!", groups = {OnUpdate.class, OnCreate.class})
    @Min(value = 0, message = "Id can not more zero", groups = {OnUpdate.class, OnCreate.class})
    private BigDecimal rate;

    public ExchangeRates() {
    }

    public ExchangeRates(Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBaseCurrencyId() {
        return BaseCurrencyId;
    }

    public void setBaseCurrencyId(Currency baseCurrencyId) {
        BaseCurrencyId = baseCurrencyId;
    }

    public Currency getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public void setTargetCurrencyId(Currency targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "id=" + id +
                ", BaseCurrencyId=" + BaseCurrencyId +
                ", TargetCurrencyId=" + TargetCurrencyId +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRates)) return false;

        ExchangeRates that = (ExchangeRates) o;

        if (getId() != that.getId()) return false;
        if (getBaseCurrencyId() != null ? !getBaseCurrencyId().equals(that.getBaseCurrencyId()) : that.getBaseCurrencyId() != null)
            return false;
        return getRate() != null ? getRate().equals(that.getRate()) : that.getRate() == null;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(getId());
        result = 31 * result + (getBaseCurrencyId() != null ? getBaseCurrencyId().hashCode() : 0);
        result = 31 * result + (getRate() != null ? getRate().hashCode() : 0);
        return result;
    }
}

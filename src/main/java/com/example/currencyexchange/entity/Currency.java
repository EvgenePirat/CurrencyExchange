package com.example.currencyexchange.entity;

import com.example.currencyexchange.service.validation.OnCreate;
import com.example.currencyexchange.service.validation.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Currency {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    @Min(value = 0, message = "Id can not more zero", groups = OnUpdate.class)
    private int id;

    @NotBlank(message = "Code must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Size(max = 3, message = "code must be 3 chars", groups = {OnUpdate.class, OnCreate.class})
    private String code;

    @NotBlank(message = "FullName must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String fullName;

    @NotBlank(message = "Sign must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String sign;

    public Currency() {
        id = -1;
    }

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency that = (Currency) o;

        if (getId() != that.getId()) return false;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(getId());
        result = 31 * result + getCode().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}

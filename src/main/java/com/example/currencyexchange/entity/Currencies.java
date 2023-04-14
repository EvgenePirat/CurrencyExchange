package com.example.currencyexchange.entity;

public class Currencies {

    private int id;

    private String code;

    private String fullName;

    private String sign;

    public Currencies(String code, String fullName, String sign) {
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
        if (!(o instanceof Currencies)) return false;

        Currencies that = (Currencies) o;

        if (getId() != that.getId()) return false;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        int result = getId();
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

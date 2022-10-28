package com.pokemartspringboot.reports;

import java.math.BigDecimal;

public class UserReport {

    private String user_name;
    private String product_name;
    private Integer total_quantity;
    private BigDecimal total_amount;

    public UserReport() {
    }

    public UserReport(String username, String productName, Integer quantity, BigDecimal totalAmount) {
        this.user_name = username;
        this.product_name = productName;
        this.total_quantity = quantity;
        this.total_amount = totalAmount;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(Integer total_quantity) {
        this.total_quantity = total_quantity;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    @Override
    public String toString() {
        return "UserReport{" +
                "username='" + user_name + '\'' +
                ", productName='" + product_name + '\'' +
                ", quantity=" + total_quantity +
                ", totalAmount=" + total_amount +
                '}';
    }
}

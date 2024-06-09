package com.pokemartspringboot.reports;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserReport {
    private String userName;
    private String productName;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
}

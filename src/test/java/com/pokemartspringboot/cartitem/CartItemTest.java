package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemTest {

    @Test
    public void getTotalPrice() {
        //given
        CartItem cartItem = new CartItem(1L, 1L, 5, new Product(1L, "", "", BigDecimal.valueOf(200), ""));

        //when
        BigDecimal result = cartItem.getTotalPrice();

        //then
        BigDecimal expected = BigDecimal.valueOf(1000);

        assertEquals(expected, result);
    }
}
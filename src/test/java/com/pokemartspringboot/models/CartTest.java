package com.pokemartspringboot.models;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    Cart cart;
    CartItem cartItem;
    CartItem cartItem2;
    CartItem cartItem3;

    @BeforeEach
    public void init() {
        cart = new Cart(1L, 1L);
        cartItem = new CartItem(1L, 1L, 5, new Product(1L, "", "", BigDecimal.valueOf(200), ""));
        cartItem2 = new CartItem(2L, 1L, 2, new Product(1L, "", "", BigDecimal.valueOf(600), ""));
        cartItem3 = new CartItem(3L, 1L, 10, new Product(1L, "", "", BigDecimal.valueOf(1000), ""));
    }

    @Test
    public void getTotalQuantity() {
        //given
        cart.getCartItems().addAll(Set.of(cartItem, cartItem2, cartItem3));

        //when
        int result = cart.getTotalQuantity();

        //then
        int expected = 17;

        assertEquals(expected, result);
    }

    @Test
    public void getTotalPrice() {
        //given
        cart.getCartItems().addAll(Set.of(cartItem, cartItem2, cartItem3));

        //when
        BigDecimal result = cart.getTotalCartPrice();

        //then
        BigDecimal expected = BigDecimal.valueOf(12200);

        assertEquals(expected, result);
    }

    @Test
    public void checkOut() {
        //given
        boolean isCheckedOut = cart.isCheckedOut();

        //when
        cart.checkOut();

        //then
        isCheckedOut = cart.isCheckedOut();

        assertTrue(isCheckedOut);
    }
}
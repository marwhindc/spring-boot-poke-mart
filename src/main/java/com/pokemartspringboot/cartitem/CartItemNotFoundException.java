package com.pokemartspringboot.cartitem;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(Long id) {
        super("Unable to find cart item with id: " + id);
    }
}

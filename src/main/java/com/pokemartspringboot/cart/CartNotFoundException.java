package com.pokemartspringboot.cart;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(Long id) {
        super("Unable to find cart with id: " + id);
    }
}

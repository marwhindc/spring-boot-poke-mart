package com.pokemartspringboot.cartitem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(Long id) {
        super("Unable to find cart item with id: " + id);
    }

    public CartItemNotFoundException(Long cartId, Long productId) {
        super("Unable to find cart item with cart id: " + cartId + " and product id: " + productId);
    }
}

package com.pokemartspringboot.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(Long id) {
        super("Unable to find cart with id: " + id);
    }
}

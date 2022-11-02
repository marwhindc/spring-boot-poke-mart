package com.pokemartspringboot.product;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long id) {
        super("Unable to find product with id: " + id);
    }
}

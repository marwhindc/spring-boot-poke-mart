package com.pokemartspringboot.cartitem;

import java.util.List;

public interface CartItemService {

    List<CartItem> findAll();
    CartItem save(CartItem cartItem);
    CartItem findById(Long id);
    void delete(Long id);
}

package com.pokemartspringboot.cartitem;

import java.util.List;

public interface CartItemService {

    List<CartItem> findAll();
    void save(CartItem cartItem);
    CartItem findById(Long id);
    void delete(Long id);
}

package com.pokemartspringboot.cart;

import java.util.List;

public interface CartService {

    List<Cart> findAll();
    void save(Cart cart);
    Cart findById(Long id);
    void delete(Long id);
    List<Cart> findByUserId(Long id);
    Cart findByUserIdAndCheckedOut(Long id, boolean isCheckedOut);
}

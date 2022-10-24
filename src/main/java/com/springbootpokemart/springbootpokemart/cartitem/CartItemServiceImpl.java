package com.springbootpokemart.springbootpokemart.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private CartItemRepository cartItemRepository;

    @Autowired
    CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findById(Long id) {
        Optional<CartItem> optional = cartItemRepository.findById(id);
        CartItem cartItem = null;
        if (optional.isPresent()) {
            cartItem = optional.get();
        } else throw new RuntimeException("Cart Item not found for id: " + id);
        return cartItem;
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}

package com.pokemartspringboot.cartitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> findAll() {
        return cartItemRepository.findAllByOrderByIdAsc();
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findById(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
//        CartItem cartItem = null;
//        if (optional.isPresent()) {
//            cartItem = optional.get();
//        } else throw new RuntimeException("Cart Item not found for id: " + id);
//        return cartItem;
        return cartItem.orElseThrow(() -> new CartItemNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}

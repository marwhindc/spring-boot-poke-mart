package com.pokemartspringboot.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    @Autowired
    CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        Optional<Cart> optional = cartRepository.findById(id);
        Cart cart = null;
        if (optional.isPresent()) {
            cart = optional.get();
        } else throw new RuntimeException("Cart not found for id: " + id);
        return cart;
    }

    @Override
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public List<Cart> findByUserId(Long id) {
        return cartRepository.findByUserIdOrderByIdDesc(id);
    }

    @Override
    public List<Cart> findByUserIdAndCheckedOut(Long id, boolean isCheckedOut) {
        return cartRepository.findByUserIdAndCheckedOutOrderByIdDesc(id, isCheckedOut);
    }
}

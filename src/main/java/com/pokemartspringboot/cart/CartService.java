package com.pokemartspringboot.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    //TODO: should not be able to create cart when it has an ID assigned (IDs are auto-generated)
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    }

    public void delete(Long id) {
        Cart cart = findById(id);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    public List<Cart> findByUserId(Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return cartRepository.findByUserIdOrderByIdDesc(id);
        }
        return new ArrayList<>();
    }

    public List<Cart> findByUserIdAndCheckedOut(Long id, boolean isCheckedOut) {
        User user = userService.findById(id);
        if (user != null) {
            return cartRepository.findByUserIdAndCheckedOutOrderByIdDesc(id, isCheckedOut);
        }
        return new ArrayList<>();
    }
}

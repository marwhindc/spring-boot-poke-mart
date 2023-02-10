package com.pokemartspringboot.cart;

import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    CartServiceImpl(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    //TODO: should not be able to create cart when it has an ID assigned (IDs are auto-generated)
    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        Cart cart = findById(id);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    @Override
    public List<Cart> findByUserId(Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return cartRepository.findByUserIdOrderByIdDesc(id);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Cart> findByUserIdAndCheckedOut(Long id, boolean isCheckedOut) {
        User user = userService.findById(id);
        if (user != null) {
            return cartRepository.findByUserIdAndCheckedOutOrderByIdDesc(id, isCheckedOut);
        }
        return new ArrayList<>();
    }
}

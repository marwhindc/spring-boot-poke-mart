package com.pokemartspringboot.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping
    public List<Cart> getCarts() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable("id") Long id) {
        return cartService.findById(id);
    }
}

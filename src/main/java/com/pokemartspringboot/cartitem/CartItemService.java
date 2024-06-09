package com.pokemartspringboot.cartitem;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;

@Service
@AllArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    public List<CartItem> findAll() {
        return cartItemRepository.findAllByOrderByIdAsc();
    }

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
    }

    public void delete(Long id) {
        CartItem cartItem = findById(id);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }
    }

    public CartItem findByCartIdAndProductId(Long cartId, Long productId) {
        Cart cart = cartService.findById(cartId);
        Product product = productService.findById(productId);
        if (cart != null && product != null) {
            return cartItemRepository.findByCartIdAndProductId(cartId, productId);
        }
        throw new CartItemNotFoundException(cartId, productId);
    }
}

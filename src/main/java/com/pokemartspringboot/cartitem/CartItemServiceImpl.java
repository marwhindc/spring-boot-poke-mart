package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartService cartService, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
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
        return cartItemRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        CartItem cartItem = findById(id);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }
    }

    @Override
    public CartItem findByCartIdAndProductId(Long cartId, Long productId) {
        Cart cart = cartService.findById(cartId);
        Product product = productService.findById(productId);
        if (cart != null && product != null) {
            return cartItemRepository.findByCartIdAndProductId(cartId, productId);
        }
        throw new CartItemNotFoundException(cartId, productId);
    }


}

package com.pokemartspringboot.cartitem;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.pokemartspringboot.cart.Cart;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5000/"})
//@CrossOrigin(origins = "http://localhost:63765/")
@RestController
@RequestMapping("/api/cart-items")
@AllArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems() {
        return ResponseEntity.ok(cartItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartItemService.findById(id));
    }

    @GetMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<CartItem> getCartItemByCartIdAndProductId(
        @PathVariable("cartId") Long cartId,
        @PathVariable("productId") Long productId
    ) {
        return ResponseEntity.ok(cartItemService.findByCartIdAndProductId(cartId, productId));
    }

    @PostMapping
    public ResponseEntity<?> createCartItem(@RequestBody CartItem cartItem) {
        CartItem createdCartItem = cartItemService.save(cartItem);
        return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable("id") Long id, @RequestBody CartItem cartItemData) {
        CartItem existingCartItem = cartItemService.findById(id);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(cartItemData.getQuantity());
            existingCartItem.setProduct(cartItemData.getProduct());
            CartItem updatedCartItem = cartItemService.save(existingCartItem);
            return ResponseEntity.ok(updatedCartItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCartItemById(@PathVariable("id") Long id) {
        cartItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

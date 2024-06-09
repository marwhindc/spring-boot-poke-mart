package com.pokemartspringboot.cart;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5000/"})
//@CrossOrigin(origins = "http://localhost:63765/")
@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getCarts() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id){
        return ResponseEntity.ok(cartService.findById(id));
    }

    //TODO: see if path can be improved
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Cart>> getCartByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.findByUserId(id));
    }

    @GetMapping("/user/{id}/{isCheckedOut}")
    public ResponseEntity<List<Cart>> getCartByUserIdAndCheckedOut(@PathVariable("id") Long id, @PathVariable("isCheckedOut") boolean isCheckedOut) {
        return ResponseEntity.ok(cartService.findByUserIdAndCheckedOut(id, isCheckedOut));
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.save(cart);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable("id") Long id, @RequestBody Cart cartData) {
        Cart existingCart = cartService.findById(id);
        if (existingCart != null) {
            existingCart.setCartItems(cartData.getCartItems());
            existingCart.setCheckedOut(cartData.isCheckedOut());
            Cart updatedCart = cartService.save(existingCart);
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCartById(@PathVariable("id") Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

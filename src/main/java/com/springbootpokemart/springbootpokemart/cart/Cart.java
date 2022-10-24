package com.springbootpokemart.springbootpokemart.cart;

import com.springbootpokemart.springbootpokemart.cartitem.CartItem;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany
    @JoinColumn(name = "cart_id")
    private Collection<CartItem> cartItems = new HashSet<>();

    public Cart() {
    }

    public Cart(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Collection<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Collection<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getSize() {
        return this.cartItems.size();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", cartItems=" + cartItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id.equals(cart.id) && userId.equals(cart.userId) && Objects.equals(cartItems, cart.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, cartItems);
    }
}

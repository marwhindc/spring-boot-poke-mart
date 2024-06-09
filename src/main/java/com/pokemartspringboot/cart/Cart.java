package com.pokemartspringboot.cart;

import com.pokemartspringboot.cartitem.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany
    @JoinColumn(name = "cart_id")
    @Builder.Default
    private Collection<CartItem> cartItems = new HashSet<>();

    @Column(name = "checked_out")
    @Builder.Default
    private boolean checkedOut = false;

    public Integer getTotalQuantity() {
        int total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getQuantity();
        }
        return total;
    }

    public BigDecimal getTotalCartPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems){
            total = total.add(cartItem.getTotalPrice());
        }
        return total;
    }

    public void checkOut() {
        this.checkedOut = true;
    }
}

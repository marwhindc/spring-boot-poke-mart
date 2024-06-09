package com.pokemartspringboot.cart;

import com.pokemartspringboot.cartitem.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @OneToMany
    @JoinColumn(name = "cartId")
    @Builder.Default
    private Collection<CartItem> cartItems = new HashSet<>();

    @Column(name = "checkedOut")
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

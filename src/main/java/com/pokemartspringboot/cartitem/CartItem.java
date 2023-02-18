package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;
    @Column(name = "cart_id")
    private Long cartId;
    private Integer quantity;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public CartItem(Long cartId, Integer quantity, Product product) {
        this.cartId = cartId;
        this.quantity = quantity;
        this.product = product;
    }

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(this.quantity).multiply(this.product.getPrice());
    }
}

package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(this.quantity).multiply(this.product.getPrice());
    }
}

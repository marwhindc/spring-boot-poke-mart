package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.product.Product;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "cartItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cartId")
    private Long cartId;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(this.quantity).multiply(this.product.getPrice());
    }
}

package com.pokemartspringboot.cartitem;

import com.pokemartspringboot.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
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

    public CartItem() {
    }

    public CartItem(Long cartId, Integer quantity, Product product) {
        this.cartId = cartId;
        this.quantity = quantity;
        this.product = product;
    }

    public CartItem(Long id, Long cartId, Integer quantity, Product product) {
        this.id = id;
        this.cartId = cartId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(this.quantity).multiply(this.product.getPrice());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return id.equals(cartItem.id) && cartId.equals(cartItem.cartId) && quantity.equals(cartItem.quantity) && product.equals(cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cartId, quantity, product);
    }
}

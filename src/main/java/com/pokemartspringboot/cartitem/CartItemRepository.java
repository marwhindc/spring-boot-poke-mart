package com.pokemartspringboot.cartitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByOrderByIdAsc();
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}

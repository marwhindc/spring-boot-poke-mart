package com.pokemartspringboot.cartitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByOrderByIdAsc();

    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}

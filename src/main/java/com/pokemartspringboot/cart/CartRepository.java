package com.pokemartspringboot.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

//    List<Cart> findByUserId(Long id);
    Cart findByUserIdAndCheckedOut(Long id, boolean isCheckedOut);
    List<Cart> findByUserIdOrderByIdDesc(Long id);
}

package com.pokemartspringboot.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    //List<Cart> findByUserId(Long id);

    List<Cart> findByUserIdAndCheckedOutOrderByIdDesc(Long id, boolean isCheckedOut);

    List<Cart> findByUserIdOrderByIdDesc(Long id);
}

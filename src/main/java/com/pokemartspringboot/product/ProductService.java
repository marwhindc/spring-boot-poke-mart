package com.pokemartspringboot.product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    void save(Product product);
    Product findById(Long id);
    void delete(Long id);
}

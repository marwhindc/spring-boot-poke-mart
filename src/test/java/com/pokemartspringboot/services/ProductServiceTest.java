package com.pokemartspringboot.services;

import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductNotFoundException;
import com.pokemartspringboot.product.ProductRepository;
import com.pokemartspringboot.product.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;

    @Test
    public void testFindAllProducts() {
        final Long ID = 111L;
        final Long ID2 = 222L;

        Product product = new Product();
        product.setId(ID);
        Product product2 = new Product();
        product2.setId(ID2);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> actualProductList = productService.findAll();

        assertThat(actualProductList, hasSize(2));
        assertThat(actualProductList, hasItem(allOf(
                hasProperty("id", is(ID))
        )));
        assertThat(actualProductList, hasItem(allOf(
                hasProperty("id", is(ID2))
        )));
    }

    @Test
    public void testFindProductById() {
        final Long ID = 111L;

        Product product = new Product();
        product.setId(ID);

        when(productRepository.findById(ID)).thenReturn(Optional.of(product));

        Product actualProduct = productService.findById(ID);

        assertEquals(ID, actualProduct.getId());
    }

    @Test
    public void testFindCartById_productDoesNotExist() {
        final Long ID = 111L;

        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        ProductNotFoundException pnfe = assertThrows(ProductNotFoundException.class, () -> {
            Product actualProduct = productService.findById(ID);
        });

        assertEquals("Unable to find product with id: " + ID, pnfe.getMessage());
    }

    @Test
    public void testSaveProduct() {
        final Long ID = 111L;

        Product product = new Product();
        product.setId(ID);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(new Product());

        assertEquals(ID, savedProduct.getId());
    }

    @Test
    public void testDeleteProduct() {
        final Long ID = 111L;

        Product product = new Product();
        product.setId(ID);

        when(productRepository.findById(ID)).thenReturn(Optional.of(product));

        productService.delete(ID);

        ArgumentCaptor<Product> arg = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).delete(arg.capture());
        Product deletedProduct = arg.getValue();
        assertEquals(ID, deletedProduct.getId());
    }

    @Test
    public void testDeleteProduct_ProductDoesNotExist() {
        final Long ID = 111L;

        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        ProductNotFoundException pnfe = assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(ID);
        });

        assertEquals("Unable to find product with id: " + ID, pnfe.getMessage());
    }
}

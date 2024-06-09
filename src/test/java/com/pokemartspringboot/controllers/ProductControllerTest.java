package com.pokemartspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductController;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.UserDetailsServiceImpl;
import com.pokemartspringboot.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private CartService cartService;
    @MockBean
    private UserService userService;
    @MockBean
    private CartItemService cartItemService;

    private final long ID = 111;

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetProducts() throws Exception {
        final long ID2 = 222;

        Product product = new Product();
        product.setId(ID);
        Product product2 = new Product();
        product2.setId(ID2);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[1].id").value(ID2))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(ID);

        when(productService.findById(ID)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setId(ID);
        product.setName("productName");
        product.setDescription("productDescription");
        product.setImageUrl("productImageURL");
        product.setPrice(BigDecimal.ZERO);

        when(productService.save(product)).thenReturn(product);

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateProduct() throws Exception {
        final String NAME = "newProductName";
        final String DESCRIPTION = "newProductDescription";
        final String IMAGE_URL = "newProductImageURL";
        final BigDecimal PRICE = BigDecimal.valueOf(100);

        Product existingProduct = new Product();
        existingProduct.setId(ID);
        existingProduct.setName("productName");
        existingProduct.setDescription("productDescription");
        existingProduct.setImageUrl("productImageURL");
        existingProduct.setPrice(BigDecimal.ZERO);

        Product updatedProduct = new Product();
        updatedProduct.setId(ID);
        updatedProduct.setName(NAME);
        updatedProduct.setDescription(DESCRIPTION);
        updatedProduct.setImageUrl(IMAGE_URL);
        updatedProduct.setPrice(PRICE);

        when(productService.findById(ID)).thenReturn(existingProduct);
        when(productService.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.imageUrl").value(IMAGE_URL))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateProduct_productNotFound() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(ID);

        when(productService.findById(ID)).thenReturn(null);

        mockMvc.perform(put("/api/products/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", ID))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).delete(ID);
    }
}

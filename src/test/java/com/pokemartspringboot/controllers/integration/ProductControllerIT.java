package com.pokemartspringboot.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductRepository;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//TODO: use pre populated values or empty db?

@SpringBootTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    public void tearDown() throws Exception {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetProducts() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/products"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(6))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetProductById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/products/{id}", 1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Poke Ball"))
                .andExpect(jsonPath("$.description").value("The PokéBall is the standard PokéBall you can obtain. It has a 1x Capture rate that doesn't increase the chances of capturing a Pokémon."))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(200).doubleValue()))
                .andExpect(jsonPath("$.imageUrl").value("https://archives.bulbagarden.net/media/upload/7/79/Dream_Poké_Ball_Sprite.png"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateProduct() throws Exception {
        //given
        Product product = new Product();
        product.setName("productName");
        product.setDescription("productDescription");
        product.setImageUrl("productImageURL");
        product.setPrice(BigDecimal.ZERO);

        //when
        ResultActions response = mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)));

        //then
        response.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateProduct() throws Exception {
        //given
        final String NAME = "newProductName";
        final String DESCRIPTION = "newProductDescription";
        final String IMAGE_URL = "newProductImageURL";
        final BigDecimal PRICE = BigDecimal.valueOf(1000);

        Product updatedProduct = new Product();
        updatedProduct.setName(NAME);
        updatedProduct.setDescription(DESCRIPTION);
        updatedProduct.setImageUrl(IMAGE_URL);
        updatedProduct.setPrice(PRICE);

        //when
        ResultActions response = mockMvc.perform(put("/api/products/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.imageUrl").value(IMAGE_URL))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateProduct_productNotFound() throws Exception {
        //given
        Product updatedProduct = new Product();

        //when
        ResultActions response = mockMvc.perform(put("/api/products/{id}", 111).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Delete is not working: foreign key constraint from cart_items table
//    @Test
//    @WithMockUser(roles = {"USER"})
//    public void testDeleteProduct() throws Exception {
//        mockMvc.perform(delete("/api/products/{id}", 1))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
}

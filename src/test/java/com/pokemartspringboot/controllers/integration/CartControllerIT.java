package com.pokemartspringboot.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.product.ProductRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartService cartService;

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
    public void testGetCarts() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/carts"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].checkedOut").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].userId").value(2))
                .andExpect(jsonPath("$[1].checkedOut").value(false))
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/carts/{id}", 1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.checkedOut").value(false))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartByUserId() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/carts/user/{id}", 1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].checkedOut").value(false))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartByUserIdAndCheckedOut() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/carts/user/{id}/{checkedOut}", 1, false));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].checkedOut").value(false))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateCart() throws Exception {
        final long USER_ID = 1;

        //given
        Cart cart = new Cart();
        cart.setUserId(USER_ID);

        //when
        ResultActions response = mockMvc.perform(post("/api/carts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)));

        //then
        response.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCart() throws Exception {

        //given
        Cart cart = new Cart();
        //TODO: add item to cart
        cart.setCheckedOut(true);

        //when
        ResultActions response = mockMvc.perform(put("/api/carts/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.checkedOut").value(true))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCart_cartNotFound() throws Exception {
        //given
        Cart cart = new Cart();
        cart.setCheckedOut(true);

        //when
        ResultActions response = mockMvc.perform(put("/api/carts/{id}", 111).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteCart() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/carts/{id}", 1));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}

package com.pokemartspringboot.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class CartItemControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartItemService cartItemService;

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
    public void testGetCartItems() throws Exception{
        ResultActions response = mockMvc.perform(get("/api/cart-items"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartItemById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/cart-items/{id}", 1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.product.id").value(1))
                .andExpect(jsonPath("$.quantity").value(5))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartItemByByCartIdAndProductId() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/cart-items/cart/{cartId}/product/{productId}", 1,1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.product.id").value(1))
                .andExpect(jsonPath("$.quantity").value(5))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateCartItem() throws Exception {
        //given
        Product product = productRepository.findById(1L).get();

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setCartId(1L);

        //when
        ResultActions response =  mockMvc.perform(post("/api/cart-items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)));

        //then
        response.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCartItem() throws Exception {
        //given
        Product product = productRepository.findById(1L).get();

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        cartItem.setProduct(product);

        //when
        ResultActions response = mockMvc.perform(put("/api/cart-items/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)));

        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCartItem_cartItemNotFound() throws Exception {
        //given
        Product product = productRepository.findById(1L).get();

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        cartItem.setProduct(product);

        //when
        ResultActions response = mockMvc.perform(put("/api/cart-items/{id}", 111).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteCart() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/cart-items/{id}", 1));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}

package com.pokemartspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartController;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItemService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false) //source: https://stackoverflow.com/questions/21749781/why-i-received-an-error-403-with-mockmvc-and-junit
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;
    @MockBean
    private CartItemService cartItemService;

    private final long ID = 111;
    private final long USER_ID = 222;

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCarts() throws Exception {
        final long ID2 = 222;

        Cart cart = new Cart();
        cart.setId(ID);
        Cart cart2 = new Cart();
        cart2.setId(ID2);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        cartList.add(cart2);

        when(cartService.findAll()).thenReturn(cartList);

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[1].id").value(ID2))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartById() throws Exception {
        Cart cart = new Cart();
        cart.setId(ID);

        when(cartService.findById(ID)).thenReturn(cart);

        mockMvc.perform(get("/api/carts/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartByUserId() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(USER_ID);

        when(cartService.findByUserId(USER_ID)).thenReturn(List.of(cart));

        mockMvc.perform(get("/api/carts/user/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(USER_ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartByUserIdAndCheckedOut() throws Exception {
        final boolean isCheckedOut = true;

        Cart cart = new Cart();
        cart.setUserId(USER_ID);
        cart.setCheckedOut(isCheckedOut);

        when(cartService.findByUserIdAndCheckedOut(USER_ID, isCheckedOut)).thenReturn(List.of(cart));

        mockMvc.perform(get("/api/carts/user/{id}/{checkedOut}", USER_ID, isCheckedOut))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(USER_ID))
                .andExpect(jsonPath("$[0].checkedOut").value(isCheckedOut))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateCart() throws Exception {
        Cart cart = new Cart();
        cart.setId(ID);
        cart.setUserId(USER_ID);

        when(cartService.save(cart)).thenReturn(cart);

        mockMvc.perform(post("/api/carts").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCart() throws Exception {
        Cart existingCart = new Cart();
        existingCart.setId(ID);
        existingCart.setCheckedOut(false);

        Cart updatedCart = new Cart();
        updatedCart.setId(ID);
        updatedCart.setCheckedOut(true);

        when(cartService.findById(ID)).thenReturn(existingCart);
        when(cartService.save(any(Cart.class))).thenReturn(updatedCart);

        mockMvc.perform(put("/api/carts/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.checkedOut").value(true))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCart_cartNotFound() throws Exception {
        Cart updatedCart = new Cart();
        updatedCart.setId(ID);

        when(cartService.findById(ID)).thenReturn(null);

        mockMvc.perform(put("/api/carts/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCart)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteCart() throws Exception {
        mockMvc.perform(delete("/api/carts/{id}", ID))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(cartService, times(1)).delete(ID);
    }
}

package com.pokemartspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemController;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.UserDetailsServiceImpl;
import com.pokemartspringboot.user.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private CartService cartService;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;

    private final long ID = 111;
    private final long CART_ID = 222;
    private final long PRODUCT_ID = 333;
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId(PRODUCT_ID);
        product.setName("productName");
        product.setDescription("productDescription");
        product.setImageUrl("productImageURL");
        product.setPrice(BigDecimal.ZERO);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartItems() throws Exception {
        final int QUANTITY = 1;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);
        cartItem.setQuantity(QUANTITY);
        cartItem.setProduct(product);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(cartItemService.findAll()).thenReturn(cartItemList);

        mockMvc.perform(get("/api/cart-items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(QUANTITY))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].quantity").value(1))
                .andExpect(jsonPath("$[0].product.id").value(PRODUCT_ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartItemById() throws Exception {
        final int QUANTITY = 1;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);
        cartItem.setQuantity(QUANTITY);
        cartItem.setProduct(product);

        when(cartItemService.findById(ID)).thenReturn(cartItem);

        mockMvc.perform(get("/api/cart-items/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.quantity").value(QUANTITY))
                .andExpect(jsonPath("$.product.id").value(PRODUCT_ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCartItemByCartIdAndProductId() throws Exception {
        final int QUANTITY = 1;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);
        cartItem.setQuantity(QUANTITY);
        cartItem.setCartId(CART_ID);
        cartItem.setProduct(product);

        when(cartItemService.findByCartIdAndProductId(CART_ID, PRODUCT_ID)).thenReturn(cartItem);

        mockMvc.perform(get("/api/cart-items/cart/{cartId}/product/{productId}", CART_ID, PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.quantity").value(QUANTITY))

                .andExpect(jsonPath("$.cartId").value(CART_ID))
                .andExpect(jsonPath("$.product.id").value(PRODUCT_ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateCartItem() throws Exception {
        final int QUANTITY = 1;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);
        cartItem.setCartId(CART_ID);
        cartItem.setQuantity(QUANTITY);
        cartItem.setProduct(product);

        when(cartItemService.save(cartItem)).thenReturn(cartItem);

        mockMvc.perform(post("/api/cart-items").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCartItem() throws Exception {
        final int QUANTITY = 2;

        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(ID);
        existingCartItem.setCartId(CART_ID);
        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(1);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(ID);
        updatedCartItem.setCartId(CART_ID);
        updatedCartItem.setProduct(product);
        updatedCartItem.setQuantity(QUANTITY);

        when(cartItemService.findById(ID)).thenReturn(existingCartItem);
        when(cartItemService.save(any(CartItem.class))).thenReturn(updatedCartItem);

        mockMvc.perform(put("/api/cart-items/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCartItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cartId").value(CART_ID))
                .andExpect(jsonPath("$.product.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.quantity").value(QUANTITY))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateCartItem_cartItemNotFound() throws Exception {
        final int QUANTITY = 1;

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(ID);
        updatedCartItem.setQuantity(QUANTITY);
        updatedCartItem.setProduct(product);

        when(cartItemService.findById(ID)).thenReturn(null);

        mockMvc.perform(put("/api/cart-items/{id}", ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCartItem)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/api/cart-items/{id}", ID))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(cartItemService, times(1)).delete(ID);
    }
}

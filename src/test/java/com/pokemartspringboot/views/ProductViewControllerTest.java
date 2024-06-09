package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserDetailsServiceImpl;
import com.pokemartspringboot.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {

    @MockBean
    private ProductService productService;
    @MockBean
    private CartService cartService;
    @MockBean
    private UserService userService;
    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private Cart cart;
    private Product product;
    private Product product2;
    private CartItem cartItem;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1L)
                .username("aketchum")
                .firstName("Ash")
                .lastName("Ketchum")
                .build();
        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .build();
        product = new Product(1L, "Poke Ball", "Poke Ball", new BigDecimal(200), "http://test.com");
        product2 = new Product(2L, "Great Ball", "Great Ball", new BigDecimal(600), "http://test.com");
        cartItem = new CartItem(1L, 1L, 1, product);
        cart.getCartItems().add(cartItem);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void viewHomePage() throws Exception {

        given(cartService.findByUserIdAndCheckedOut(user.getId(), false)).willReturn(List.of(cart));

        given(productService.findAll()).willReturn(List.of(product, product2));

        mockMvc.perform(get("/products").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("product-page"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute("products", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("Poke Ball")),
                                hasProperty("description", is("Poke Ball")),
                                hasProperty("price", is(BigDecimal.valueOf(200))),
                                hasProperty("imageUrl", is("http://test.com"))
                        )
                )))
                .andExpect(model().attribute("products", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("name", is("Great Ball")),
                                hasProperty("description", is("Great Ball")),
                                hasProperty("price", is(BigDecimal.valueOf(600))),
                                hasProperty("imageUrl", is("http://test.com"))
                        )
                )))
                .andExpect(model().attribute("cart", hasProperty("id", is(1L))))
                .andExpect(model().attribute("cart", hasProperty("userId", is(1L))))
                .andExpect(model().attribute("cart", hasProperty("cartItems", is(cart.getCartItems()))))
                .andExpect(model().attribute("cart", hasProperty("checkedOut", is(false))));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addToCart_invoke_save_and_add_quantity() throws Exception {

        given(cartService.findByUserIdAndCheckedOut(user.getId(), false)).willReturn(List.of(cart));
        given(cartItemService.findByCartIdAndProductId(cart.getId(), 1L)).willReturn(cartItem);

        mockMvc.perform(get("/products/addToCart/{id}", 1L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));
        then(cartItemService).should().save(cartItem);
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addToCart_invoke_save_for_new_item() throws Exception {

        CartItem newCartItem = new CartItem(2L, 1L, 1, product2);

        given(cartService.findByUserIdAndCheckedOut(user.getId(), false)).willReturn((List.of(cart)));

        given(productService.findById(2L)).willReturn(product2);

        mockMvc.perform(get("/products/addToCart/{id}", 2L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));

        doReturn(newCartItem).when(cartItemService).save(newCartItem);
        CartItem savedCartItem = cartItemService.save(newCartItem);

        assertEquals(newCartItem, savedCartItem);
    }
}
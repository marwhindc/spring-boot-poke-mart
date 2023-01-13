package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.UserDetailsServiceImpl;
import com.pokemartspringboot.user.User;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartViewController.class)
class CartViewControllerTest {

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
    private Cart checkedOutCart;
    private Product product;
    private Product product2;
    private CartItem cartItem;
    private CartItem cartItem2;

    @BeforeEach
    public void init() {
        user = new User(1L, "aketchum", "Ash", "Ketchum");
        cart = new Cart(1L, user.getId());
        product = new Product(1L, "Poke Ball", "Poke Ball", new BigDecimal(200), "http://test.com");
        product2 = new Product(2L, "Great Ball", "Great Ball", new BigDecimal(600), "http://test.com");
        cartItem = new CartItem(1L, 1L, 1, product);
        cartItem2 = new CartItem(2L, 2L, 1, product2);
        cart.getCartItems().add(cartItem);

        checkedOutCart = new Cart(2L, user.getId());
        checkedOutCart.getCartItems().add(cartItem2);
        checkedOutCart.checkOut();

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void viewCartPage() throws Exception {
        given(cartService.findByUserIdAndCheckedOut(user.getId(), false)).willReturn(List.of(cart));

        mockMvc.perform(get("/carts").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("cart-page"))
                .andExpect(model().attribute("cart", hasProperty("id", is(1L))))
                .andExpect(model().attribute("cart", hasProperty("userId", is(1L))))
                .andExpect(model().attribute("cart", hasProperty("cartItems", is(cart.getCartItems()))))
                .andExpect(model().attribute("cart", hasProperty("checkedOut", is(false))));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void viewPurchasePage() throws Exception {
        given(cartService.findByUserIdAndCheckedOut(user.getId(), false)).willReturn(List.of(cart));

        given(cartService.findByUserIdAndCheckedOut(user.getId(), true)).willReturn(List.of(checkedOutCart));

        mockMvc.perform(get("/carts/purchase").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("purchase-page"))
                .andExpect(model().attribute("carts", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("userId", is(user.getId())),
                                hasProperty("cartItems", is(checkedOutCart.getCartItems())),
                                hasProperty("checkedOut", is(true))
                        )
                )))
                .andExpect(model().attribute("activeCart", hasProperty("id", is(1L))))
                .andExpect(model().attribute("activeCart", hasProperty("userId", is(1L))))
                .andExpect(model().attribute("activeCart", hasProperty("cartItems", is(cart.getCartItems()))))
                .andExpect(model().attribute("activeCart", hasProperty("checkedOut", is(false))));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void checkOut() throws Exception {
        Cart newCart = new Cart(user.getId());

        given(cartService.findById(1L)).willReturn(cart);

        mockMvc.perform(get("/carts/checkOut/{id}", 1L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/carts"));

        then(cartService).should().save(cart);
        doReturn(newCart).when(cartService).save(newCart);
        Cart savedNewCart = cartService.save(newCart);

        assertTrue(cart.isCheckedOut());
        assertEquals(newCart, savedNewCart);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void add_item_quantity() throws Exception {

        given(cartItemService.findById(1L)).willReturn(cartItem);

        mockMvc.perform(get("/carts/add/{id}", 1L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/carts"));

        then(cartItemService).should().save(cartItem);

        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void subtract_item_quantity_without_delete() throws Exception {
        cartItem.setQuantity(2);
        given(cartItemService.findById(1L)).willReturn(cartItem);

        mockMvc.perform(get("/carts/subtract/{id}", 1L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/carts"));

        then(cartItemService).should().save(cartItem);

        assertEquals(1, cartItem.getQuantity());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void subtract_item_quantity_with_delete() throws Exception {

        given(cartItemService.findById(1L)).willReturn(cartItem);

        mockMvc.perform(get("/carts/subtract/{id}", 1L).flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/carts"));

        then(cartItemService).should().delete(1L);
    }


}
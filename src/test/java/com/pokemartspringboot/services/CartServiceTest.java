package com.pokemartspringboot.services;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartNotFoundException;
import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cart.CartServiceImpl;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserService userService;

    @Test
    public void testFindAllCarts() {
        final Long ID = 111L;
        final Long ID2 = 222L;

        Cart cart = new Cart();
        cart.setId(ID);
        Cart cart2 = new Cart();
        cart2.setId(ID2);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        cartList.add(cart2);

        when(cartRepository.findAll()).thenReturn(cartList);

        List<Cart> actualCartList = cartService.findAll();

        assertThat(actualCartList, hasSize(2));
        assertThat(actualCartList, hasItem(allOf(
                hasProperty("id", is(ID))
        )));
        assertThat(actualCartList, hasItem(allOf(
                hasProperty("id", is(ID2))
        )));
    }

    @Test
    public void testFindCartById() {
        final Long ID = 111L;

        Cart cart = new Cart();
        cart.setId(ID);

        when(cartRepository.findById(ID)).thenReturn(Optional.of(cart));

        Cart actualCart = cartService.findById(ID);

        assertEquals(ID, actualCart.getId());
    }

    @Test
    public void testFindCartById_cartDoesNotExist() {
        final Long ID = 111L;

        when(cartRepository.findById(ID)).thenReturn(Optional.empty());

        CartNotFoundException cnfe = assertThrows(CartNotFoundException.class, () -> {
            Cart actualCart = cartService.findById(ID);
        });

        assertEquals("Unable to find cart with id: " + ID, cnfe.getMessage());
    }

    @Test
    public void testFindCartByUserId() {
        final Long ID = 111L;
        final Long USER_ID = 222L;

        User user = new User();
        user.setId(USER_ID);

        Cart cart = new Cart();
        cart.setId(ID);
        cart.setUserId(USER_ID);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        when(userService.findById(USER_ID)).thenReturn(user);
        when(cartRepository.findByUserIdOrderByIdDesc(USER_ID)).thenReturn(cartList);

        List<Cart> actualCartList = cartService.findByUserId(USER_ID);

        assertThat(actualCartList, hasSize(1));
        assertThat(actualCartList, hasItem(allOf(
                hasProperty("id", is(ID)),
                hasProperty("userId", is(USER_ID))
        )));
    }

    @Test
    public void testFindCartByUserId_userIsNull() {
        final Long USER_ID = 222L;

        when(userService.findById(USER_ID)).thenReturn(null);

        List<Cart> actualCartList = cartService.findByUserId(USER_ID);

        assertTrue(actualCartList.isEmpty());
    }

    @Test
    public void testFindCartByUserIdAndCheckedOut() {
        final Long ID = 111L;
        final Long USER_ID = 222L;
        final boolean isCheckedOut = true;

        User user = new User();
        user.setId(USER_ID);

        Cart cart = new Cart();
        cart.setId(ID);
        cart.setUserId(USER_ID);
        cart.setCheckedOut(isCheckedOut);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        when(userService.findById(USER_ID)).thenReturn(user);
        when(cartRepository.findByUserIdAndCheckedOutOrderByIdDesc(USER_ID, isCheckedOut)).thenReturn(cartList);

        List<Cart> actualCartList = cartService.findByUserIdAndCheckedOut(USER_ID, isCheckedOut);

        assertThat(actualCartList, hasSize(1));
        assertThat(actualCartList, hasItem(allOf(
                hasProperty("id", is(ID)),
                hasProperty("userId", is(USER_ID)),
                hasProperty("checkedOut", is(isCheckedOut))
        )));
    }

    @Test
    public void testFindCartByUserIdAndIsCheckedOut_userIsNull() {
        final Long USER_ID = 222L;
        final boolean isCheckedOut = true;

        when(userService.findById(USER_ID)).thenReturn(null);

        List<Cart> actualCartList = cartService.findByUserIdAndCheckedOut(USER_ID, isCheckedOut);

        assertTrue(actualCartList.isEmpty());
    }

    @Test
    public void testSaveCart() {
        final Long ID = 111L;

        Cart cart = new Cart();
        cart.setId(ID);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart savedCart = cartService.save(new Cart());

        assertEquals(ID, savedCart.getId());
    }

    @Test
    public void testDeleteCart() {
        final Long ID = 111L;

        Cart cart = new Cart();
        cart.setId(ID);

        when(cartRepository.findById(ID)).thenReturn(Optional.of(cart));

        cartService.delete(ID);

        ArgumentCaptor<Cart> arg = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(1)).delete(arg.capture());
        Cart deletedCart = arg.getValue();
        assertEquals(ID, deletedCart.getId());
    }

    @Test
    public void testDeleteCart_cartDoesNotExist() {
        final Long ID = 111L;

        when(cartRepository.findById(ID)).thenReturn(Optional.empty());

        CartNotFoundException cnfe = assertThrows(CartNotFoundException.class, () -> {
            cartService.delete(ID);
        });

        assertEquals("Unable to find cart with id: " + ID, cnfe.getMessage());
    }
}

package com.pokemartspringboot.services;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemNotFoundException;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.Product;
import com.pokemartspringboot.product.ProductService;
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
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;

    @Test
    public void testFindAllCartItems() {
        final Long ID = 111L;
        final Long ID2 = 222L;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);
        CartItem cartItem2 = new CartItem();
        cartItem2.setId(ID2);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cartItemList.add(cartItem2);

        when(cartItemRepository.findAllByOrderByIdAsc()).thenReturn(cartItemList);

        List<CartItem> actualCartItemList = cartItemService.findAll();

        assertThat(actualCartItemList, hasSize(2));
        assertThat(actualCartItemList, hasItem(allOf(
                hasProperty("id", is(ID))
        )));
        assertThat(actualCartItemList, hasItem(allOf(
                hasProperty("id", is(ID2))
        )));
    }

    @Test
    public void testFindCartItemById() {
        final Long ID = 111L;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);

        when(cartItemRepository.findById(ID)).thenReturn(Optional.of(cartItem));

        CartItem actualCartItem = cartItemService.findById(ID);

        assertEquals(ID, actualCartItem.getId());
    }

    @Test
    public void testFindCartItemById_cartItemDoesNotExist() {
        final Long ID = 111L;

        when(cartItemRepository.findById(ID)).thenReturn(Optional.empty());

        CartItemNotFoundException cinfe = assertThrows(CartItemNotFoundException.class, () -> {
            CartItem actualCartItem = cartItemService.findById(ID);
        });

        assertEquals("Unable to find cart item with id: " + ID, cinfe.getMessage());
    }

    @Test
    public void testFindCartItemByCartIdAndProductId() {
        final Long PRODUCT_ID = 111L;
        final Long CART_ID = 222L;

        Product product = new Product();
        product.setId(PRODUCT_ID);

        Cart cart = new Cart();
        cart.setId(CART_ID);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCartId(CART_ID);

        when(cartService.findById(CART_ID)).thenReturn(cart);
        when(productService.findById(PRODUCT_ID)).thenReturn(product);
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID)).thenReturn(cartItem);

        CartItem actualCartItem = cartItemService.findByCartIdAndProductId(CART_ID, PRODUCT_ID);

        assertEquals(CART_ID, actualCartItem.getCartId());
        assertEquals(PRODUCT_ID, actualCartItem.getProduct().getId());
    }

    @Test
    public void testFindCartItemByCartIdAndProductId_cartAndProductDoesNotExist() {
        final Long PRODUCT_ID = 111L;
        final Long CART_ID = 222L;

        when(cartService.findById(CART_ID)).thenReturn(null);
        when(productService.findById(PRODUCT_ID)).thenReturn(null);

        CartItemNotFoundException cinfe = assertThrows(CartItemNotFoundException.class, () -> {
            CartItem actualCartItem = cartItemService.findByCartIdAndProductId(CART_ID, PRODUCT_ID);
        });

        assertEquals("Unable to find cart item with cart id: " + CART_ID + " and product id: " + PRODUCT_ID, cinfe.getMessage());
    }

    @Test
    public void testSaveCartItem() {
        final Long ID = 111L;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem savedCartItem = cartItemService.save(new CartItem());

        assertEquals(ID, savedCartItem.getId());
    }

    @Test
    public void testDeleteCartItem() {
        final Long ID = 111L;

        CartItem cartItem = new CartItem();
        cartItem.setId(ID);

        when(cartItemRepository.findById(ID)).thenReturn(Optional.of(cartItem));

        cartItemService.delete(ID);

        ArgumentCaptor<CartItem> arg = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository, times(1)).delete(arg.capture());
        CartItem deletedCartItem = arg.getValue();
        assertEquals(ID, deletedCartItem.getId());
    }

    @Test
    public void testDeleteCartItem_cartItemDoesNotExist() {
        final Long ID = 111L;

        when(cartItemRepository.findById(ID)).thenReturn(Optional.empty());

        CartItemNotFoundException cinfe = assertThrows(CartItemNotFoundException.class, () -> {
            cartItemService.delete(ID);
        });

        assertEquals("Unable to find cart item with id: " + ID, cinfe.getMessage());
    }
}

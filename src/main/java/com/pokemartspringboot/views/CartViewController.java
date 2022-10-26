package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carts")
public class CartViewController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public String viewCartPage(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("carts", cartService.findByUserId(user.getId()));
        return "cart-page";
    }

    @GetMapping("{userId}/checkOut/{id}")
    public String addToCart(@PathVariable("userId") Long userId, @PathVariable(value = "id") Long cartId) {
        User user = userService.findById(userId);
        Cart cart = cartService.findById(cartId);
        cart.checkOut();
        cartService.save(cart);
        Cart newCart = new Cart(user.getId());
        cartService.save(newCart);
        return "redirect:/carts?id=" + userId;
    }

    @GetMapping("{userId}/plus/{id}")
    public String addItemQuantity(@PathVariable("userId") Long userId, @PathVariable(value = "id") Long cartItemId) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQuantity(cartItem.getQuantity()+1);
        cartItemService.save(cartItem);
        return "redirect:/carts?id=" + userId;
    }

    @GetMapping("{userId}/minus/{id}")
    public String subtractItemQuantity(@PathVariable("userId") Long userId, @PathVariable(value = "id") Long cartItemId) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQuantity(cartItem.getQuantity()-1);
        if (cartItem.getQuantity() == 0) {
            cartItemService.delete(cartItemId);
            return "redirect:/carts?id=" + userId;
        }
        cartItemService.save(cartItem);
        return "redirect:/carts?id=" + userId;
    }
}

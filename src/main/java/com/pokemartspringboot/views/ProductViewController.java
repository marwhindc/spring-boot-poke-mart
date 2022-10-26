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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public String viewProductPage(@RequestParam(value = "id") Long id, Model model) {
        User user = userService.findById(id);
        Cart cart = cartService.findByUserIdAndCheckedOut(user.getId(),false);
        model.addAttribute("user", user);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart", cart);
        return "product-page";
    }

    @GetMapping("{userId}/addToCart/{id}")
    public String addToCart(@PathVariable("userId") Long userId, @PathVariable(value = "id") Long productId) {
        User user = userService.findById(userId);
        Cart cart = cartService.findByUserIdAndCheckedOut(user.getId(),false);
        Collection<CartItem> cartItems = cart.getCartItems();
        for(CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(productId)) {
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartItemService.save(cartItem);
                return "redirect:/products?id=" + userId;
            }
        }
        CartItem newCartItem = new CartItem(cart.getId(), 1, productService.findById(productId));
        cartItemService.save(newCartItem);
        return "redirect:/products?id=" + userId;
    }

}

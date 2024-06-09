package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartViewController {

    private CartService cartService;
    private CartItemService cartItemService;

    @GetMapping
    public String viewCartPage(Model model) {
        User user = (User)model.getAttribute("user");
        Cart activeCart = cartService.findByUserIdAndCheckedOut(user.getId(),false).get(0);
        model.addAttribute("cart", activeCart);
        return "cart-page";
    }

    @GetMapping("/checkOut/{id}")
    public String checkOut(@PathVariable("id") Long id, Model model) {
        User user = (User)model.getAttribute("user");
        Cart cart = cartService.findById(id);
        cart.checkOut();
        cartService.save(cart);
        Cart newCart = Cart.builder()
                .userId(user.getId())
                .build();
        cartService.save(newCart);
        return "redirect:/carts";
    }

    @GetMapping("/add/{id}")
    public String addItemQuantity(@PathVariable("id") Long id) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(cartItem.getQuantity()+1);
        cartItemService.save(cartItem);
        return "redirect:/carts";
    }

    @GetMapping("/subtract/{id}")
    public String subtractItemQuantity(@PathVariable("id") Long id) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(cartItem.getQuantity()-1);
        if (cartItem.getQuantity() == 0) {
            cartItemService.delete(id);
            return "redirect:/carts";
        }
        cartItemService.save(cartItem);
        return "redirect:/carts";
    }

    @GetMapping("/purchase")
    public String viewPurchasePage(Model model) {
        User user = (User)model.getAttribute("user");
        model.addAttribute("activeCart", cartService.findByUserIdAndCheckedOut(user.getId(),false).get(0));
        model.addAttribute("carts", cartService.findByUserIdAndCheckedOut(user.getId(),true));
        return "purchase-page";
    }
}

package com.springbootpokemart.springbootpokemart.product;

import com.springbootpokemart.springbootpokemart.cart.Cart;
import com.springbootpokemart.springbootpokemart.cart.CartService;
import com.springbootpokemart.springbootpokemart.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product-page")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewProductPage(@ModelAttribute("user") User user, Model model) {
        Cart cart = cartService.findByUserId(user.getId()).get(0); //gets first cart only
        model.addAttribute("products", productService.findAll());
        model.addAttribute("carts", cart);
        return "product-page";
    }

}

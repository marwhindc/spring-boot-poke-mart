package com.springbootpokemart.springbootpokemart;

import com.springbootpokemart.springbootpokemart.user.User;
import com.springbootpokemart.springbootpokemart.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "index";
    }

    @PostMapping
    public String login(@RequestParam("user") Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id);
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/product-page";
    }

}

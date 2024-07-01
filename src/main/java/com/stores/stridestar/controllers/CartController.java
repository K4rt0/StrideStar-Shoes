package com.stores.stridestar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @GetMapping
    public String showCart() {
        return "/main-site/cart/index";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/main-site/cart/checkout";
    }

    @GetMapping("/checkout/completed")
    public String checkoutCompleted() {
        return "/main-site/cart/completed";
    }
}

package com.stores.stridestar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @GetMapping
    public String shop() {
        return "main-site/shop/index";
    }
    @GetMapping("/product-detail/{id}")
    public String productDetail() {
        return "main-site/shop/product-detail";
    }
}

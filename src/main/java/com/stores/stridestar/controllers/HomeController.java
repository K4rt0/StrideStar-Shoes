package com.stores.stridestar.controllers;

import com.stores.stridestar.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String home(Model model) {
        var products =  productService.getAllProducts();
        model.addAttribute("products",products.stream()
                                                .limit(8)
                                                .collect(Collectors.toList()));

        var bestProducts = products.stream().limit(6).collect(Collectors.toList());
        model.addAttribute("bestProducts", bestProducts);

        return "main-site/home/index";
    }
}

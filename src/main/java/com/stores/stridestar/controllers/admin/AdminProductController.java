package com.stores.stridestar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @GetMapping
    public String productList() {
        return "admin/products/product-list";
    }
    @GetMapping("/create")
    public String productCreate() {
        return "admin/products/product-create";
    }
    @GetMapping("/edit/{id}")
    public String productEdit(@PathVariable("id") Long id) {
        return "admin/products/product-edit";
    }
}

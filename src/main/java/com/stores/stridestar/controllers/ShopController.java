package com.stores.stridestar.controllers;


import com.stores.stridestar.models.Category;
import com.stores.stridestar.models.Product;
import com.stores.stridestar.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String shop(Model model) {
        var products =  productService.getAllProducts();
        model.addAttribute("products", products);
        return "main-site/shop/index";
    }
    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable("id") Long id,Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);

        var products =  productService.getAllProducts();
        List<Product> relatedProducts = products.stream()
                .filter(p -> product.getCategory().equals(p.getCategory()) && !p.getId().equals(product.getId()))
                .limit(6).collect(Collectors.toList());
        model.addAttribute("relatedProducts", relatedProducts);

        var firstProductVariant = product.getVariants().stream().findFirst();
        model.addAttribute("firstProductVariant", firstProductVariant);

        return "main-site/shop/product-detail";
    }
}

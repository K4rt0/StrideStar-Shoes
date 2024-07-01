package com.stores.stridestar.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stores.stridestar.models.Product;
import com.stores.stridestar.services.ProductService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class HomeApiController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Map<String, List<Product>> getAllProducts() {
        Map<String, List<Product>> response = new HashMap<>();

        // Featured Products
        List<Product> productFeatured = productService.getAllProducts().stream()
            .sorted(Comparator.comparing(Product::getId).reversed())
            .limit(6)
            .collect(Collectors.toList());

        // Best Seller
        List<Product> productBestSeller = productService.getAllProducts().stream()
            .sorted(Comparator.comparing(Product::getId).reversed())
            .limit(6)
            .collect(Collectors.toList());

        response.put("featuredProducts", productFeatured);
        response.put("bestSellerProducts", productBestSeller);

        return response;
    }
}
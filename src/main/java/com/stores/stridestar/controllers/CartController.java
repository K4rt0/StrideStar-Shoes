package com.stores.stridestar.controllers;

import com.stores.stridestar.models.Product;
import com.stores.stridestar.services.CartService;
import com.stores.stridestar.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    private ProductService productService;

    @GetMapping
    public String showCart(Model model) {
        var cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);

        return "/cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long userId,@RequestParam Long productId,@RequestParam Long variantId, @RequestParam int quantity) {
        Product product = productService.getProductById(productId).orElseThrow();
        cartService.addToCart(userId,product,variantId, quantity);
        return "redirect:/cart";
    }
    /*@GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }*/

}

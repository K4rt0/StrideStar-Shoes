package com.stores.stridestar.controllers;

import com.stores.stridestar.models.Category;
import com.stores.stridestar.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String home() {
        return "main-site/home/index";
    }
    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.getAllCategories();
    }
}

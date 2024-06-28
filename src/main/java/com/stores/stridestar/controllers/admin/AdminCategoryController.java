package com.stores.stridestar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @GetMapping
    public String categoryList() {
        return "admin/categories/category-list";
    }

    @GetMapping("/create")
    public String categoryCreateForm() {
        return "admin/categories/category-create";
    }
    @GetMapping("/edit/{id}")
    public String categoryEditForm(@PathVariable("id") Long id) {
        return "admin/categories/category-edit";
    }
}
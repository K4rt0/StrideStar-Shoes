package com.stores.stridestar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @GetMapping
    public String categoryList(Model model) {
        return "admin/categories/category-list";
    }

    @GetMapping("/create")
    public String categoryCreateForm() {
        return "admin/categories/category-create";
    }

    @GetMapping("/edit/{id}")
    public String categoryEditForm() {
        return "admin/categories/category-edit";
    }
}
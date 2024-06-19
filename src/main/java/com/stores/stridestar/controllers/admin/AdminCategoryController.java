package com.stores.stridestar.controllers.admin;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.stores.stridestar.extensions.CommonFunction;
import com.stores.stridestar.models.Category;

import jakarta.validation.Valid;

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

    @PostMapping("/create")
    public String categoryCreate(@Valid Category category, BindingResult result,
            @RequestParam("avatar") MultipartFile avatar) throws IOException {
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String categoryEdit(@PathVariable("id") Long id, Model model) {
        return "admin/categories/category-edit";
    }
}
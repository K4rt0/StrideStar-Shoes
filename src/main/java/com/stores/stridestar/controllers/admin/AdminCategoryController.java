package com.stores.stridestar.controllers.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.stores.stridestar.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String categoryList(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories/category-list";
    }

    @GetMapping("/create")
    public String categoryCreateForm() {
        return "admin/categories/category-create";
    }

    @PostMapping("/create")
    public String categoryCreate(@Valid Category category, BindingResult result,
            @RequestParam("avatar") MultipartFile avatar) throws IOException {
        
        String seoUrl = CommonFunction.SEOUrl(category.getName());
        category.setAvatar(CommonFunction.saveFile(seoUrl, "/categories", avatar));
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String categoryEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/categories/category-edit";
    }
    @PostMapping("/edit/{id}")
    public String categoryEdit(@PathVariable("id") Long id, @Valid Category categoryDetails, BindingResult result, Model model,
        @RequestParam("avatar") MultipartFile avatar) throws IOException {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        
        category.setName(categoryDetails.getName());
        category.setDisplay(categoryDetails.isDisplay());

        if(!avatar.isEmpty()) {
            String seoUrl = CommonFunction.SEOUrl(categoryDetails.getName());
            category.setAvatar(CommonFunction.saveFile(seoUrl, "/categories", avatar));
        }
        categoryService.addCategory(category);

        return "redirect:/admin/categories";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        categoryService.deleteCategoryById(id);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/admin/categories";
    }
}
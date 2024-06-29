package com.stores.stridestar.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stores.stridestar.extensions.CommonFunction;
import com.stores.stridestar.models.Category;
import com.stores.stridestar.models.Product;
import com.stores.stridestar.services.AmazonS3Service;
import com.stores.stridestar.services.CategoryService;
import com.stores.stridestar.services.ProductService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AmazonS3Service amazonS3Service;
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.getAllCategories();

        if(categories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@Valid @RequestPart("category") Category category,
                                                   @RequestParam("avatar") MultipartFile avatar) {
        if (!avatar.isEmpty()) {
            String seoUrl = CommonFunction.SEOUrl(category.getName());
            category.setAvatar(amazonS3Service.uploadFile(avatar, seoUrl));
        }

        categoryService.addCategory(category);
        return new ResponseEntity<Category>(category, HttpStatus.CREATED);
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<Category> getCategoryById(@ModelAttribute("id") Long id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        if(category == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id,
                                                   @Valid @RequestPart("category") Category category,
                                                   @RequestPart(value = "avatar") MultipartFile avatar) {
        Category existingCategory = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));

        existingCategory.setName(category.getName());
        existingCategory.setDisplay(category.isDisplay());
        
        if (!avatar.isEmpty()) {
            amazonS3Service.deleteFile(amazonS3Service.getFileName(category.getAvatar()));
            String seoUrl = CommonFunction.SEOUrl(category.getName());
            existingCategory.setAvatar(amazonS3Service.uploadFile(avatar, seoUrl));
        }

        categoryService.updateCategory(existingCategory);
        return new ResponseEntity<Category>(existingCategory, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        if(category == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        List<Product> products = productService.getAllByCategoryId(id);
        products.forEach(product -> {
            productService.deleteById(product.getId());
        });
        categoryService.deleteCategoryById(id);
        amazonS3Service.deleteFile(amazonS3Service.getFileName(category.getAvatar()));
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }
}
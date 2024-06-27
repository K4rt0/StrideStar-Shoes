package com.stores.stridestar.controllers.admin;

import com.stores.stridestar.extensions.CommonFunction;
import com.stores.stridestar.models.Blog;
import com.stores.stridestar.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/blogs")
public class AdminBlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public String blogList(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        return "admin/blogs/blog-list";
    }

    @GetMapping("/create")
    public String blogCreateForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "admin/blogs/blog-create";
    }

    @PostMapping("/create")
    public String createBlog(@ModelAttribute Blog blog, @RequestParam("avatar") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Lưu ảnh vào thư mục static/images/blog
                String seoUrl = CommonFunction.SEOUrl(blog.getTitle());
                blog.setAvatarUrl(CommonFunction.saveFile(seoUrl, "/blogs", file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blogService.saveBlog(blog);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/edit/{id}")
    public String editBlogForm(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blogId:" + id));
        model.addAttribute("blog", blog);
        return "admin/blogs/blog-edit";
    }

    @PostMapping("/edit/{id}")
    public String blogEdit(@PathVariable Long id, @ModelAttribute Blog blog, @RequestParam("avatar") MultipartFile file) {
        Blog currentBlog = blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blogId:" + id));
        if (!file.isEmpty()) {
            try {
                // Lưu ảnh vào thư mục static/images/blog
                String seoUrl = CommonFunction.SEOUrl(blog.getTitle());
                blog.setAvatarUrl(CommonFunction.saveFile(seoUrl, "/blogs", file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            blog.setAvatarUrl(currentBlog.getAvatarUrl());
        }
        blogService.saveBlog(blog);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteBlogById(id);
        return "redirect:/admin/blogs";
    }
}

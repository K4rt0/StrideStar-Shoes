package com.stores.stridestar.controllers;

import com.stores.stridestar.models.Blog;
import com.stores.stridestar.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public String listBlogs(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        blogs.forEach(blog -> System.out.println(blog.getTitle()));
        model.addAttribute("blogs", blogs);
        return "main-site/blog/index";
    }

    @GetMapping("/blog/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id).orElse(null);
        if (blog == null) {
            return "redirect:/blogs";
        }
        List<Blog> relatedBlogs = blogService.getRelatedBlogs(id, 5);
        model.addAttribute("blog", blog);
        model.addAttribute("relatedBlogs", relatedBlogs);
        return "main-site/blog/detail";
    }
}
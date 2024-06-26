package com.stores.stridestar.controllers.api;

import com.stores.stridestar.models.Blog;
import com.stores.stridestar.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogApiController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blogId:" + id));
    }

    @PostMapping("/create")
    public Blog createBlog(@ModelAttribute Blog blog, @RequestParam("avatar") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Lưu ảnh vào thư mục static/images/blog
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/static/images/blog/" + file.getOriginalFilename());
                Files.write(path, bytes);
                blog.setAvatarUrl("/images/blog/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blogService.saveBlog(blog);
    }

    @PutMapping("/edit/{id}")
    public Blog updateBlog(@PathVariable Long id, @ModelAttribute Blog blog, @RequestParam("avatar") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Lưu ảnh vào thư mục static/images/blog
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/static/images/blog/" + file.getOriginalFilename());
                Files.write(path, bytes);
                blog.setAvatarUrl("/images/blog/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blog.setId(id);
        return blogService.saveBlog(blog);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBlog(@PathVariable Long id) {
        blogService.deleteBlogById(id);
    }
}

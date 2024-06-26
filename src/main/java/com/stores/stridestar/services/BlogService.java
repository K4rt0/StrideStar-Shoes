package com.stores.stridestar.services;

import com.stores.stridestar.models.Blog;
import com.stores.stridestar.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Blog> getRecentBlogs() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        return blogRepository.findAll(pageable).getContent();
    }

    public List<Blog> getRelatedBlogs(Long excludeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return blogRepository.findByIdNot(excludeId, pageable).getContent();
    }
}

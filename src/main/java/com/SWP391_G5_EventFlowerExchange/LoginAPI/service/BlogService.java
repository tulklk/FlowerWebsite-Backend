package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Blog;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService implements IBlogService {
    @Autowired
    private IBlogRepository blogRepository;
    @Override
    public List<Blog> getBlog() {
        return blogRepository.findAll();
    }

    @Override
    public Blog insert(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog update(int blogID, Blog blog) {
        return null;
    }

    @Override
    public void delete(int blogID) {
       blogRepository.deleteById(blogID);
    }

    @Override
    public Optional<Blog> getBlogById(int blogID) {
        return blogRepository.findById(blogID);
    }
}

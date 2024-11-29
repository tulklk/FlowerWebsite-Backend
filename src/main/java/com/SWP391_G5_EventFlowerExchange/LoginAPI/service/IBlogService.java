package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Blog;

import java.util.List;
import java.util.Optional;

public interface IBlogService {
//    public List<Category> getAllCategories();
//    public Category insertCategory(Category category);
//    public Category updateCategory(int catetoryId,Category category);
//    public void deleteCategory(int catetoryId);
//    public Optional<Category> getCategoryById(int catetoryId);
public List<Blog> getBlog();
    public Blog insert(Blog blog);
    public Blog update(int blogID,Blog blog);
    public void delete(int blogID);
    public Optional<Blog> getBlogById(int blogID);
}

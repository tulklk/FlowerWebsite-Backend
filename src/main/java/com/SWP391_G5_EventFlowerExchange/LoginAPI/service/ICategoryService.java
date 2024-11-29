package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category insertCategory(Category category);
    Category updateCategory(int categoryId,Category category);
    void deleteCategory(int categoryId);
    Optional<Category> getCategoryById(int categoryId);
}

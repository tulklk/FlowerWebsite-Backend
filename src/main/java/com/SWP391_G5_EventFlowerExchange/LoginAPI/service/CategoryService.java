package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Category;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.ICatetoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {

    ICatetoryRepository iCatetoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return iCatetoryRepository.findAll();
    }

    @Override
    public Category insertCategory(Category category) {
        return iCatetoryRepository.save(category);
    }

    @Override
    public Category updateCategory(int categoryId, Category category) {
        Category cate = iCatetoryRepository.getById(categoryId);
        if(cate != null){

            if (category.getFlowerType() != null) {
                cate.setFlowerType(category.getFlowerType());
            }
            iCatetoryRepository.save(cate);

        }

        return null;
    }

    @Override
    public void deleteCategory(int categoryId) {
        iCatetoryRepository.deleteById(categoryId);
    }

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        return iCatetoryRepository.findById(categoryId);
    }
}

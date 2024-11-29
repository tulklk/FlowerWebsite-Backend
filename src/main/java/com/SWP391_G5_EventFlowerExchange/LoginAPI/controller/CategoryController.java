package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Category;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<Category>> fetchAll(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Category saveCate(@RequestBody Category cateId) {

        return categoryService.insertCategory(cateId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCateId(@PathVariable int id, @RequestBody Category cate) {
        Category updateCateId = categoryService.updateCategory(id, cate);
        return ResponseEntity.ok(updateCateId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCate(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Deleted!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getCateById(@PathVariable int id) {
        Optional<Category> cate= categoryService.getCategoryById(id);
        return ResponseEntity.ok(cate);
    }

}

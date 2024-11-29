package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Blog;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Category;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @GetMapping("/")
    public ResponseEntity<List<Blog>> fetchAll(){
        return ResponseEntity.ok(blogService.getBlog());
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Blog saveBlog(@RequestBody Blog blogID) {
        return blogService.insert(blogID);//201 CREATED
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable int id) {
        blogService.delete(id);
        return ResponseEntity.ok("Deleted!");
    }

}

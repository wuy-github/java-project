package com.project.futabuslines.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    // Hien thi tat ca cac Category
    @GetMapping("") // http://localhost:8088/api/v1/categories
    public ResponseEntity<String> getAllCategory(){
        return ResponseEntity.ok("Get All Categories");
    }
    @PostMapping("")
    public ResponseEntity<String> insertCategory(){
        return ResponseEntity.ok("Insert Category");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("Update Categories with id = " + id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return  ResponseEntity.ok("Delete Categories with id = " + id);
    }
}
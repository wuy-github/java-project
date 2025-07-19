package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.CategoryDTO;
import com.project.futabuslines.models.Category;
import com.project.futabuslines.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("create-category")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(category);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("get-all-categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories =categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("get-category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update-category/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            Category category = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(category +"\nUpdate Category Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("delete-category/{id}")
    public ResponseEntity<String> deteteCategory(
            @PathVariable long id
    ){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Delete Category Successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

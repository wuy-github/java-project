package com.project.futabuslines.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    @PostMapping("add-cart")
    public ResponseEntity<?> addCart(
            @Valid @RequestBody CartDTO cartDTO ,
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
            return ResponseEntity.ok("Cart added successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-cart/{userId}")
    public ResponseEntity<?> getCartByUserId(
        @PathVariable("userId") long userId
    ){
        try{
            return ResponseEntity.ok("Cart retrieved successfully for user ID: " + userId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deteleCart(
            @PathVariable long id
    ){
        try {
            return ResponseEntity.ok("Cart deleted successfully for ID: " + id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @PostMapping("add-cart")
    public ResponseEntity<?> addCart(
            @Valid @RequestBody CartDTO cartDTO,
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
            Cart cart = cartService.addCart(cartDTO);
            return ResponseEntity.ok(cart);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-cart/{userId}")
    public ResponseEntity<?> getCartByUserId(
        @PathVariable("userId") long userId
    ){
        try{
            List<Cart> carts = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(carts);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete-cart/{id}")
    public ResponseEntity<?> deteleCart(
            @PathVariable long id
    ){
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok("Delete Cart Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

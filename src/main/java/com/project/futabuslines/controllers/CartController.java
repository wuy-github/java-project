package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.responses.CartResponse;
import com.project.futabuslines.services.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;
    private final ICartService cartService;

    @PostMapping("add-cart")
    // Them vao gio hang
    // Phan quyen buyer
    // Chua dang nhap thi khong duoc them
    public ResponseEntity<?> addCart(
            @Valid @RequestBody CartDTO cartDTO,
            @RequestHeader(value = "Authorization", required = false) String token,
            BindingResult result
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            Cart cart = cartService.addCart(cartDTO, userId);
            return ResponseEntity.ok(cart);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-cart")
    public ResponseEntity<?> getCartByUserId(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try{
            Long userId = authUtil.extractUserIdFromToken(token);
            List<Cart> carts = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(carts);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
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

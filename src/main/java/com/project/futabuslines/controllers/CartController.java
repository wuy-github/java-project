package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.CartDTO;
import com.project.futabuslines.models.Cart;
import com.project.futabuslines.models.User;
import com.project.futabuslines.responses.CartResponse;
import com.project.futabuslines.services.EntityFinder;
import com.project.futabuslines.services.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;
    private final ICartService cartService;
    private final EntityFinder entityFinder;

    @PostMapping("add")
    // Them vao gio hang
    // Phan quyen buyer
    // Chua dang nhap thi khong duoc them
    public ResponseEntity<?> addCart(
            @Valid @RequestBody CartDTO cartDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            CartResponse cart = cartService.addCart(cartDTO, userId);
            return ResponseEntity.ok(cart);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get")
    public ResponseEntity<?> getCartByUserId(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try{
            Long userId = authUtil.extractUserIdFromToken(token);
            List<CartResponse> carts = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(carts);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/decrease/{id}")
    public ResponseEntity<?> decreaseCartQuantity(
            @PathVariable("id") Long cartId,
            @Valid @RequestBody CartDTO cartDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }

        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            Cart cart = entityFinder.findCartById(cartId);

            if (!Objects.equals(cart.getUser().getId(), userId)) {
                return ResponseEntity.status(403).body("Bạn không có quyền sửa giỏ hàng này.");
            }

            CartResponse updatedCart = cartService.decreaseQuantity(cartDTO, cart);

            if (updatedCart == null) {
                return ResponseEntity.ok().body(Map.of(
                        "message", "Cart item deleted due to zero quantity.",
                        "cartId", cartId
                ));
            }

            return ResponseEntity.ok(updatedCart);} catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deteleCart(
            @PathVariable long id,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            Cart cart = entityFinder.findCartById(id);
            if (!Objects.equals(cart.getUser().getId(), userId)) {
                return ResponseEntity.status(403).body("Bạn không có quyền sửa giỏ hàng này.");
            }
            cartService.deleteCart(id);
            return ResponseEntity.ok("Delete Cart Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

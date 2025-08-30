package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.FavoriteDTO;
import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.responses.FavoriteResponse;
import com.project.futabuslines.services.IFavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final IFavoriteService favoriteService;
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;

    @PostMapping("create")
    // Them yeu thich
    public ResponseEntity<?> addFavorite(
            @Valid @RequestBody FavoriteDTO favoriteDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            FavoriteResponse favorite = favoriteService.addFavorite(favoriteDTO, userId);
            return ResponseEntity.ok(favorite);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get")
    // Lay danh sach yeu thich cua user
    public ResponseEntity<?> getWatchByUserId(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            List<FavoriteResponse> favorite = favoriteService.getFavoriteByUserId(userId);
            return ResponseEntity.ok(favorite);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    // Xoa
    public ResponseEntity<?> deteteCategory(
            @PathVariable long id
    ){
        try {
            favoriteService.deleteFavorite(id);
            return ResponseEntity.ok("Delete Favorite Successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

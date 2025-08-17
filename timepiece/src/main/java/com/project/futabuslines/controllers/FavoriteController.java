package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.FavoriteDTO;
import com.project.futabuslines.models.Favorite;
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

    @PostMapping("create")
    // Them yeu thich
    public ResponseEntity<?> addFavorite(
            @Valid @RequestBody FavoriteDTO favoriteDTO,
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
            Favorite favorite = favoriteService.addFavorite(favoriteDTO);
            return ResponseEntity.ok(favorite);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-favorite/{userId}")
    // Lay danh sach yeu thich cua user
    public ResponseEntity<?> getWatchByUserId(@PathVariable long userId){
        try {
            List<Favorite> favorite = favoriteService.getFavoriteByUserId(userId);
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

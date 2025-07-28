package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.BrandDTO;
import com.project.futabuslines.models.Brand;
import com.project.futabuslines.services.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/brand")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;

    @PostMapping("create")
    public ResponseEntity<?> createBrand(
            @Valid @RequestBody BrandDTO brandDTO,
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
            Brand brand = brandService.createBrand(brandDTO);
            return ResponseEntity.ok(brand);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<List<Brand>> getAllBrands(){
        List<Brand> brands =brandService.getAllBrand();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable long id){
        try {
            Brand brand = brandService.getBrandById(id);
            return ResponseEntity.ok(brand);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateBrand(
            @PathVariable long id,
            @Valid @RequestBody BrandDTO brandDTO,
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
            Brand brand = brandService.updateBrand(id, brandDTO);
            return ResponseEntity.ok(brand + "\nUpdate Brand Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deteteBrand(
            @PathVariable long id
    ){
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok("Delete Brand Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

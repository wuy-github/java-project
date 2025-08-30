package com.project.futabuslines.controllers;

import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.BrandDTO;
import com.project.futabuslines.models.Brand;
import com.project.futabuslines.services.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/brand")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    private final ValidationUtil validationUtil;

    @PostMapping("create")
    // Tao nhan hang moi
    // Phan quyen Admin
    public ResponseEntity<?> createBrand(
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Brand brand = brandService.createBrand(brandDTO);
            URI location = URI.create("/api/v1/brand/" + brand.getId());
            return ResponseEntity.created(location).body(brand);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all")
    // Lay toan bo danh sach Brand
    // Khong phan quyen => Can response de phuc vu filter tim kiem
    public ResponseEntity<List<Brand>> getAllBrands(){
        List<Brand> brands =brandService.getAllBrand();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("get/{id}")
    // Lay mot brand theo id cua no
    // Khong can thiet
    public ResponseEntity<?> getBrandById(@PathVariable long id){
        try {
            Brand brand = brandService.getBrandById(id);
            return ResponseEntity.ok(brand);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    // Cap nhat brand
    // Phan quyen admin
    public ResponseEntity<?> updateBrand(
            @PathVariable long id,
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Brand brand = brandService.updateBrand(id, brandDTO);
            return ResponseEntity.ok(brand);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    // Xoa brand
    // Phan quyen admin
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

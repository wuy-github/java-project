package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.services.IAppraisalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appraisal")
@RequiredArgsConstructor
public class AppraisalController {
    private final IAppraisalService appraisalService;
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;

    @PostMapping("create")
    // Chi co chinh chu moi co the tao bang tham dinh moi
    // Phan quyen Appraisal
    public ResponseEntity<?> createAppraisal(
            @Valid @RequestBody AppraisalDTO appraisalDTO,
            @RequestHeader(value = "Authorization", required = false) String token,
            BindingResult result
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            Appraisal appraisal = appraisalService.createAppraisal(appraisalDTO, userId);
            return ResponseEntity.ok(appraisal);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all")
    // Lay toan bo tham dinh (Chua response data tra ve)
    // Phan quyen admin
    public ResponseEntity<?> getAllAppraisal(){
        List<Appraisal> appraisal = appraisalService.getAllAppraisal();
        return ResponseEntity.ok(appraisal);
    }

    @GetMapping("get-user/{userId}")
    // Chua xac dinh quyen cu the - co the public
    // Lay danh sach bang tham dinh cua 1 nha tham dinh nao do
    // => Co the bo vao profile - bat ki ai co the truy cap de xem
    // => Hien thi admin
    // (Chua response)
    public ResponseEntity<?> getAppraisalByUser(
            @PathVariable long userId
    ){
        List<Appraisal> appraisals = appraisalService.getAppraisalByUserId(userId);
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("get-watch/{watchId}")
    // public - khong phan quyen
    // Lay danh sach tham dinh cua cac nha tham dinh o tai 1 san pham nao do
    // Bat ky ai co the truy cap de xem <-> Xem thong qua trang watch detail
    // (Chua response)
    public ResponseEntity<?> getAppraisalByWatch(
            @PathVariable long watchId
    ){
        List<Appraisal> appraisals = appraisalService.getAppraisalByWatchId(watchId);
        return ResponseEntity.ok(appraisals);
    }

    @PutMapping("update/{id}")
    // Cap nhat tham dinh, co the can nhac gioi han so lan cap nhat < 2
    // Phan quyen Appraisal
    public ResponseEntity<?> updateAppraisal(
            @PathVariable long id,
            @Valid @RequestBody AppraisalDTO appraisalDTO,
            @RequestHeader(value = "Authorization", required = false) String token,
            BindingResult result
    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try{
            Long userId = authUtil.extractUserIdFromToken(token);
            Appraisal appraisal = appraisalService.updateAppraisal(id, appraisalDTO, userId);
            return ResponseEntity.ok(appraisal);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    // Xoa tham dinh
    // => Admin co the xoa neu appraisal quy pham vao co the khoa tai khoan cua appraisal neu quy pham nhieu lan
    // => Appraisal chinh chu co the xoa bang tham dinh cua minh
    // Phan quyen Admin - Appraisal
    public ResponseEntity<?> deleteAppraisal(
            @PathVariable long id
    ){
        try {
            appraisalService.deleteAppraisal(id);
            return ResponseEntity.ok("Delete Appraisal Successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

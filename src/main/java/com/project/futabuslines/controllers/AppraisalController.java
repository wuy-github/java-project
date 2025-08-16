package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.services.AppraisalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appraisal")
@RequiredArgsConstructor
public class AppraisalController {
    private final AppraisalService appraisalService;

    @PostMapping("create")
    public ResponseEntity<?> createAppraisal(
            @Valid @RequestBody AppraisalDTO appraisalDTO,
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
            Appraisal appraisal = appraisalService.createAppraisal(appraisalDTO);
            return ResponseEntity.ok(appraisal);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllAppraisal(){
        List<Appraisal> appraisal = appraisalService.getAllAppraisal();
        return ResponseEntity.ok(appraisal);
    }

    @GetMapping("get-user/{userId}")
    public ResponseEntity<?> getAppraisalByUser(
            @PathVariable long userId
    ){
        List<Appraisal> appraisals = appraisalService.getAppraisalByUserId(userId);
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("get-watch/{watchId}")
    public ResponseEntity<?> getAppraisalByWatch(
            @PathVariable long watchId
//        return null;
    ){
        List<Appraisal> appraisals = appraisalService.getAppraisalByWatchId(watchId);
        return ResponseEntity.ok(appraisals);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateAppraisal(
            @PathVariable long id,
            @Valid @RequestBody AppraisalDTO appraisalDTO,
            BindingResult result
    ){
        if (result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try{
            Appraisal appraisal = appraisalService.updateAppraisal(id, appraisalDTO);
            return ResponseEntity.ok(appraisal);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
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

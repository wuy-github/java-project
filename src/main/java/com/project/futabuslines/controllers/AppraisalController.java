package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.FileStorageUtil;
import com.project.futabuslines.dtos.AppraisalDTO;
import com.project.futabuslines.dtos.AppraisalReportDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.responses.AppraisalResponse;
import com.project.futabuslines.services.EntityFinder;
import com.project.futabuslines.services.FileService;
import com.project.futabuslines.services.IAppraisalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/appraisal")
@RequiredArgsConstructor
public class AppraisalController {
    private final IAppraisalService appraisalService;
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;
    private final FileStorageUtil fileStorageUtil;
    private final FileService fileService;
    private final EntityFinder entityFinder;

//    @PostMapping("create")
//    // Chi co chinh chu moi co the tao bang tham dinh moi
//    // Phan quyen Appraisal
//    public ResponseEntity<?> createAppraisal(
//            @Valid @RequestBody AppraisalDTO appraisalDTO,
//            BindingResult result,
//            @RequestHeader(value = "Authorization", required = false) String token
//
//    ){
//        if (validationUtil.hasErrors(result)) {
//            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
//        }
//        try {
//            Long userId = authUtil.extractUserIdFromToken(token);
//            AppraisalResponse appraisal = appraisalService.createAppraisal(appraisalDTO, userId);
//            URI location = URI.create("/api/v1/appraisal/" + appraisal.getId());
////            return ResponseEntity.ok(appraisal);
//            return ResponseEntity.created(location).body(appraisal);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping("create")
    // Role: Appraisers - Nha tham dinh
    // Thuc hien danh gia
    public ResponseEntity<?> createAppraisal(
            @Valid @RequestBody AppraisalDTO appraisalDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ) throws DataNotFoundException, ResourceAlreadyExistsException { // khai báo throw nếu cần
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }

        Long userId = authUtil.extractUserIdFromToken(token);
        AppraisalResponse appraisal = appraisalService.createAppraisal(appraisalDTO, userId);
        URI location = URI.create("/api/v1/appraisal/" + appraisal.getId());
        return ResponseEntity.created(location).body(appraisal);
    }

    @PostMapping(value = "upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Role: Appraisers - Nha tham dinh
    // Tao bao cao
    public ResponseEntity<?> uploadAppraisalReport(
            @PathVariable("id") Long appraisalId,
            @RequestParam("file") List<MultipartFile> files,
            @RequestHeader(value = "Authorization", required = false) String token
            ){
        try{
            Long userId = authUtil.extractUserIdFromToken(token);
            if(userId ==null){
                return ResponseEntity.badRequest().body("User Id null");
            }
            Appraisal appraisal = entityFinder.findAppraisalById(appraisalId);
            if(!Objects.equals(appraisal.getUser().getId(), userId)){
                return ResponseEntity.badRequest().body("Ban khong co quyen chinh sua Tham dinh cua nguoi khac");
            }
            if (files.get(0).isEmpty()) {
                return ResponseEntity.badRequest().body("Cannot empty files!");
            }
            if (files.size() > 2) {
                return ResponseEntity.badRequest().body("You can only upload maximum 2 report files!");
            }
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiem tra kich thuoc file va dinh dang
                if (file.getSize() > 100 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File us too large! Maximum size is 100MB");
                }
                String contentType = file.getContentType();
                if (contentType == null ||
                        (!contentType.equals("application/pdf") &&
                                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be PDF or Word (.docx)");
                }
                // Luu file va cap nhat thumbnail trong DTO
                String filename = fileStorageUtil.storeDocumentFile(file);
                appraisalService.uploadAppraisalReport(appraisalId,
                        AppraisalReportDTO.builder()
                                .appraisalReport(filename)
                                .build());
            }
            return ResponseEntity.ok("Upload Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-all")
    // Role: Administrator
    // Lay toan bo bao cao tham dinh
    public ResponseEntity<?> getAllAppraisal(){
        List<AppraisalResponse> appraisals = appraisalService.getAllAppraisal();
        return ResponseEntity.ok(appraisals);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getAppraisalById(
            @PathVariable("id") long appraisalId
    ){
        try {
            AppraisalResponse appraisal = appraisalService.getAppraisalById(appraisalId);
            return ResponseEntity.ok(appraisal);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        List<AppraisalResponse> appraisals = appraisalService.getAppraisalByUserId(userId);
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
        List<AppraisalResponse> appraisals = appraisalService.getAppraisalByWatchId(watchId);
        return ResponseEntity.ok(appraisals);
    }

    @PutMapping("update/{id}")
    // Cap nhat tham dinh, co the can nhac gioi han so lan cap nhat < 2
    // Phan quyen Appraisal
    public ResponseEntity<?> updateAppraisal(
            @PathVariable long id,
            @Valid @RequestBody AppraisalDTO appraisalDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token

    ){
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try{
            Long userId = authUtil.extractUserIdFromToken(token);
            AppraisalResponse appraisal = appraisalService.updateAppraisal(id, appraisalDTO, userId);
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

    @GetMapping("/report/{id}")
    public ResponseEntity<?> getAppraisalReport(@PathVariable Long id) {
        try {
            Appraisal appraisal = appraisalService.findById(id);
            byte[] fileData = fileService.loadFile(appraisal.getAppraisalReport());

            String fileName = appraisal.getAppraisalReport();
            String contentType = fileName.endsWith(".pdf") ? "application/pdf" :
                    fileName.endsWith(".docx") ? "application/vnd.openxmlformats-officedocument.wordprocessingml.document" :
                            "application/octet-stream";

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

package com.project.futabuslines.controllers;

import com.project.futabuslines.components.JwtTokenUtil;
import com.project.futabuslines.dtos.UpdateWatchDTO;
import com.project.futabuslines.dtos.WatchDTO;
import com.project.futabuslines.dtos.WatchImageDTO;
import com.project.futabuslines.enums.WatchStatus;
import com.project.futabuslines.models.*;
import com.project.futabuslines.responses.WatchUserViewResponse;
import com.project.futabuslines.service.IWatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/watches")
@RequiredArgsConstructor
public class WatchController {
    private final IWatchService watchService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("create-watches")
    // Dang tai dong ho moi
    public ResponseEntity<?> createWatch(
            @Valid @RequestBody WatchDTO watchDTO,
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
            Watch watch = watchService.createWatch(watchDTO);
            return ResponseEntity.ok(watch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("get-all-watch")
    // Lay toan bo danh sach
    public ResponseEntity<List<Watch>> getAllWatch(){
        List<Watch> watch =watchService.getAllWatch();
        return ResponseEntity.ok(watch);
    }

    @GetMapping("get-watches/{id}")
    // Lay 1 dong ho theo id
    public ResponseEntity<?> getWatchById(@PathVariable long id){
        try {
            Watch watch = watchService.getWatchById(id);
            return ResponseEntity.ok(watch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update-watches/{id}")
    // Cap nhat thong tin
    public ResponseEntity<?> updateWatch(
            @PathVariable long id,
            @Valid @RequestBody UpdateWatchDTO watchDTO,
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
            Watch watch = watchService.updateWatch(id, watchDTO);
            return ResponseEntity.ok(watch +"\nUpdate Category Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("delete-watches/{id}")
    // Xoa
    public ResponseEntity<String> deteteWatch(
            @PathVariable long id
    ){
        try {
            watchService.deleteWatch(id);
            return ResponseEntity.ok("Delete Watch Successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/watch-statuses")
    // Lay enum danh sach trang thai dang ban
    public ResponseEntity<List<String>> getAllWatchStatuses() {
        List<String> statuses = Arrays.stream(WatchStatus.values())
                .map(WatchStatus::getValue)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/user/{userId}")
    // Response lai de co the hien thi hinh anh
    public List<WatchUserViewResponse> getAllWatchesForUser(@PathVariable Long userId) {
        return watchService.getAllWatchesForUser(userId);
    }

    @GetMapping("/watches")
    public ResponseEntity<?> getAllWatches(@RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = null;

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            userId = jwtTokenUtil.extractUserId(jwt);
        }

        List<WatchUserViewResponse> response = watchService.getAllWatches(userId);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadWatchImage(
            @PathVariable("id") Long watchId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            Watch existingWatch = watchService.getWatchById(watchId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > WatchImage.MAXIMUM_IMAGES_PER_WATCH) {
                return ResponseEntity.badRequest().body("You can only upload maximum 9 images!");
            }
            List<WatchImage> watchImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiem tra kich thuoc file va dinh dang
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File us too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Luu file va cap nhat thumbnail trong DTO
                String filename = storeFile(file);
                WatchImage watchImage = watchService.uploadWatchImage(
                        existingWatch.getId(),
                        WatchImageDTO.builder()
                                .watchId(existingWatch.getId())
                                .imageUrl(filename)
                                .build()
                );

                watchImages.add(watchImage);
            }
            return ResponseEntity.ok().body(watchImages);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        // Lay ten file goc va lam sach => An toan hop le
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Them UUID vao truoc ten file de dam bao ten file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Duong dan den thu muc muon luu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiem tra va tao thuc muc neu no khong ton tai
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Duong dan den file day du
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}

package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.JwtTokenUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.UpdateWatchDTO;
import com.project.futabuslines.dtos.WatchDTO;
import com.project.futabuslines.dtos.WatchImageDTO;
import com.project.futabuslines.enums.WatchStatus;
import com.project.futabuslines.models.*;
import com.project.futabuslines.responses.ApiResponse;
import com.project.futabuslines.responses.WatchListUserViewResponse;
import com.project.futabuslines.responses.WatchUserViewResponse;
import com.project.futabuslines.services.IWatchService;
import com.project.futabuslines.components.FileStorageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/watch")
@RequiredArgsConstructor
public class WatchController {
    private final IWatchService watchService;
    private final JwtTokenUtil jwtTokenUtil;
    private final FileStorageUtil fileStorageUtil;
    private final AuthUtil authUtil;
    private final ValidationUtil validationUtil;

    @PostMapping("create")
    public ResponseEntity<?> createWatch(
            @Valid @RequestBody WatchDTO watchDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<String>>builder()
                            .success(false)
                            .message("Validation failed")
                            .data(validationUtil.getErrorMessages(result))
                            .build()
            );
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            Watch watch = watchService.createWatch(watchDTO, userId);
            return ResponseEntity.ok(
                    ApiResponse.<Watch>builder()
                            .success(true)
                            .message("Watch created successfully")
                            .data(watch)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Watch>builder()
                            .success(false)
                            .message("Error: " + e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("get-all")
    // Lay toan bo danh sach
    public ResponseEntity<List<Watch>> getAllWatch(){
        List<Watch> watch =watchService.getAllWatch();
        return ResponseEntity.ok(watch);
    }

    @GetMapping("get/{id}")
    // Lay 1 dong ho theo id
    public ResponseEntity<?> getWatchById(@PathVariable long id){
        try {
            Watch watch = watchService.getWatchById(id);
            return ResponseEntity.ok(watch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
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

    @DeleteMapping("delete/{id}")
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

    // Upload anh cho Watch
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
                String filename = fileStorageUtil.storeImageFile(file);
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

    @GetMapping("get-watch")
    public ResponseEntity<?> getWatch(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "brandName", required = false) String brandName,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            Long userId = null;
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                userId = jwtTokenUtil.extractUserId(jwt);
            }
            Sort sort = order.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
            PageRequest pageRequest = PageRequest.of(page, limit, sort);
            Page<WatchUserViewResponse> watchPage = watchService.getWatchesByBrandAndCategory(
                    brandName, categoryName, pageRequest, userId
            );

            return ResponseEntity.ok(WatchListUserViewResponse.builder()
                    .watch(watchPage.getContent())
                    .totalPage(watchPage.getTotalPages())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/watches")
    public ResponseEntity<?> getWatches(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int limit
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, limit); // page start from 0
            Page<Watch> watchPage = watchService.getWatches(pageRequest);

            return ResponseEntity.ok(
                    Map.of(
                            "watches", watchPage.getContent(),
                            "totalPage", watchPage.getTotalPages()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }




}

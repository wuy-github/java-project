package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.repositories.FeedbackRepository;
import com.project.futabuslines.services.FeedbackService;
import com.project.futabuslines.components.FileStorageUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackRepository feedbackRepository;
    private final FileStorageUtil fileStorageUtil;

    // POST: http://localhost:8080/api/v1/notifications
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFeedback(
            @Valid @ModelAttribute FeedbackDTO feedbackDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            List<MultipartFile> files = feedbackDTO.getFiles();
            files = (files == null) ? new ArrayList<>() : files;

            if (files.size() > 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Chỉ được phép tải lên tối đa 3 ảnh.");
            }

            List<String> imageNames = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.getSize() == 0) continue;

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                String filename = fileStorageUtil.storeFile(file); // ✅ GỌI TỪ UTILITY
                imageNames.add(filename);
            }

            // Gắn tên ảnh vào DTO nếu cần (tuỳ cấu trúc FeedbackDTO và DB của bạn)
            feedbackDTO.setImageUrls(
                    String.join(",", imageNames));

            Feedback feedback = feedbackService.createFeedback(
                    feedbackDTO.getUserId(), feedbackDTO
            );
            return ResponseEntity.ok(feedback);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
        Path uploadDir = Paths.get("uploads");
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFeedbacksByUserId(@PathVariable Long userId) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByUserId(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: http://localhost:8080/api/v1/notifications/read/{id}
    @PutMapping("/read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        feedbackRepository.save(feedback);
        return ResponseEntity.ok("Đã cập nhật trạng thái thông báo");
    }

    // GET: http://localhost:8080/api/v1/notifications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackDetail(@PathVariable Long id) {
        try {
            Feedback feedback = feedbackService.getFeedbackDetailById(id);
            return ResponseEntity.ok(feedback);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // GET: http://localhost:8080/api/v1/notifications
    @GetMapping("")
    public ResponseEntity<?> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();

            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}

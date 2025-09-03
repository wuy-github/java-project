package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.repositories.FeedbackRepository;
import com.project.futabuslines.responses.FeedbackResponse;
import com.project.futabuslines.services.FeedbackService;
import com.project.futabuslines.components.FileStorageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
@RequestMapping("${api.prefix}/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackRepository feedbackRepository;
    private final FileStorageUtil fileStorageUtil;
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;

    // POST: http://localhost:8080/api/v1/notifications
    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFeedback(
            @Valid @ModelAttribute FeedbackDTO feedbackDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            if(userId == null){
                return ResponseEntity.badRequest().body("User Id not null");
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

                String filename = fileStorageUtil.storeImageFile(file); // ✅ GỌI TỪ UTILITY
                imageNames.add(filename);
            }

            // Gắn tên ảnh vào DTO nếu cần (tuỳ cấu trúc FeedbackDTO và DB của bạn)
            feedbackDTO.setImageUrls(
                    String.join(",", imageNames));

            FeedbackResponse feedback = feedbackService.createFeedback(
                    userId, feedbackDTO
            );
            return ResponseEntity.ok(feedback);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-user")
    public ResponseEntity<?> getFeedbacksByUserId(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            List<FeedbackResponse> feedbacks = feedbackService.getFeedbacksByUserId(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-watch/{watchId}")
    public ResponseEntity<?> getFeedbackByWatchId(@PathVariable Long watchId) {
        try {
            List<FeedbackResponse> feedbacks = feedbackService.getFeedbackByWatchId(watchId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        try{
            FeedbackResponse feedback = feedbackService.getFeedbackDetailById(id);
            return ResponseEntity.ok(feedback);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllFeedback(){
        try{
            List<FeedbackResponse> feedback = feedbackService.getAllFeedBack();
            return ResponseEntity.ok(feedback);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFeedback(
            @PathVariable Long id
    ){
        try{
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok("Delete successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

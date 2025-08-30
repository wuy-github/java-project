package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.dtos.*;
import com.project.futabuslines.models.Token;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.UserImage;
import com.project.futabuslines.repositories.TokenRepository;
import com.project.futabuslines.responses.LoginResponseDTO;
import com.project.futabuslines.responses.UserDetailResponse;
import com.project.futabuslines.responses.UserResponse;
import com.project.futabuslines.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final TokenRepository tokenRepository;
    private final IUserService userService;
    private final AuthUtil authUtil;

    // POST: http://localhost:8080/api/v1/users/register
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match");
            }

            userService.createUser(userDTO);
            return ResponseEntity.ok("Register successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            LoginResponseDTO token = userService.login(userLoginDTO);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/users/uploads/{id}
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserImage(
            @PathVariable("id") Long userId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            User existingUser = userService.getUserById(userId);
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > UserImage.MAXIMUM_IMAGES_PER_USER) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images!");
            }
            List<UserImage> productImages = new ArrayList<>();
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
                UserImage productImage = userService.uploadUserImage(
                        existingUser.getId(),
                        UserImageDTO.builder()
                                .userId(existingUser.getId())
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);

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

    // PUT: http://localhost:8080/api/v1/users/update/{id}
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUploadDTO userDTO
    ) {
        try {
            userService.updateUser(id, userDTO);
            return ResponseEntity.ok("Update Bus successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // DELETE: http://localhost:8080/api/v1/users/delete{id}
    @DeleteMapping("delete/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleUser(id);
    }

    // POST: http://localhost:8080/api/v1/users/logout
    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(token).orElse(null);

        if (storedToken != null) {
//            tokenRepository.delete(storedToken);  // hoặc
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
        return ResponseEntity.ok("✅ Logout successful");
    }

    // GET: http://localhost:8080/api/v1/users/get-info/{id}
    @GetMapping("get-info/{id}")
    public ResponseEntity<UserResponse> getInfor(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("get-info")
    public ResponseEntity<UserResponse> getInfoUser(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        Long userId = authUtil.extractUserIdFromToken(token);
        UserResponse user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    // GET: // POST: http://localhost:8080/api/v1/users/get-user/{id}
    @GetMapping("get-user")
    public ResponseEntity<?> getInforUser(
            @RequestHeader(value = "Authorization", required = false) String token
    ){
        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            UserDetailResponse user = userService.getUser(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/users/auth/reset-password
    @PostMapping("auth/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) throws Exception {
        userService.resetPassword(dto);
        return ResponseEntity.ok("Mật khẩu đã được cập nhật thành công.");
    }

    // GET: http://localhost:8080/api/v1/users/all
    @GetMapping("all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}



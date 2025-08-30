package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.OTPRequest;
import com.project.futabuslines.dtos.OTPResetPasswordDTO;
import com.project.futabuslines.dtos.OtpVerificationRequest;
import com.project.futabuslines.dtos.UserSummaryDTO;
import com.project.futabuslines.responses.OTPResponse;
import com.project.futabuslines.services.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/otp")
@RequiredArgsConstructor
public class    OTPController {
    private final OTPService otpService;

    // POST: http://localhost:8080/api/v1/otp/send
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OTPRequest contact) {
        try {
            Object result = otpService.sendOtp(contact.getContact());

            if (result instanceof UserSummaryDTO) {
                return ResponseEntity.ok(result); // Tra ve json nguoi dung da dang ky = true
            } else if (result instanceof String) {
                return ResponseEntity.ok(result); // Gui OTP xac nhan
            } else {
                return ResponseEntity.status(500).body("Unexpected response");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/otp/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<?> sendOtpResetPassWord(@RequestBody OTPResetPasswordDTO contact) {
        try {
            String result = otpService.sendOtpForResetPassword(contact.getContact());
                return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/otp/verify
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        OTPResponse response = otpService.verifyOtp(request.getContact(), request.getOtp());
        return ResponseEntity.ok(response);
    }
}

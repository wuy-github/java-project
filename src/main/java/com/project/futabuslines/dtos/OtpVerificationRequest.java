package com.project.futabuslines.dtos;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String contact;

    private String otp;
}
package com.project.futabuslines.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String fullName;
    private Long roleId;
    private String imageUrl;
}
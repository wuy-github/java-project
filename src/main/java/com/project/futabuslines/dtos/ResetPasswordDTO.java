package com.project.futabuslines.dtos;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String contact;

    private String newPassword;
}

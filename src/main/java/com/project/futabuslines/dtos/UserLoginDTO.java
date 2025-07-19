package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserLoginDTO {
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;


    public String getIdentifier() {
        return (email != null && !email.isBlank()) ? email : phoneNumber;
    }

}

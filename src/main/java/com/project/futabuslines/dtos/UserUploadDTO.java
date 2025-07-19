package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserUploadDTO {

    @JsonProperty("fullname")
    private String fullName;

    @Email(message = "Email không hợp lệ")
//    @NotBlank(message = "Email không được để trống!")
    private String email;

    private String address;
    private String job;
    private String sex;

}

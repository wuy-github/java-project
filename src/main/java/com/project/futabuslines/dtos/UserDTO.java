package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ!")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=|<>?{}\\[\\]~-]).{8,}$",
            message = "Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số và một ký tự đặc biệt")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("role_id")
    @NotNull(message = "Role ID không được để trống!!!")
    private Long roleId;

    private String address;

    private String job;

    private String sex;
}

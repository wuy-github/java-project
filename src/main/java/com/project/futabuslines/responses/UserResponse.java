package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}

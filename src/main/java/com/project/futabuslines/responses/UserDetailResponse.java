package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;


import com.project.futabuslines.models.User;
import com.project.futabuslines.models.UserImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetailResponse {
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String address;
    private String job;
    private String sex;

    private List<String> userImages;

    public static UserDetailResponse fromUser(User user){
        return UserDetailResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .job(user.getJob())
                .sex(user.getSex())
                .userImages(user.getUserImages().stream()
                        .map(UserImage::getImageUrl)
                        .toList())
                .build();
    }
}

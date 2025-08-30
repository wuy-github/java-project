package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.dtos.UserSimpleDTO;
import com.project.futabuslines.dtos.WatchSimpleDTO;
import com.project.futabuslines.models.Favorite;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class FavoriteResponse {
    private Long id;

    private UserSimpleDTO user;

    private WatchSimpleDTO watch;


    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    public static FavoriteResponse fromFavorite(Favorite favorite){
        User user = favorite.getUser();
        UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
        userSimpleDTO.setId(user.getId());
        userSimpleDTO.setFullName(user.getFullName());
        userSimpleDTO.setPhoneNumber(user.getPhoneNumber());

        Watch watch = favorite.getWatch();
        WatchSimpleDTO watchDTO = new WatchSimpleDTO();
        watchDTO.setName(watch.getName());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setId(watch.getId());

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .user(userSimpleDTO)
                .watch(watchDTO)
                .createdAt(favorite.getCreatedAt())
                .isActive(favorite.getIsActive())
                .build();
    }
}


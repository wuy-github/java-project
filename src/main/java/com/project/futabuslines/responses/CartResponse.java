package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.dtos.UserSimpleDTO;
import com.project.futabuslines.dtos.WatchDTO;
import com.project.futabuslines.dtos.WatchSimpleDTO;
import com.project.futabuslines.models.Cart;
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
public class CartResponse {
    private Long id;

    private UserSimpleDTO user;

    @JsonProperty("watch")
    private WatchSimpleDTO watch;


    private Integer quantity;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    public static CartResponse fromCart(Cart cart){
        User user = cart.getUser();
        UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
        userSimpleDTO.setId(user.getId());
        userSimpleDTO.setFullName(user.getFullName());
        userSimpleDTO.setPhoneNumber(user.getPhoneNumber());

        Watch watch = cart.getWatch();
        WatchSimpleDTO watchDTO = new WatchSimpleDTO();
        watchDTO.setName(watch.getName());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setId(watch.getId());

        return CartResponse.builder()
                .id(cart.getId())
                .user(userSimpleDTO)
                .watch(watchDTO)
                .quantity(cart.getQuantity())
                .createdAt(cart.getCreatedAt())
                .isActive(cart.getIsActive())
                .build();
    }
}


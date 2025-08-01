package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Cart;
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

    @JsonProperty("watch_id")
    private Long watchId;

    private Integer quantity;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    public static CartResponse fromCart(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .watchId(cart.getWatch().getId())
                .quantity(cart.getQuantity())
                .createdAt(cart.getCreatedAt())
                .isActive(cart.getIsActive())
                .build();
    }
}


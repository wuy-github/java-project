package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.project.futabuslines.models.Watch;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchUserViewResponse {
    private Long id;
    private String name;
    private double price;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("name_brand")
    private String nameBrand;

    @JsonProperty("name_category")
    private String nameCategory;

    @JsonProperty("is_favorite")
    private boolean isFavorite;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("seller_name")
    private String fullName;

    @JsonProperty("seller_image")
    private String userUrl;
    public static WatchUserViewResponse fromWatchView(Watch watch, boolean isFavorite, String imageUrl, String userUrl) {
        return WatchUserViewResponse.builder()
                .id(watch.getId())
                .name(watch.getName())
                .price(watch.getPrice())
                .imageUrl(imageUrl)
                .userUrl(userUrl)
                .fullName(watch.getUser().getFullName())
                .nameBrand(watch.getBrand() != null ? watch.getBrand().getName() : "")
                .nameCategory(watch.getCategory() != null ? watch.getCategory().getName() : "")
                .isFavorite(isFavorite)
                .updatedAt(watch.getUpdatedAt())
                .build();
    }
}


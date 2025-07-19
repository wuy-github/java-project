package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchDTO {
    private String name;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("brand_id")
    private Long brandId;

    @JsonProperty("category_id")
    private Long categoryId;

    private Integer price;

    private Integer quantity;
}

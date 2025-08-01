package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("watch_id")
    @Min(value = 1, message = "Product's ID must be >0")
    private Long watchId;

    @JsonProperty("quantity")
    @Min(value = 1, message = "Number os products must be >= 1")
    private int quantity;
}

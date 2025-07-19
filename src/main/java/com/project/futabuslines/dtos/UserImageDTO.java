package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserImageDTO {

    @JsonProperty("user_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long userId;

    @JsonProperty("image_url")
    @Size(min = 3, max = 200, message = "Image name must be between 3 and 200 characters")
    private String imageUrl;
}

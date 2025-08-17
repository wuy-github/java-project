package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("watch_id")
    private Long watchId;
}

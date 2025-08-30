package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDTO {

    @JsonProperty("watch_id")
    private Long watchId;

    private Integer rating;

    private String description;
    private String imageUrls;
    private List<MultipartFile> files;
}

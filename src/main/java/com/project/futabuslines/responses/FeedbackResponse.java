package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.dtos.UserSimpleDTO;
import com.project.futabuslines.dtos.WatchSimpleDTO;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class FeedbackResponse {
    private Long id;

    private UserSimpleDTO user;

    private WatchSimpleDTO watch;

    private Integer rating;

    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("update_at")
    private LocalDateTime updatedAt;

    public static FeedbackResponse fromFeedback(Feedback feedback){
        User user = feedback.getUser();
        UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
        userSimpleDTO.setId(user.getId());
        userSimpleDTO.setFullName(user.getFullName());
        userSimpleDTO.setPhoneNumber(user.getPhoneNumber());

        Watch watch = feedback.getWatch();
        WatchSimpleDTO watchDTO = new WatchSimpleDTO();
        watchDTO.setName(watch.getName());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setId(watch.getId());

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .user(userSimpleDTO)
                .watch(watchDTO)
                .rating(feedback.getRating())
                .description(feedback.getDescription())
                .imageUrl(feedback.getImageUrl())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdateAt())
                .build();
    }
}

package com.project.futabuslines.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "feedbacks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "watch_id"}, name = "uk_user_watch")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "watch"})
@EqualsAndHashCode(callSuper = false, of = "id")
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating must be no more than 5.")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Description cannot be empty.")
    @Column(length = 400, nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull(message = "Feedback must be associated with a user.")
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Feedback must be for a specific watch.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;
}
package com.project.futabuslines.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watch_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchImage {
    public static final int MAXIMUM_IMAGES_PER_WATCH = 9;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "watch_id")
    private Watch watch;

}

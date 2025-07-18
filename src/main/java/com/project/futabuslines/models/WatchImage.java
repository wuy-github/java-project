package com.project.futabuslines.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

public class WatchImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

}

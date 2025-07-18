package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watch_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    private String gender;

    private String shape;

    @Column(name = "movement_type")
    private String movementType;

    private String style;

    @Column(name = "glass_type")
    private String glassType;
}

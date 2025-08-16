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

    @OneToOne
    @JoinColumn(name = "watch_id", unique = true)
    private Watch watch;

    private String gender;

    private String shape;

    @Column(name = "movement_type")
    private String movementType;

    private String style;

    @Column(name = "glass_type")
    private String glassType;

    // Duong kinh
    private String diameter;

    // Chat lieu vo
    @Column(name = "case_material")
    private String caseMaterial;

    // Chat lieu day
    @Column(name = "band_material")
    private String bandMaterial;

    // Do chiu nuoc
    @Column(name = "water_resistance")
    private String waterResistance;

    // Tinh nang
    private String features;

    // Mau mat dong ho
    @Column(name = "dial_color")
    private String dialColor;

    // Xuat xu thuong hieu
    private String origin;

}

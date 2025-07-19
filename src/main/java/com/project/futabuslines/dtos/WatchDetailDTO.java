package com.project.futabuslines.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
public class WatchDetailDTO {
    @JsonProperty("watch_id")
    private Long watchId;

    // Dong ho Nam / Nu
    private String gender;

    // Kieu dang
    private String shape;

    // Loai may
    @JsonProperty("movement_type")
    private String movementType;

    // Phong cach
    private String style;

    // Mat kinh
    @JsonProperty("glass_type")
    private String glassType;

    // Duong kinh
    private String diameter;

    // Chat lieu vo
    @JsonProperty("case_material")
    private String caseMaterial;

    // Chat lieu day
    @JsonProperty("band_material")
    private String bandMaterial;

    // Do chiu nuoc
    @JsonProperty("water_resistance")
    private String waterResistance;

    // Tinh nang
    private String features;

    // Mau mat dong ho
    @JsonProperty("dial_color")
    private String dialColor;

    // Xuat xu thuong hieu
    private String origin;
}

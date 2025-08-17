package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.models.WatchDetail;
import com.project.futabuslines.models.WatchImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchDetailViewResponse {
    private Long id;
    private String name;
    private double price;

    @JsonProperty("image_url")
    private List<String> imageUrls;

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

    public static WatchDetailViewResponse fromWatchDetail(WatchDetail watchDetail){
        Watch watch = watchDetail.getWatch();

        List<String> imageUrls = watch.getImages() != null
                ? watch.getImages().stream()
                .map(WatchImage::getImageUrl)
                .toList()
                : List.of();

        return WatchDetailViewResponse.builder()
                .id(watchDetail.getId())
                .name(watch.getName())
                .price(watch.getPrice())
                .imageUrls(imageUrls)
                .watchId(watch.getId())
                .gender(watchDetail.getGender())
                .shape(watchDetail.getShape())
                .movementType(watchDetail.getMovementType())
                .style(watchDetail.getStyle())
                .glassType(watchDetail.getGlassType())
                .diameter(watchDetail.getDiameter())
                .caseMaterial(watchDetail.getCaseMaterial())
                .bandMaterial(watchDetail.getBandMaterial())
                .waterResistance(watchDetail.getWaterResistance())
                .features(watchDetail.getFeatures())
                .dialColor(watchDetail.getDialColor())
                .origin(watchDetail.getOrigin())
                .build();
    }
}

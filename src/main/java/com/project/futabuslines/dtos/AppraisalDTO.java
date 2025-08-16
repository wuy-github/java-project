package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
public class AppraisalDTO {
    @JsonProperty("watch_id")
    private Long watchId;

    @JsonProperty("user_id")
    private Long userId;

    // Gia tri tham dinh
    @JsonProperty("appraisal_value")
    private Integer appraisalValue;

    // Tinh xac thuc
    private boolean authenticity;

    @JsonProperty("watch_condition")
    private String watchCondition;

    // Bao cao tham dinh
    @JsonProperty("appraisal_report")
    private String appraisalReport;
}

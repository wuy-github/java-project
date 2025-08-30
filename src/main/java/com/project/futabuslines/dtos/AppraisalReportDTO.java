package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppraisalReportDTO {
    // Bao cao tham dinh
    @JsonProperty("appraisal_report")
    private String appraisalReport;
}

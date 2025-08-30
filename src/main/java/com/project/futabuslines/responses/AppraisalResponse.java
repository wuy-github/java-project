package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.dtos.AppraisalReportDTO;
import com.project.futabuslines.dtos.UserSimpleDTO;
import com.project.futabuslines.dtos.WatchSimpleDTO;
import com.project.futabuslines.models.Appraisal;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.Watch;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class AppraisalResponse {
    private Long id;

    private WatchSimpleDTO watch;

    private UserSimpleDTO user;

    @JsonProperty("appraisal_value")
    private Integer appraisalValue;

    private boolean authenticity;

    @JsonProperty("watch_condition")
    private String watchCondition;

    @JsonProperty("appraisal_report")
    private String appraisalReport;

    public static AppraisalResponse fromAppraisal(Appraisal appraisal){
        Watch watch = appraisal.getWatch();
        WatchSimpleDTO watchSimpleDTO = new WatchSimpleDTO();
        watchSimpleDTO.setId(watch.getId());
        watchSimpleDTO.setName(watch.getName());

        User user = appraisal.getUser();
        UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
        userSimpleDTO.setId(user.getId());
        userSimpleDTO.setFullName(user.getFullName());
        userSimpleDTO.setPhoneNumber(user.getPhoneNumber());
        return AppraisalResponse.builder()
                .id(appraisal.getId())
                .watch(watchSimpleDTO)
                .user(userSimpleDTO)
                .appraisalValue(appraisal.getAppraisalValue())
                .authenticity(appraisal.getAuthenticity() != null ? appraisal.getAuthenticity() : false)
                .watchCondition(appraisal.getWatchCondition())
                .appraisalReport(appraisal.getAppraisalReport())
                .build();
    }
}

package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppraisalDTO {
    @JsonProperty("watch_id")
//    @NotNull(message = "Watch's id cannot be null")
    private Long watchId;

    // Gia tri tham dinh
    @JsonProperty("appraisal_value")
    @NotNull(message = "Appraisal Value cannot be null")
    private Integer appraisalValue;

    // Tinh xac thuc
    private boolean authenticity;

    @JsonProperty("watch_condition")
    @NotBlank(message = "Watch condition cannot be blank")
    private String watchCondition;
}

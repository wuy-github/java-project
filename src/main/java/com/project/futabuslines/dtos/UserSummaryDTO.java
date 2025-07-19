package com.project.futabuslines.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserSummaryDTO {
    private Boolean registered;

    private Long userId;

    private String fullName;
}


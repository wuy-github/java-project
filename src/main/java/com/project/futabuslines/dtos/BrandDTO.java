package com.project.futabuslines.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // => have toString();
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {
    // Ten cua nhan hang khong duoc bo trong
    @NotEmpty(message = "Brand's name cannot be empty")
    private String name;
}

package com.project.futabuslines.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    // Ten cua danh muc khong duoc bo trong
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;
}

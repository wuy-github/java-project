package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than of equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id") // ANH XA TU CO SO DU LIEU
    private String categoryId;

    private List<MultipartFile> files;
}

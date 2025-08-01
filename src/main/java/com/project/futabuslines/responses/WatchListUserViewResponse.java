package com.project.futabuslines.responses;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.List;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class WatchListUserViewResponse {
    private List<WatchUserViewResponse>watch;
    private int totalPage;
}

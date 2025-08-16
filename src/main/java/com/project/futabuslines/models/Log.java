package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "target_table")
    private String targetTable;

    @Column(name = "target_id")
    private Integer targetId;

    @Column(name = "action_time")
    private LocalDateTime actionTime;

    private String details;
}

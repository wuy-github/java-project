package com.project.futabuslines.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appraisals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appraisal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Gia tri tham dinh
    @Column(name = "appraisal_value")
    private Integer appraisalValue;

    // Tinh xac thuc
    @Column(name = "authenticity")
    private Boolean authenticity;

    // Tinh trang
    @Column(name = "watch_condition")
    private String watchCondition;

    // Bao cao tham dinh
    @Column(name = "appraisal_report")
    private String appraisalReport;
}

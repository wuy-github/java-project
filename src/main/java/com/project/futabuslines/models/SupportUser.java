package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "support_user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;
}

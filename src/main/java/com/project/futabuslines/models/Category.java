package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Co the khong can thiet neu cung ten => Thua
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}
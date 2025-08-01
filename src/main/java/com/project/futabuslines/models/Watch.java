package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "watches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watch extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "price")
    private Integer price;

    private Integer quantity;

    @Column(name = "status")
    private String status; // Ben seller co the chinh sua, gom 2 trang thai la da ban, con hang

    @Column(name ="is_active")
    private boolean isActive; // Duoc duyet + con hang -> is active = 1  => duoc hien thi tren store
                             // Chua duoc duyet | het hang -> is active = 0 => khong duoc hien thi

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WatchImage> images;
}

package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "token_type", length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "is_mobile")
    private Boolean isMobile = false;

    @Column(name = "refresh_token")
    private String refreshToken = "";

    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

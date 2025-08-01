package com.project.futabuslines.repositories;

import com.project.futabuslines.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findTopByContactOrderByCreatedAtDesc(String contact);
    Optional<OTP> findByContactAndOtpCodeAndIsUsedFalseAndExpiresAtAfter(String contact, String otpCode, LocalDateTime now);
    List<OTP> findByIsValidTrueAndExpiresAtBefore(LocalDateTime time);

}

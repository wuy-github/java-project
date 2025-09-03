package com.project.futabuslines.services;

import com.project.futabuslines.dtos.UserSummaryDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.OTP;
import com.project.futabuslines.models.User;
import com.project.futabuslines.repositories.OTPRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.responses.OTPResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final OTPRepository otpRepository;
    private final SMSService smsService;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Scheduled(fixedRate = 60000)
    public void invalidateExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<OTP> expiredOtps = otpRepository.findByIsValidTrueAndExpiresAtBefore(now);
        for (OTP otp : expiredOtps) {
            otp.setIsValid(false);
        }
        otpRepository.saveAll(expiredOtps);
    }

    public String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public Object sendOtp(String contact) {
        Optional<User> existingUser = contact.contains("@")
                ? userRepository.findByEmail(contact)
                : userRepository.findByPhoneNumber(contact);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.getIsActive()) {
                throw new BadCredentialsException("Your account is deactivated. Please contact support.");
            }
        }
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return new UserSummaryDTO(true, user.getId(), user.getFullName());
        }
        if (contact.isEmpty()) {
            throw new IllegalArgumentException("Contact not found");
        }
        Optional<OTP> latestOtp = otpRepository.findTopByContactOrderByCreatedAtDesc(contact);
        if (latestOtp.isPresent() && latestOtp.get().getCreatedAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait before requesting a new OTP.");
        }

        String code = generateOtpCode();

        OTP otp = new OTP();
        otp.setOtpCode(code);
        otp.setContact(contact);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        otp.setIsUsed(false);
        otp.setIsValid(true);
        otpRepository.save(otp);

        if (contact.contains("@")) {
            // Email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(contact);
            message.setSubject("OTP Verification");
            message.setText("Mã OTP xác thực: " + code + "\nMã hết hạn sau 1 phút.");
            mailSender.send(message);
            System.out.println("📩 Sent OTP to email: " + contact + " - Code: " + code);
        } else {
            // Phone
            String phone = normalizePhoneNumber(contact);
            smsService.sendOtpViaSMS(phone);
        }

        return code;
    }

    public String sendOtpForResetPassword(String contact) throws DataNotFoundException {
        if (contact == null || contact.isEmpty()) {
            throw new IllegalArgumentException("❌ Contact không được để trống");
        }

        Optional<User> existingUser = contact.contains("@")
                ? userRepository.findByEmail(contact)
                : userRepository.findByPhoneNumber(contact);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (!user.getIsActive()) {
                throw new BadCredentialsException("Your account is deactivated. Please contact support.");
            }
        }
        if (existingUser.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy tài khoản với thông tin này.");
        }

        User user = existingUser.get();
        if (!user.getIsActive()) {
            throw new BadCredentialsException("Tài khoản đã bị vô hiệu hóa.");
        }

        Optional<OTP> latestOtp = otpRepository.findTopByContactOrderByCreatedAtDesc(contact);
        if (latestOtp.isPresent() && latestOtp.get().getCreatedAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait before requesting a new OTP.");
        }

        String code = generateOtpCode();

        OTP otp = new OTP();
        otp.setOtpCode(code);
        otp.setContact(contact);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        otp.setIsUsed(false);
        otp.setIsValid(true);
        otpRepository.save(otp);

        if (contact.contains("@")) {
            // Email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(contact);
            message.setSubject("OTP Verification");
            message.setText("Mã OTP xác thực : " + code + "\nMã hết hạn sau 1 phút.");
            mailSender.send(message);
            System.out.println("📩 Sent OTP to email: " + contact + " - Code: " + code);
        } else {
            // Phone
            String phone = normalizePhoneNumber(contact);
            smsService.sendOtpViaSMS(phone);
        }

        return code;
    }


    public OTPResponse verifyOtp(String contact, String code) {
        if (!contact.contains("@")) {
            String phone = normalizePhoneNumber(contact);
            boolean success = smsService.verifyOtpViaSMS(phone, code);
            return new OTPResponse(success);
        }

        Optional<OTP> otpOptional = otpRepository.findByContactAndOtpCodeAndIsUsedFalseAndExpiresAtAfter(contact, code, LocalDateTime.now());
        if (otpOptional.isEmpty()) return new OTPResponse(false);

        OTP otp = otpOptional.get();
        otp.setIsUsed(true);
        otpRepository.save(otp);
        return new OTPResponse(true);
    }


    public String normalizePhoneNumber(String phone) {
        if (phone.startsWith("0")) {
            return "+84" + phone.substring(1);
        }
        if (!phone.startsWith("+")) {
            return "+84" + phone;
        }
        return phone;
    }
}

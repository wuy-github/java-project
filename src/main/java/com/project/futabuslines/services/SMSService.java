package com.project.futabuslines.services;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${twilio.verify-service-sid}")
    private String VERIFY_SERVICE_SID;

    public void sendOtpViaSMS(String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(
                        VERIFY_SERVICE_SID,
                        phoneNumber,
                        "sms")
                .create();

        System.out.println("📲 SMS sent to " + phoneNumber + " - SID: " + verification.getSid());
    }

    public boolean verifyOtpViaSMS(String phoneNumber, String code) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID)
                .setTo(phoneNumber)
                .setCode(code)
                .create();

        return "approved".equals(verificationCheck.getStatus());
    }
}

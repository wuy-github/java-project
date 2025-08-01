package com.project.futabuslines.services;
import com.project.futabuslines.configurations.TwilioConfiguration;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioService {
    private final TwilioConfiguration twilioConfig;

    public void sendSms(String to, String content) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(twilioConfig.getFromNumber()),
                content
        ).create();
    }
}


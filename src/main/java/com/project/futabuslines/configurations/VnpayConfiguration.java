package com.project.futabuslines.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vnp")
@Data
public class VnpayConfiguration {
    private String tmnCode;
    private String hashSecret;
    private String payUrl;
    private String returnUrl;
}

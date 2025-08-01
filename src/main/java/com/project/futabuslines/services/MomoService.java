// src/main/java/com/project/futabuslines/services/MomoService.java
package com.project.futabuslines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MomoService {
    @Value("${momo.payment.dev.partner-code}")
    private String momoPartnerCode;
    @Value("${momo.payment.dev.access-key}")
    private String momoAccessKey;
    @Value("${momo.payment.dev.secret-key}")
    private String momoSecretKey;
    @Value("${momo.payment.dev.redirect-url}")
    private String momoReturnUrl;
    @Value("${momo.payment.dev.notify-url}")
    private String momoNotifyUrl;


    private final String MOMO_API_URL = "https://test-payment.momo.vn/v2/gateway/api/create";

    private final RestTemplate restTemplate = new RestTemplate();

    public String createPaymentUrl(String orderId, int amount, String ipAddress) {
        try {
            String requestId = UUID.randomUUID().toString();
            String orderInfo = "Thanh toan ve xe VIWAYBUS";
            String requestType = "captureWallet";
            String extraData = "";

            Map<String, String> rawData = new TreeMap<>();
            rawData.put("accessKey", momoAccessKey);
            rawData.put("amount", String.valueOf(amount));
            rawData.put("extraData", extraData);
            rawData.put("ipnUrl", momoNotifyUrl);
            rawData.put("orderId", orderId);
            rawData.put("orderInfo", orderInfo);
            rawData.put("partnerCode", momoPartnerCode);
            rawData.put("redirectUrl", momoReturnUrl);
            rawData.put("requestId", requestId);
            rawData.put("requestType", requestType);

            String rawHash = rawData.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            String signature = hmacSHA256(rawHash, momoSecretKey);

            Map<String, Object> requestBody = new LinkedHashMap<>(rawData);
            requestBody.put("signature", signature);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(requestBody), headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(MOMO_API_URL, entity, Map.class);
            System.out.println("MoMo response: " + new ObjectMapper().writeValueAsString(response.getBody()));

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("payUrl");
            } else {
                throw new RuntimeException("MoMo response error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo payment URL MoMo", e);
        }
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}

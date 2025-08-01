package com.project.futabuslines.services;

import com.project.futabuslines.configurations.VnpayConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VnpayService {

    private final VnpayConfiguration config;

    /**
     * Create payment URL - FIXED VERSION
     */
    public String createPaymentUrl(String orderId, long amount, String ipAddress, String orderInfo) {
        if (!StringUtils.hasText(orderId) || amount <= 0 || !StringUtils.hasText(ipAddress)) {
            throw new IllegalArgumentException("Invalid parameters for payment URL creation");
        }

        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TmnCode = config.getTmnCode();
            String vnp_Amount = String.valueOf(amount * 100);
            String vnp_CurrCode = "VND";
            String vnp_TxnRef = orderId;
            String vnp_OrderInfo = orderInfo != null ? orderInfo : "Thanh toan don " + orderId;
            String vnp_OrderType = "other";
            String vnp_Locale = "vn";
            String vnp_ReturnUrl = config.getReturnUrl();
            String vnp_IpAddr = ipAddress;

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", vnp_OrderType);
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Build query string
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    // Build query string
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(config.getHashSecret(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = config.getPayUrl() + "?" + queryUrl;

            log.info("Created payment URL for order: {}, amount: {}", orderId, amount);
            log.debug("Hash data: {}", hashData.toString());
            log.debug("Generated hash: {}", vnp_SecureHash);

            return paymentUrl;

        } catch (Exception e) {
            log.error("Error creating payment URL for order: {}", orderId, e);
            throw new RuntimeException("Failed to create payment URL", e);
        }
    }

    public String createPaymentUrl(String orderId, long amount, String ipAddress) {
        return createPaymentUrl(orderId, amount, ipAddress, "Thanh toan ve xe bus " + orderId);
    }

    /**
     * Verify payment return - FIXED VERSION
     */
    public PaymentResult verifyReturn(HttpServletRequest request) {
        try {
            log.info("=== Starting Payment Verification ===");

            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            log.debug("All received parameters:");
            fields.forEach((key, value) -> log.debug("  {}: {}", key, value));

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
                log.warn("Missing vnp_SecureHash parameter");
                return PaymentResult.MISSING_HASH;
            }

            log.debug("Received hash: {}", vnp_SecureHash);

            // Remove hash from fields for verification
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            // Build hash data for verification
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = fields.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    try {
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    } catch (UnsupportedEncodingException e) {
                        log.error("Error encoding field value", e);
                        return PaymentResult.ERROR;
                    }
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }

            String expectedHash = hmacSHA512(config.getHashSecret(), hashData.toString());

            log.debug("Hash data for verification: {}", hashData.toString());
            log.debug("Expected hash: {}", expectedHash);
            log.debug("Received hash: {}", vnp_SecureHash);
            log.debug("Hash match: {}", expectedHash.equals(vnp_SecureHash));

            if (!expectedHash.equals(vnp_SecureHash)) {
                log.error("HASH VERIFICATION FAILED!");
                log.error("Expected: {}", expectedHash);
                log.error("Received: {}", vnp_SecureHash);

                // Try alternative hash calculation (without URL encoding)
                StringBuilder alternativeHashData = new StringBuilder();
                Iterator<String> altItr = fieldNames.iterator();
                while (altItr.hasNext()) {
                    String fieldName = altItr.next();
                    String fieldValue = fields.get(fieldName);
                    if ((fieldValue != null) && (fieldValue.length() > 0)) {
                        alternativeHashData.append(fieldName);
                        alternativeHashData.append('=');
                        alternativeHashData.append(fieldValue); // No URL encoding
                        if (altItr.hasNext()) {
                            alternativeHashData.append('&');
                        }
                    }
                }

                String alternativeHash = hmacSHA512(config.getHashSecret(), alternativeHashData.toString());
                log.debug("Alternative hash data: {}", alternativeHashData.toString());
                log.debug("Alternative hash: {}", alternativeHash);

                if (!alternativeHash.equals(vnp_SecureHash)) {
                    return PaymentResult.HASH_MISMATCH;
                } else {
                    log.info("Alternative hash verification successful");
                }
            }

            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");

            log.info("Payment verification completed for transaction: {}, responseCode: {}", vnp_TxnRef, vnp_ResponseCode);

            if ("00".equals(vnp_ResponseCode)) {
                return PaymentResult.SUCCESS;
            } else {
                return PaymentResult.FAILED;
            }

        } catch (Exception e) {
            log.error("Error verifying payment return", e);
            return PaymentResult.ERROR;
        }
    }

    /**
     * Alternative verification using TreeMap for automatic sorting
     */
    public PaymentResult verifyReturnV2(HttpServletRequest request) {
        try {
            Map<String, String> vnpParams = new TreeMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    vnpParams.put(fieldName, fieldValue);
                }
            }

            String vnpSecureHash = vnpParams.remove("vnp_SecureHash");
            vnpParams.remove("vnp_SecureHashType");

            if (vnpSecureHash == null || vnpSecureHash.isEmpty()) {
                return PaymentResult.MISSING_HASH;
            }

            StringBuilder hashData = new StringBuilder();
            vnpParams.entrySet().forEach(entry -> {
                hashData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            });

            // Remove last &
            if (hashData.length() > 0) {
                hashData.setLength(hashData.length() - 1);
            }

            String expectedHash = hmacSHA512(config.getHashSecret(), hashData.toString());

            log.debug("V2 Hash data: {}", hashData.toString());
            log.debug("V2 Expected: {}", expectedHash);
            log.debug("V2 Received: {}", vnpSecureHash);

            if (!expectedHash.equals(vnpSecureHash)) {
                return PaymentResult.HASH_MISMATCH;
            }

            return "00".equals(vnpParams.get("vnp_ResponseCode")) ? PaymentResult.SUCCESS : PaymentResult.FAILED;
        } catch (Exception e) {
            log.error("Error in V2 verification", e);
            return PaymentResult.ERROR;
        }
    }

    public PaymentDetails getPaymentDetails(HttpServletRequest request) {
        return PaymentDetails.builder()
                .orderId(request.getParameter("vnp_TxnRef"))
                .amount(parseAmount(request.getParameter("vnp_Amount")))
                .responseCode(request.getParameter("vnp_ResponseCode"))
                .transactionNo(request.getParameter("vnp_TransactionNo"))
                .bankCode(request.getParameter("vnp_BankCode"))
                .payDate(request.getParameter("vnp_PayDate"))
                .build();
    }

    private long parseAmount(String amountStr) {
        try {
            return StringUtils.hasText(amountStr) ? Long.parseLong(amountStr) / 100 : 0;
        } catch (NumberFormatException e) {
            log.warn("Invalid amount format: {}", amountStr);
            return 0;
        }
    }

    private String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key or data is null");
            }

            Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);

            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);

            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Error generating HMAC-SHA512", ex);
            throw new RuntimeException("Error generating hash", ex);
        }
    }

    // Enums and DTOs
    public enum PaymentResult {
        SUCCESS("Payment successful"),
        FAILED("Payment failed"),
        HASH_MISMATCH("Hash verification failed"),
        MISSING_HASH("Missing security hash"),
        INVALID_PARAMS("Invalid parameters"),
        ERROR("System error");

        private final String description;

        PaymentResult(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class PaymentDetails {
        private String orderId;
        private long amount;
        private String responseCode;
        private String transactionNo;
        private String bankCode;
        private String payDate;
    }
}
package com.project.futabuslines.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRedirectResponse {
    private String paymentUrl;
    private String message;
}


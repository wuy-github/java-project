package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min =10, message = "Phone number must be at least 10 characters")
    private String phoneNumber;

    private String address;

    private String note;

    private String status;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private int totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @NotNull(message = "Order details are required")
    @Size(min = 1, message = "Must have at least 1 order item")
    private List<OrderDetailDTO> orderDetails;

}

package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Order;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class OrderResponse{
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("order_date")
    private Date orderDate;

    private String status;

    @JsonProperty("total_money")
    private Integer totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;
    
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private Boolean active;

    private List<OrderDetailResponse> orderDetails;

    @JsonProperty("payment_time")
    private LocalDateTime updatedAt;

    public static OrderResponse fromOrder(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .shippingDate(order.getShippingDate())
                .shippingAddress(order.getShippingAddress())
                .trackingNumber(order.getTrackingNumber())
                .paymentMethod(order.getPaymentMethod())
                .active(order.getActive())
                .orderDetails(
                        order.getOrderDetails() != null
                                ? order.getOrderDetails().stream()
                                .map(OrderDetailResponse::fromOrderDetail)
                                .toList()
                                : Collections.emptyList()
                )
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

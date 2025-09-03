package com.project.futabuslines.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.OrderDetail;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Builder
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("watch_id")
    private Long watchId;

    private Integer price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private Integer totalMoney;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .watchId(orderDetail.getWatch().getId())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}

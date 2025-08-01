package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.models.OrderDetail;
import com.project.futabuslines.responses.OrderDetailResponse;
import com.project.futabuslines.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders_details")
//@Validated
@RequiredArgsConstructor
public class OrderDetailsController {
    private final OrderDetailService orderDetailService;

    // POST: http://localhost:8088/api/v1/orders_details
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO newOrderDetailDTO
    ){
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(newOrderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // GET: http://localhost:8088/api/v1/orders_details/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: http://localhost:8088/api/v1/orders_details/order/{orderID}
    @GetMapping("/order/{orderId}")
    // Danh sách order details của một order nào đó
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
//        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    // PUT: http://localhost:8088/api/v1/orders_details/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
             @RequestBody OrderDetailDTO newOrderDetailData
    ){
        try {OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, newOrderDetailData);
            return ResponseEntity.ok().body(orderDetail);
//            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: http://localhost:8088/api/v1/orders_details/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("Delete Order detail with id: " + id + " successfully.");
    }


}

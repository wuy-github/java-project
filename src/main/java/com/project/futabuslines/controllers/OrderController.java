package com.project.futabuslines.controllers;

import com.project.futabuslines.components.AuthUtil;
import com.project.futabuslines.components.ValidationUtil;
import com.project.futabuslines.dtos.OrderDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.models.Order;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.responses.OrderResponse;
import com.project.futabuslines.services.IOrderService;
import com.project.futabuslines.services.MomoService;
import com.project.futabuslines.services.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/orders")
//@Validated
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final VnpayService vnpayService;
    private final MomoService momoService;
    private final ValidationUtil validationUtil;
    private final AuthUtil authUtil;

    // POST: http://localhost:8088/api/v1/orders/create
    @PostMapping("create")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result,
            @RequestHeader(value = "Authorization", required = false) String token
    ) throws Exception {
        if (validationUtil.hasErrors(result)) {
            return ResponseEntity.badRequest().body(validationUtil.getErrorMessages(result));
        }
        Long userId = authUtil.extractUserIdFromToken(token);
        OrderResponse orderResponse = orderService.createOrder(orderDTO, orderDTO.getOrderDetails(),userId);
        URI location = URI.create("/api/v1/appraisal/" + orderResponse.getId());
        return ResponseEntity.created(location).body(orderResponse);
    }

    // GET: http://localhost:8088/api/v1/orders/{id}
    // Lay chi tiet Order
    @GetMapping("get/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try {
            OrderResponse existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: http://localhost:8088/api/v1/orders/user/{userID}
    @GetMapping("user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try {
            List<OrderResponse> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: http://localhost:8088/api/v1/orders/get-all
    @GetMapping("get-all")
    public ResponseEntity<?> getAll(){
        try{
            List<OrderResponse> orders = orderService.findAll();
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-page")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, limit); // page start from 0
            Page<OrderResponse> watchPage = orderService.getAll(pageRequest);

            return ResponseEntity.ok(
                    Map.of(
                            "watches", watchPage.getContent(),
                            "totalPage", watchPage.getTotalPages()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT: http://localhost:8088/api/v1/orders/{id}
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
            @RequestHeader(value = "Authorization", required = false) String token
    ){

        try {
            Long userId = authUtil.extractUserIdFromToken(token);
            OrderResponse order = orderService.updateOrder(id, orderDTO, userId);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: http://localhost:8088/api/v1/orders/{id}
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id
    ){
        // Xoa mem => Cap nhat truong active = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }

    @PostMapping("/payment")
        public ResponseEntity<?> paymentTicket(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        try {
            Object result = orderService.paymentOrder(paymentDTO, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> handlePaymentReturn(HttpServletRequest request) throws Exception {
        VnpayService.PaymentResult result = vnpayService.verifyReturn(request);
        VnpayService.PaymentDetails details = vnpayService.getPaymentDetails(request);
        String orderId = details.getOrderId();

        if (result == VnpayService.PaymentResult.SUCCESS) {
            orderService.successPayment(orderId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:5173/payment-result?status=success&orderId=" + orderId)
                    .build();
        } else {
            orderService.cancelPayment(orderId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "http://localhost:5173/payment-result?status=fail&orderId=" + orderId)
                    .build();
        }
    }

    @GetMapping("/momo/callback")
    public ResponseEntity<Void> handleMomoCallback(@RequestParam Map<String, String> params) throws Exception {
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");

        try {
            if ("0".equals(resultCode)) {
                orderService.successPayment(orderId);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "http://localhost:5173/payment-result?status=success&orderId=" + orderId)
                        .build();
            } else {
                orderService.cancelPayment(orderId);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", "http://localhost:5173/payment-result?status=fail&orderId=" + orderId)
                        .build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

package com.project.futabuslines.services;

import com.project.futabuslines.dtos.OrderDTO;
import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Order;
import com.project.futabuslines.responses.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails) throws Exception;
    Order getOrder(Long id);
    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
    Object paymentOrder(PaymentDTO paymentDTO, HttpServletRequest request);
    void cancelPayment(String id) throws DataNotFoundException;
    void successPayment(String id) throws DataNotFoundException;
}

package com.project.futabuslines.services;

import com.project.futabuslines.dtos.OrderDTO;
import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Order;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.responses.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails, Long userId) throws Exception;
    OrderResponse getOrder(Long id) throws DataNotFoundException;
    OrderResponse updateOrder(Long id, OrderDTO orderDTO, Long userId) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<OrderResponse> findByUserId(Long userId);
    List<OrderResponse> findAll();
    Page<OrderResponse> getAll(PageRequest pageRequest);
    Object paymentOrder(PaymentDTO paymentDTO, HttpServletRequest request) throws DataNotFoundException;
    void cancelPayment(String id) throws DataNotFoundException;
    void successPayment(String id) throws DataNotFoundException;
}

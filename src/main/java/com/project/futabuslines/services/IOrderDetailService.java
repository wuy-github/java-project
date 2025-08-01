package com.project.futabuslines.services;

import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.OrderDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;
    void deleteOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);

}

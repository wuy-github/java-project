        package com.project.futabuslines.services;

import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Order;
import com.project.futabuslines.models.OrderDetail;
import com.project.futabuslines.models.Watch;
import com.project.futabuslines.repositories.OrderDetailRepository;
import com.project.futabuslines.repositories.OrderRepository;
import com.project.futabuslines.repositories.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WatchRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {
//        Order order = orderRepository.findById(newOrderDetail.getOrderId())
//                .orElseThrow(()->new DataNotFoundException("Cannot find Order with id: " + newOrderDetail.getOrderId()));
//        Watch product = productRepository.findById(newOrderDetail.getProductId())
//                .orElseThrow(()->new DataNotFoundException("Cannot find Order with id: " + newOrderDetail.getProductId()));
//        OrderDetail orderDetail = OrderDetail.builder()
//                .order(order)
//                .watch(product)
//                .numberOfProducts(newOrderDetail.getNumberOfProducts())
//                .totalMoney(newOrderDetail.getTotalMoney())
//                .price(newOrderDetail.getPrice())
//                .build();
//
//        return orderDetailRepository.save(orderDetail);
        return null;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetail with id: "+ id));
    }

    @Override
    public OrderDetail updateOrderDetail(
            Long id,
            OrderDetailDTO newOrderDetailData
    ) throws DataNotFoundException {
        // Tim order details co ton tai khong
//        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
//                .orElseThrow(
//                        ()->new DataNotFoundException("Cannot find Order Detail with id: "+id));
//        Order existingOrder = orderRepository.findById(newOrderDetailData.getOrderId())
//                .orElseThrow(
//                        ()->new DataNotFoundException("Cannot find Order with id: "+id));
//        Watch existingProduct = productRepository.findById(newOrderDetailData.getProductId())
//                .orElseThrow(
//                        ()->new DataNotFoundException("Cannot find Order with id: " + newOrderDetailData.getProductId()));
//        existingOrderDetail.setPrice(newOrderDetailData.getPrice());
//        existingOrderDetail.setTotalMoney(newOrderDetailData.getTotalMoney());
//        existingOrderDetail.setNumberOfProducts(newOrderDetailData.getNumberOfProducts());
//        existingOrderDetail.setOrder(existingOrder);
//        existingOrderDetail.setWatch(existingProduct);
//
//        return orderDetailRepository.save(existingOrderDetail);
        return null;
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}

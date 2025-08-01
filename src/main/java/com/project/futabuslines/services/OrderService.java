package com.project.futabuslines.services;

import com.project.futabuslines.dtos.OrderDTO;
import com.project.futabuslines.dtos.OrderDetailDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.enums.WatchStatus;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.OrderDetailRepository;
import com.project.futabuslines.repositories.OrderRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.repositories.WatchRepository;
import com.project.futabuslines.responses.OrderResponse;
import com.project.futabuslines.responses.PaymentRedirectResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WatchRepository watchRepository;

    private final EntityFinder entityFinder;
    private final ModelMapper modelMapper;
    private final VnpayService vnpayService;
    private final MomoService momoService;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails) throws Exception {
        User user = entityFinder.findUserById(orderDTO.getUserId());

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);

        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be today or later!");
        }
        order.setShippingDate(shippingDate);
        // Luu de tao order Id
        orderRepository.save(order);


        int total = 0;
        List<OrderDetail> orderDetailEntities = new ArrayList<>();

        for (OrderDetailDTO detailDTO : orderDetails) {
            Watch watch = entityFinder.findWatchById(detailDTO.getWatchId());

            if (detailDTO.getQuantity() > watch.getQuantity()) {
                throw new DataNotFoundException(
                        "Not enough quantity for watch with ID " + watch.getId() + ". Only " + watch.getQuantity() + " left."
                );
            }
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setWatch(watch);
            detail.setPrice(watch.getPrice());
            detail.setQuantity(detailDTO.getQuantity());

            int totalMoneyForWatch = watch.getPrice() * detailDTO.getQuantity();
//            detail.setTotalMoney(totalMoneyForWatch);

            total += totalMoneyForWatch;
            orderDetailEntities.add(detail);

            int remainingQuantity = watch.getQuantity() - detailDTO.getQuantity();
            watch.setQuantity(remainingQuantity);
            if (remainingQuantity == 0) {
                watch.setStatus(String.valueOf(WatchStatus.SOLD_OUT)); // enum ProductStatus phải có SOLD_OUT
            }
            watchRepository.saveAll(
                    orderDetailEntities.stream().map(OrderDetail::getWatch).collect(Collectors.toList())
            );

        }

        order.setTotalMoney(total);

        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetailEntities);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }


    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElse(null);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find order with id: "+ id));
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException("Cannot find user with id: "+ orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        orderRepository.save(order);
        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // No hard-delete => Pls soft-delete
        if(order != null){
//            orderRepository.delete(optionalOrder.get());
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Object paymentOrder(PaymentDTO paymentDTO, HttpServletRequest request) {
        String clientIp = getClientIp(request);

        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé với ID đã cung cấp"));

        String orderId = "ORDER_" + order.getId() + "_" + System.currentTimeMillis();

        long totalMoney = order.getTotalMoney(); // giả sử trả về long
        int amount = (int) totalMoney; // Đơn vị: đồng

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
            String paymentUrl = vnpayService.createPaymentUrl(orderId, amount, clientIp);
            return new PaymentRedirectResponse(paymentUrl, "Vui lòng truy cập đường dẫn VNPAY để thanh toán.");
        }

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("MOMO")) {
            String paymentUrl = momoService.createPaymentUrl(orderId, amount, clientIp);
            return new PaymentRedirectResponse(paymentUrl, "Vui lòng truy cập đường dẫn MoMo để thanh toán.");
        }

        return order;
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
    }

    @Override
    public void successPayment(String id) throws DataNotFoundException {
        Long orderId = extractOrderId(id);
        Order order = entityFinder.findOrderById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }

    @Override
    public void cancelPayment(String id) throws DataNotFoundException {
        Long orderId = extractOrderId(id);
        Order order = entityFinder.findOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private Long extractOrderId(String id) {
        // id có dạng "ORDER_6_1753460744037"
        // Giả sử phần sau "ORDER_" là ID thật
        try {
            String[] parts = id.split("_");
            return Long.parseLong(parts[1]); // ví dụ: "6"
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid order ID format: " + id);
        }
    }
}

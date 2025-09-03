package com.project.futabuslines.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    public OrderResponse createOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails, Long userId) throws Exception {
        User user = entityFinder.findUserById(userId);

        // Map Order từ DTO
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);
        String trackingNumber = generateUniqueTicketCode();
        order.setTrackingNumber(trackingNumber);


        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be today or later!");
        }
        order.setShippingDate(shippingDate);

        // Xử lý chi tiết đơn hàng
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
            detail.setOrder(order);  // liên kết 2 chiều
            detail.setWatch(watch);
            detail.setPrice(watch.getPrice());
            detail.setQuantity(detailDTO.getQuantity());

            int totalMoneyForWatch = watch.getPrice() * detailDTO.getQuantity();
            detail.setTotalMoney(totalMoneyForWatch);

            total += totalMoneyForWatch;
            orderDetailEntities.add(detail);

            // Trừ số lượng hàng
            int remainingQuantity = watch.getQuantity() - detailDTO.getQuantity();
            watch.setQuantity(remainingQuantity);
            if (remainingQuantity == 0) {
                watch.setStatus(WatchStatus.SOLD_OUT.getValue());
            }
        }

        order.setTotalMoney(total);
        order.setOrderDetails(orderDetailEntities); // gắn list detail vào order
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromOrder(savedOrder);
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {
        Order order = entityFinder.findOrderById(id);
        return OrderResponse.fromOrder(order);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO, Long userId) throws DataNotFoundException {
        Order order = entityFinder.findOrderById(id);
        User existingUser = entityFinder.findUserById(userId);

        if (orderDTO.getFullName() != null) order.setFullName(orderDTO.getFullName());
        if (orderDTO.getPhoneNumber() != null) order.setPhoneNumber(orderDTO.getPhoneNumber());
        if (orderDTO.getAddress() != null) order.setAddress(orderDTO.getAddress());
        if (orderDTO.getNote() != null) order.setNote(orderDTO.getNote());
        if (orderDTO.getShippingMethod() != null) order.setShippingMethod(orderDTO.getShippingMethod());
        if (orderDTO.getShippingAddress() != null) order.setShippingAddress(orderDTO.getShippingAddress());
        if (orderDTO.getShippingDate() != null) order.setShippingDate(orderDTO.getShippingDate());
        if (orderDTO.getPaymentMethod() != null) order.setPaymentMethod(orderDTO.getPaymentMethod());
        if(orderDTO.getStatus() != null) order.setStatus(orderDTO.getStatus());
        order.setUser(existingUser);

        orderRepository.save(order);
        return OrderResponse.fromOrder(order);
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
    public List<OrderResponse> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> findAll(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> getAll(PageRequest pageRequest) {
        Page<Order> orders = orderRepository.findAll(pageRequest);
        return orders.map(OrderResponse::fromOrder);
    }


    @Override
    public Object paymentOrder(PaymentDTO paymentDTO, HttpServletRequest request) throws DataNotFoundException {
        String clientIp = getClientIp(request);
        Order order = entityFinder.findOrderById(paymentDTO.getOrderId());

        String orderId = "ORDER_" + order.getId() + "_" + System.currentTimeMillis();

        long totalMoney = order.getTotalMoney();
        int amount = (int) totalMoney;

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
            order.setPaymentMethod("VN-PAY");
            String paymentUrl = vnpayService.createPaymentUrl(orderId, amount, clientIp);
            return new PaymentRedirectResponse(paymentUrl, "Vui lòng truy cập đường dẫn VNPAY để thanh toán.");
        }

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("MOMO")) {
            String paymentUrl = momoService.createPaymentUrl(orderId, amount, clientIp);
            order.setPaymentMethod("MOMO");
            return new PaymentRedirectResponse(paymentUrl, "Vui lòng truy cập đường dẫn MoMo để thanh toán.");
        }
        orderRepository.save(order);
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

    private String generateUniqueTicketCode() {
        return "TIMEPIECE_" + generateRandomAlphaNumeric(6); // 6 ký tự chữ và số
    }

    private String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    private String generateQRCodeBase64(String content) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", outputStream);
        byte[] qrBytes = outputStream.toByteArray();

        // Add this line: prefix required for rendering in HTML <img src="...">
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
    }
}

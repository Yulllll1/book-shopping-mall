package io.elice.shoppingmall.order.service;

import io.elice.shoppingmall.order.DTO.OrderDTO;
import io.elice.shoppingmall.order.entity.Orders;
import io.elice.shoppingmall.order.mapper.OrderMapper;
import io.elice.shoppingmall.order.repository.OrderRepository;
import io.elice.shoppingmall.user.entity.User;
import io.elice.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Transactional //주문 생성
    public Orders createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + orderDTO.getUserId()));
        Orders order = OrderMapper.INSTANCE.toOrderEntity(orderDTO, user);
        return orderRepository.save(order);
    }

    public OrderDTO getOrderById(Long id) { //관리자 - 주문 아이디로 조회
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.getOrderLine().forEach(line -> line.getOrderLineBooks().size());
        return orderMapper.toOrderDTO(order);
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) { //관리자 - 모든 주문조회
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderDTO);
    }

    public Page<OrderDTO> getOrdersByUserId(Long userId, Pageable pageable) { //사용자 - 사용자 아이디별 주문 조회
        Page<Orders> orders = orderRepository.findByUserId(userId, pageable);
        orders.getContent().forEach(order -> order.getOrderLine().forEach(line -> line.getOrderLineBooks().size()));
        return orders.map(orderMapper::toOrderDTO);
    }

    public OrderDTO getOrderDetails(Long orderId) { // 관리자,사용자 - 주문 아이디로 주문 상세 조회
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.getOrderLine().forEach(line -> line.getOrderLineBooks().size());
        return orderMapper.toOrderDTO(order);
    }

    @Transactional
    public Orders updateOrder(Long id, OrderDTO orderDTO) { //관리자, 사용자 - 주문 수정
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        orderMapper.updateOrderFromDTO(orderDTO, order);
        return orderRepository.save(order);
    }

    @Transactional //관리자, 사용자 - 주문 삭제
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
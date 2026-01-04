package com.nizami.homekitchen.service;

import com.nizami.homekitchen.dto.OrderRequestDTO;
import com.nizami.homekitchen.dto.OrderResponseDTO;
import com.nizami.homekitchen.mapper.OrderMapper;
import com.nizami.homekitchen.model.Order;
import com.nizami.homekitchen.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    // =========================
    // GET
    // =========================
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDTO);
    }


    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return orderMapper.toResponseDTO(order);
    }

    // =========================
    // CREATE
    // =========================
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        Order order = new Order();
        orderMapper.updateOrderFromDTO(order, dto);
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    @Transactional
    public List<OrderResponseDTO> createMultipleOrders(List<OrderRequestDTO> dtos) {
        List<Order> orders = dtos.stream()
                .map(dto -> {
                    Order order = new Order();
                    orderMapper.updateOrderFromDTO(order, dto);
                    return order;
                })
                .toList();

        return orderRepository.saveAll(orders).stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        orderMapper.updateOrderFromDTO(order, dto);
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    // =========================
    // PATCH
    // =========================
    @Transactional
    public OrderResponseDTO patchOrder(Long id, OrderRequestDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        orderMapper.patchOrderFromDTO(order, dto);
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}

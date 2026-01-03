package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.OrderRequestDTO;
import com.nizami.homekitchen.dto.OrderResponseDTO;
import com.nizami.homekitchen.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // =========================
    // GET
    // =========================
    @GetMapping
    public Page<OrderResponseDTO> getAllOrders(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }


    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody @Valid OrderRequestDTO dto) {
        return orderService.createOrder(dto);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<OrderResponseDTO> createMultipleOrders(@RequestBody @Valid List<OrderRequestDTO> dtos) {
        return orderService.createMultipleOrders(dtos);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequestDTO dto) {
        return orderService.updateOrder(id, dto);
    }

    @PatchMapping("/{id}")
    public OrderResponseDTO patchOrder(@PathVariable Long id, @RequestBody OrderRequestDTO dto) {
        return orderService.patchOrder(id, dto);
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllOrders() {
        orderService.deleteAllOrders();
    }
}

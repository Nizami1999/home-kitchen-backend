package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.dto.OrderRequestDTO;
import com.nizami.homekitchen.dto.OrderResponseDTO;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.model.Order;
import com.nizami.homekitchen.repository.DishRepository;
import com.nizami.homekitchen.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    // GET all orders
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // GET single order by ID
    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return mapToResponseDTO(order);
    }

    // POST create a new order
    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody @Valid OrderRequestDTO requestDTO) {
        Dish dish = dishRepository.findById(requestDTO.getDishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));

        Order order = new Order();
        order.setCustomerName(requestDTO.getCustomerName());
        order.setCustomerAddress(requestDTO.getCustomerAddress());
        order.setQuantity(requestDTO.getQuantity());
        order.setDish(dish);

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    // PUT update entire order
    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequestDTO requestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        Dish dish = dishRepository.findById(requestDTO.getDishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid dish ID"));

        order.setCustomerName(requestDTO.getCustomerName());
        order.setCustomerAddress(requestDTO.getCustomerAddress());
        order.setQuantity(requestDTO.getQuantity());
        order.setDish(dish);

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    // PATCH partial update of order
    @PatchMapping("/{id}")
    public OrderResponseDTO patchOrder(@PathVariable Long id, @RequestBody OrderRequestDTO requestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (requestDTO.getCustomerName() != null) order.setCustomerName(requestDTO.getCustomerName());
        if (requestDTO.getCustomerAddress() != null) order.setCustomerAddress(requestDTO.getCustomerAddress());
        if (requestDTO.getQuantity() != null) order.setQuantity(requestDTO.getQuantity());
        if (requestDTO.getDishId() != null) {
            Dish dish = dishRepository.findById(requestDTO.getDishId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid dish ID"));
            order.setDish(dish);
        }

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    // DELETE order
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
    }

    // Utility method to map Order entity to response DTO
    private OrderResponseDTO mapToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerAddress(),
                order.getQuantity(),
                new DishResponseDTO(
                        order.getDish().getId(),
                        order.getDish().getName(),
                        order.getDish().getDescription(),
                        order.getDish().getPrice(),
                        order.getDish().getImageUrl(),
                        new CategoryResponseDTO(
                                order.getDish().getCategory().getId(),
                                order.getDish().getCategory().getName()
                        )
                ),
                order.getOrderDate()
        );
    }
}

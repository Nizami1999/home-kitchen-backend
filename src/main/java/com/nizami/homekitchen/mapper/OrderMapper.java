package com.nizami.homekitchen.mapper;

import com.nizami.homekitchen.dto.*;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.model.Order;
import com.nizami.homekitchen.model.OrderItem;
import com.nizami.homekitchen.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final DishRepository dishRepository;

    public OrderMapper(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    // =========================
    // ORDER → ORDER RESPONSE DTO
    // =========================
    public OrderResponseDTO toResponseDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(this::toOrderItemResponseDTO)
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerAddress(),
                order.getCustomerPhone(),
                order.getCustomerEmail(),
                order.getDiscountApplied(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getDeliveryTime(),
                order.getSpecialInstructions(),
                order.getIsActive(),
                order.getDishSnapshot(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    // =========================
    // ORDER ITEM → ORDER ITEM RESPONSE DTO
    // =========================
    private OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getId(),
                item.getDish().getId(),
                item.getDish().getName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }

    // =========================
    // CREATE / UPDATE ORDER FROM DTO (PUT)
    // =========================
    public void updateOrderFromDTO(Order order, OrderRequestDTO dto) {
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerAddress(dto.getCustomerAddress());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setCustomerEmail(dto.getCustomerEmail());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setDeliveryTime(dto.getDeliveryTime());
        order.setSpecialInstructions(dto.getSpecialInstructions());
        order.setDiscountApplied(dto.getDiscountApplied() != null ? dto.getDiscountApplied() : 0.0);

        // Clear existing items and add new items from request + Update dish snapshot
        StringJoiner snapshotJoiner = new StringJoiner(", ");
        List<OrderItem> items = dto.getItems().stream()
                .map(itemDTO -> {
                    Dish dish = dishRepository.findById(itemDTO.getDishId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));
                    snapshotJoiner.add(dish.getName() + " x" + itemDTO.getQuantity());
                    return new OrderItem(order, dish, itemDTO.getQuantity(), dish.getPrice());
                })
                .collect(Collectors.toList());

        order.setItems(items);
        order.setDishSnapshot(snapshotJoiner.toString());

//      order.recalculateTotalPrice(); Seems redundant, since entity already does this
    }

    // =========================
    // PATCH ORDER FROM DTO
    // =========================
    public void patchOrderFromDTO(Order order, OrderRequestDTO dto) {
        if (dto.getCustomerName() != null) order.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerAddress() != null) order.setCustomerAddress(dto.getCustomerAddress());
        if (dto.getCustomerPhone() != null) order.setCustomerPhone(dto.getCustomerPhone());
        if (dto.getCustomerEmail() != null) order.setCustomerEmail(dto.getCustomerEmail());
        if (dto.getPaymentMethod() != null) order.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getDeliveryTime() != null) order.setDeliveryTime(dto.getDeliveryTime());
        if (dto.getSpecialInstructions() != null) order.setSpecialInstructions(dto.getSpecialInstructions());
        if (dto.getDiscountApplied() != null) order.setDiscountApplied(dto.getDiscountApplied());

        if (dto.getItems() != null) {
            StringJoiner snapshotJoiner = new StringJoiner(", ");

            List<OrderItem> items = dto.getItems().stream()
                    .map(itemDTO -> {
                        Dish dish = dishRepository.findById(itemDTO.getDishId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));
                        snapshotJoiner.add(dish.getName() + " x" + itemDTO.getQuantity());
                        return new OrderItem(order, dish, itemDTO.getQuantity(), dish.getPrice());
                    })
                    .collect(Collectors.toList());

            order.setItems(items);
            order.setDishSnapshot(snapshotJoiner.toString());
        }

//        order.recalculateTotalPrice(); Seems redundant, since entity already does this
    }
}

package com.nizami.homekitchen.dto;

import com.nizami.homekitchen.model.enums.OrderStatus;
import com.nizami.homekitchen.model.enums.PaymentStatus;

import java.time.LocalDateTime;

public record OrderResponseDTO(
        Long id,
        String customerName,
        String customerAddress,
        String customerPhone,
        String customerEmail,
        Integer quantity,
        Double unitPrice,
        Double totalPrice,
        Double discountApplied,
        OrderStatus status,
        PaymentStatus paymentStatus,
        String paymentMethod,
        LocalDateTime deliveryTime,
        String specialInstructions,
        Boolean isActive,
        String dishSnapshot,
        DishResponseDTO dish,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

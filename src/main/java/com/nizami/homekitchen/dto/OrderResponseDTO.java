package com.nizami.homekitchen.dto;

import com.nizami.homekitchen.model.enums.OrderStatus;
import com.nizami.homekitchen.model.enums.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String customerName,
        String customerAddress,
        String customerPhone,
        String customerEmail,
        Double discountApplied,
        Double totalPrice,
        OrderStatus status,
        PaymentStatus paymentStatus,
        String paymentMethod,
        LocalDateTime deliveryTime,
        String specialInstructions,
        Boolean isActive,
        String dishSnapshot,
        List<OrderItemResponseDTO> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

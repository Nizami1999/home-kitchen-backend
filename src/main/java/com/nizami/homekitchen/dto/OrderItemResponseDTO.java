package com.nizami.homekitchen.dto;

public record OrderItemResponseDTO(
        Long id,
        Long dishId,
        String dishName,
        Double unitPrice,
        Integer quantity,
        Double totalPrice
) {}

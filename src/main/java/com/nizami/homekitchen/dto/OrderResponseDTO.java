package com.nizami.homekitchen.dto;

import java.time.LocalDateTime;

public record OrderResponseDTO(
        Long id,
        String customerName,
        String customerAddress,
        Integer quantity,
        DishResponseDTO dish,
        LocalDateTime orderDate
) {}

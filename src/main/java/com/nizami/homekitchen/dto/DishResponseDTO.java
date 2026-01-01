package com.nizami.homekitchen.dto;

public record DishResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imageUrl,
        CategoryResponseDTO category
) {}

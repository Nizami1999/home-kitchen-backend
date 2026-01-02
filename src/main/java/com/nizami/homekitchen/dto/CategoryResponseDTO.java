package com.nizami.homekitchen.dto;

public record CategoryResponseDTO(
        Long id,
        String name,
        String imageUrl,
        String description,
        Integer dishCount,
        Boolean isActive,
        Integer sortOrder
) {}

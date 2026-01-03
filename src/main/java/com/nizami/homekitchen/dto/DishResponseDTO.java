package com.nizami.homekitchen.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DishResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imageUrl,
        CategoryResponseDTO category,
        Boolean isActive,
        Boolean isFeatured,
        Integer sortOrder,
        String ingredients,
        Boolean isVegetarian,
        Integer calories,
        String currency,
        Double discountPrice,
        List<String> galleryImages,
        String videoUrl,
        Double averageRating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

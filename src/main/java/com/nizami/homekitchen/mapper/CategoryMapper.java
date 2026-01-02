package com.nizami.homekitchen.mapper;

import com.nizami.homekitchen.dto.CategoryRequestDTO;
import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Full mapping from entity → DTO
    public CategoryResponseDTO toDTO(Category category, int dishCount) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                category.getDescription(),
                dishCount,
                category.getIsActive(),
                category.getSortOrder()
        );
    }

    // Mapping from request DTO → entity
    public Category toEntity(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setImageUrl(dto.getImageUrl());
        category.setDescription(dto.getDescription());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        return category;
    }

    // Update entire entity from DTO
    public void updateEntity(Category category, CategoryRequestDTO dto) {
        category.setName(dto.getName());
        category.setImageUrl(dto.getImageUrl());
        category.setDescription(dto.getDescription());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : category.getIsActive());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : category.getSortOrder());
    }

    // Patch partial update
    public void patchEntity(Category category, CategoryRequestDTO dto) {
        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getImageUrl() != null) category.setImageUrl(dto.getImageUrl());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) category.setIsActive(dto.getIsActive());
        if (dto.getSortOrder() != null) category.setSortOrder(dto.getSortOrder());
    }
}

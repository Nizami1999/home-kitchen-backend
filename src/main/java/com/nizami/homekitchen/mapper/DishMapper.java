package com.nizami.homekitchen.mapper;

import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.dto.DishRequestDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.repository.DishRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    private final DishRepository dishRepository;

    public DishMapper(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    // =========================
    // ENTITY → DTO
    // =========================

    public DishResponseDTO toResponseDTO(Dish dish) {
        Category category = dish.getCategory();

        return new DishResponseDTO(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                new CategoryResponseDTO(
                        category.getId(),
                        category.getName(),
                        category.getImageUrl(),
                        category.getDescription(),
                        getDishCount(category.getId()), // 🔥 cached
                        category.getIsActive(),
                        category.getSortOrder()
                )
        );
    }

    // =========================
    // DTO → ENTITY
    // =========================

    public Dish toEntity(DishRequestDTO dto, Category category) {
        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(category);
        return dish;
    }

    public void updateDish(Dish dish, DishRequestDTO dto, Category category) {
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(category);
    }

    public void patchDish(Dish dish, DishRequestDTO dto) {
        if (dto.getName() != null) dish.setName(dto.getName());
        if (dto.getDescription() != null) dish.setDescription(dto.getDescription());
        if (dto.getPrice() != null) dish.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) dish.setImageUrl(dto.getImageUrl());
    }

    // =========================
    // CACHING (NEW)
    // =========================

    @Cacheable(value = "dishCountByCategory", key = "#categoryId")
    public int getDishCount(Long categoryId) {
        return (int) dishRepository.countByCategoryId(categoryId);
    }
}

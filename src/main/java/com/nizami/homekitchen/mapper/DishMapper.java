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
                        getDishCount(category.getId()),
                        category.getIsActive(),
                        category.getSortOrder()
                ),
                dish.getIsActive(),
                dish.getIsFeatured(),
                dish.getSortOrder(),
                dish.getIngredients(),
                dish.getIsVegetarian(),
                dish.getCalories(),
                dish.getCurrency(),
                dish.getDiscountPrice(),
                dish.getGalleryImages(),
                dish.getVideoUrl(),
                dish.getAverageRating(),
                dish.getCreatedAt(),
                dish.getUpdatedAt()
        );
    }

    // =========================
    // DTO → ENTITY (CREATE)
    // =========================

    public Dish toEntity(DishRequestDTO dto, Category category) {
        Dish dish = new Dish();

        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(category);

        dish.setIsActive(dto.getIsActive());
        dish.setIsFeatured(dto.getIsFeatured());
        dish.setSortOrder(dto.getSortOrder());
        dish.setIngredients(dto.getIngredients());
        dish.setIsVegetarian(dto.getIsVegetarian());
        dish.setCalories(dto.getCalories());
        dish.setCurrency(dto.getCurrency());
        dish.setDiscountPrice(dto.getDiscountPrice());
        dish.setGalleryImages(dto.getGalleryImages());
        dish.setVideoUrl(dto.getVideoUrl());
        dish.setAverageRating(dto.getAverageRating());

        return dish;
    }

    // =========================
    // DTO → ENTITY (PUT)
    // =========================

    public void updateDish(Dish dish, DishRequestDTO dto, Category category) {
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setImageUrl(dto.getImageUrl());
        dish.setCategory(category);

        dish.setIsActive(dto.getIsActive());
        dish.setIsFeatured(dto.getIsFeatured());
        dish.setSortOrder(dto.getSortOrder());
        dish.setIngredients(dto.getIngredients());
        dish.setIsVegetarian(dto.getIsVegetarian());
        dish.setCalories(dto.getCalories());
        dish.setCurrency(dto.getCurrency());
        dish.setDiscountPrice(dto.getDiscountPrice());
        dish.setGalleryImages(dto.getGalleryImages());
        dish.setVideoUrl(dto.getVideoUrl());
        dish.setAverageRating(dto.getAverageRating());
    }

    // =========================
    // DTO → ENTITY (PATCH)
    // =========================

    public void patchDish(Dish dish, DishRequestDTO dto) {
        if (dto.getName() != null) dish.setName(dto.getName());
        if (dto.getDescription() != null) dish.setDescription(dto.getDescription());
        if (dto.getPrice() != null) dish.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) dish.setImageUrl(dto.getImageUrl());

        if (dto.getIsActive() != null) dish.setIsActive(dto.getIsActive());
        if (dto.getIsFeatured() != null) dish.setIsFeatured(dto.getIsFeatured());
        if (dto.getSortOrder() != null) dish.setSortOrder(dto.getSortOrder());
        if (dto.getIngredients() != null) dish.setIngredients(dto.getIngredients());
        if (dto.getIsVegetarian() != null) dish.setIsVegetarian(dto.getIsVegetarian());
        if (dto.getCalories() != null) dish.setCalories(dto.getCalories());
        if (dto.getCurrency() != null) dish.setCurrency(dto.getCurrency());
        if (dto.getDiscountPrice() != null) dish.setDiscountPrice(dto.getDiscountPrice());
        if (dto.getGalleryImages() != null) dish.setGalleryImages(dto.getGalleryImages());
        if (dto.getVideoUrl() != null) dish.setVideoUrl(dto.getVideoUrl());
        if (dto.getAverageRating() != null) dish.setAverageRating(dto.getAverageRating());
    }

    // =========================
    // CACHING
    // =========================

    @Cacheable(value = "dishCountByCategory", key = "#categoryId")
    public int getDishCount(Long categoryId) {
        return (int) dishRepository.countByCategoryId(categoryId);
    }
}

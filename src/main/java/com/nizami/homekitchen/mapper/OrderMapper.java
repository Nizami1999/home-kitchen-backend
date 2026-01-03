package com.nizami.homekitchen.mapper;

import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.dto.OrderRequestDTO;
import com.nizami.homekitchen.dto.OrderResponseDTO;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.model.Order;
import com.nizami.homekitchen.repository.DishRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class OrderMapper {

    private final DishRepository dishRepository;

    public OrderMapper(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    // =========================
    // MAPPING
    // =========================
    public OrderResponseDTO toResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerAddress(),
                order.getQuantity(),
                mapDish(order.getDish()),
                order.getOrderDate()
        );
    }

    private DishResponseDTO mapDish(Dish dish) {
        return new DishResponseDTO(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                mapCategory(dish.getCategory()),
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

    private CategoryResponseDTO mapCategory(Category category) {
        int dishCount = getDishCount(category.getId());
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

    @Cacheable(value = "dishCountByCategory", key = "#categoryId")
    public int getDishCount(Long categoryId) {
        return (int) dishRepository.countByCategoryId(categoryId);
    }

    // =========================
    // UPDATE ENTIRE ORDER (PUT)
    // =========================
    public void updateOrderFromDTO(Order order, OrderRequestDTO dto) {
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerAddress(dto.getCustomerAddress());
        order.setQuantity(dto.getQuantity());

        Dish dish = dishRepository.findById(dto.getDishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));
        order.setDish(dish);

    }

    // =========================
    // PARTIAL UPDATE ORDER (PATCH)
    // =========================
    public void patchOrderFromDTO(Order order, OrderRequestDTO dto) {
        if (dto.getCustomerName() != null) order.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerAddress() != null) order.setCustomerAddress(dto.getCustomerAddress());
        if (dto.getQuantity() != null) order.setQuantity(dto.getQuantity());
        if (dto.getDishId() != null) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));
            order.setDish(dish);
        }

    }
}

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
                order.getCustomerPhone(),
                order.getCustomerEmail(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalPrice(),
                order.getDiscountApplied(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),
                order.getDeliveryTime(),
                order.getSpecialInstructions(),
                order.getIsActive(),
                order.getDishSnapshot(),
                mapDish(order.getDish()),
                order.getCreatedAt(),
                order.getUpdatedAt()
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

        // Set unit price from the dish
        order.setUnitPrice(dish.getPrice());

        // Calculate total price
        double total = dish.getPrice() * dto.getQuantity();
        if (dto.getDiscountApplied() != null) {
            total -= dto.getDiscountApplied();
            order.setDiscountApplied(dto.getDiscountApplied());
        } else {
            order.setDiscountApplied(0.0);
        }
        order.setTotalPrice(total);
        order.setDishSnapshot(dish.getName() + " - " + dish.getPrice());

    }


    // =========================
    // PARTIAL UPDATE ORDER (PATCH)
    // =========================
    public void patchOrderFromDTO(Order order, OrderRequestDTO dto) {
        // Update basic fields if provided
        if (dto.getCustomerName() != null) order.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerAddress() != null) order.setCustomerAddress(dto.getCustomerAddress());
        if (dto.getQuantity() != null) order.setQuantity(dto.getQuantity());

        boolean recalcPrice = false;

        // Update dish if provided
        if (dto.getDishId() != null) {
            Dish dish = dishRepository.findById(dto.getDishId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish ID not found"));
            order.setDish(dish);
            order.setUnitPrice(dish.getPrice());

            // Update dish snapshot
            order.setDishSnapshot(dish.getName() + " - " + dish.getPrice());

            recalcPrice = true;
        }

        // Recalculate total price if quantity or dish changed
        if (dto.getQuantity() != null) recalcPrice = true;

        if (recalcPrice) {
            int quantity = order.getQuantity() != null ? order.getQuantity() : 1;
            double unitPrice = order.getUnitPrice() != null ? order.getUnitPrice() : 0.0;

            // Use discount from DTO if provided, otherwise fallback to existing order discount, or 0
            double discount = dto.getDiscountApplied() != null
                    ? dto.getDiscountApplied()
                    : (order.getDiscountApplied() != null ? order.getDiscountApplied() : 0.0);

            order.setDiscountApplied(discount);
            order.setTotalPrice(unitPrice * quantity - discount);
        }

        // Optional: update discount if only discount was provided (no quantity/dish change)
        if (!recalcPrice && dto.getDiscountApplied() != null) {
            double discount = dto.getDiscountApplied();
            order.setDiscountApplied(discount);

            double unitPrice = order.getUnitPrice() != null ? order.getUnitPrice() : 0.0;
            int quantity = order.getQuantity() != null ? order.getQuantity() : 1;
            order.setTotalPrice(unitPrice * quantity - discount);
        }
    }


}

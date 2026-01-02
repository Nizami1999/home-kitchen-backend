package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.dto.DishRequestDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.repository.CategoryRepository;
import com.nizami.homekitchen.repository.DishRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // GET all dishes, optionally filtered by category ID
    @GetMapping
    public List<DishResponseDTO> getDishes(@RequestParam(required = false) Long categoryId) {
        List<Dish> dishes;

        if (categoryId != null) {
            // Validate category exists
            boolean exists = categoryRepository.existsById(categoryId);
            if (!exists) {
                return Collections.emptyList(); // Or throw 400 if preferred
            }
            dishes = dishRepository.findByCategoryId(categoryId);
        } else {
            dishes = dishRepository.findAll();
        }

        return dishes.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // GET single dish by ID
    @GetMapping("/{id}")
    public DishResponseDTO getDishById(@PathVariable Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
        return mapToResponseDTO(dish);
    }

    // POST create a new dish
    @PostMapping
    public DishResponseDTO createDish(@RequestBody @Valid DishRequestDTO requestDTO) {
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));

        Dish dish = new Dish();
        dish.setName(requestDTO.getName());
        dish.setDescription(requestDTO.getDescription());
        dish.setPrice(requestDTO.getPrice());
        dish.setImageUrl(requestDTO.getImageUrl());
        dish.setCategory(category);

        Dish savedDish = dishRepository.save(dish);
        return mapToResponseDTO(savedDish);
    }

    // PUT update entire dish
    @PutMapping("/{id}")
    public DishResponseDTO updateDish(@PathVariable Long id, @RequestBody @Valid DishRequestDTO requestDTO) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));

        dish.setName(requestDTO.getName());
        dish.setDescription(requestDTO.getDescription());
        dish.setPrice(requestDTO.getPrice());
        dish.setImageUrl(requestDTO.getImageUrl());
        dish.setCategory(category);

        Dish updatedDish = dishRepository.save(dish);
        return mapToResponseDTO(updatedDish);
    }

    // PATCH partial update of dish
    @PatchMapping("/{id}")
    public DishResponseDTO patchDish(@PathVariable Long id, @RequestBody DishRequestDTO requestDTO) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));

        if (requestDTO.getName() != null) dish.setName(requestDTO.getName());
        if (requestDTO.getDescription() != null) dish.setDescription(requestDTO.getDescription());
        if (requestDTO.getPrice() != null) dish.setPrice(requestDTO.getPrice());
        if (requestDTO.getImageUrl() != null) dish.setImageUrl(requestDTO.getImageUrl());

        if (requestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));
            dish.setCategory(category);
        }

        Dish updatedDish = dishRepository.save(dish);
        return mapToResponseDTO(updatedDish);
    }

    // DELETE dish
    @DeleteMapping("/{id}")
    public void deleteDish(@PathVariable Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        dishRepository.deleteById(id);
    }

    // DELETE all dishes
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllDishes() {
        dishRepository.deleteAll();
    }

    // GET all dishes for a specific category
    @GetMapping("/categories/{categoryId}")
    public List<DishResponseDTO> getDishesByCategory(@PathVariable Long categoryId) {
        // Validate category exists
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Fetch all dishes for this category
        List<Dish> dishes = dishRepository.findByCategoryId(categoryId);

        // Sort by dish ID or any other field if needed
        dishes.sort((a, b) -> a.getId().compareTo(b.getId()));

        return dishes.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // POST create multiple dishes at once
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DishResponseDTO> createMultipleDishes(@RequestBody @Valid List<DishRequestDTO> requests) {
        List<Dish> dishes = requests.stream().map(req -> {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));

            Dish dish = new Dish();
            dish.setName(req.getName());
            dish.setDescription(req.getDescription());
            dish.setPrice(req.getPrice());
            dish.setImageUrl(req.getImageUrl());
            dish.setCategory(category);

            return dish;
        }).toList();

        List<Dish> savedDishes = dishRepository.saveAll(dishes);

        return savedDishes.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // Utility method to map Dish entity to response DTO
    private DishResponseDTO mapToResponseDTO(Dish dish) {
        Category category = dish.getCategory();
        // Compute dishCount dynamically
        int dishCount = (int) dishRepository.countByCategoryId(category.getId());
        return new DishResponseDTO(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                new CategoryResponseDTO(
                        dish.getCategory().getId(),
                        dish.getCategory().getName(),
                        dish.getCategory().getImageUrl(),
                        dish.getCategory().getDescription(),
                        dishCount,
                        dish.getCategory().getIsActive(),
                        dish.getCategory().getSortOrder()
                )
        );
    }
}

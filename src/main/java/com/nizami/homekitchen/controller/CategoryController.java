package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.CategoryRequestDTO;
import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.repository.CategoryRepository;
import com.nizami.homekitchen.repository.DishRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DishRepository dishRepository;

    // GET all categories
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // GET single category by ID
    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return mapToResponseDTO(category);
    }

    // POST create a new category
    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody @Valid CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setImageUrl(requestDTO.getImageUrl());
        category.setDescription(requestDTO.getDescription());
        category.setIsActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true);
        category.setSortOrder(requestDTO.getSortOrder() != null ? requestDTO.getSortOrder() : 0);

        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }

    // PUT update entire category
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(requestDTO.getName());
        category.setImageUrl(requestDTO.getImageUrl());
        category.setDescription(requestDTO.getDescription());
        category.setIsActive(requestDTO.getIsActive() != null ? requestDTO.getIsActive() : true);
        category.setSortOrder(requestDTO.getSortOrder() != null ? requestDTO.getSortOrder() : 0);

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }

    // PATCH partial update
    @PatchMapping("/{id}")
    public CategoryResponseDTO patchCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (requestDTO.getName() != null) category.setName(requestDTO.getName());
        if (requestDTO.getImageUrl() != null) category.setImageUrl(requestDTO.getImageUrl());
        if (requestDTO.getDescription() != null) category.setDescription(requestDTO.getDescription());
        if (requestDTO.getIsActive() != null) category.setIsActive(requestDTO.getIsActive());
        if (requestDTO.getSortOrder() != null) category.setSortOrder(requestDTO.getSortOrder());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }

    // DELETE category by ID
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    // DELETE all categories
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    // POST multiple categories
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CategoryResponseDTO> createMultipleCategories(@RequestBody @Valid List<CategoryRequestDTO> requests) {
        List<Category> categories = requests.stream().map(req -> {
            Category category = new Category();
            category.setName(req.getName());
            category.setImageUrl(req.getImageUrl());
            category.setDescription(req.getDescription());
            category.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
            category.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
            return category;
        }).toList();

        List<Category> savedCategories = categoryRepository.saveAll(categories);
        return savedCategories.stream().map(this::mapToResponseDTO).toList();
    }

    // Utility method to map Category entity to DTO with dynamic dishCount
    private CategoryResponseDTO mapToResponseDTO(Category category) {
        int dishCount = (int) dishRepository.countByCategoryId(category.getId());
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
}

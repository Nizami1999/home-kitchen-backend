package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.CategoryRequestDTO;
import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // =========================
    // GET
    // =========================

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody @Valid CategoryRequestDTO dto) {
        return categoryService.createCategory(dto);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CategoryResponseDTO> createMultipleCategories(@RequestBody @Valid List<CategoryRequestDTO> dtos) {
        return categoryService.createMultipleCategories(dtos);
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO dto) {
        return categoryService.updateCategory(id, dto);
    }

    @PatchMapping("/{id}")
    public CategoryResponseDTO patchCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO dto) {
        return categoryService.patchCategory(id, dto);
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCategories() {
        categoryService.deleteAllCategories();
    }
}

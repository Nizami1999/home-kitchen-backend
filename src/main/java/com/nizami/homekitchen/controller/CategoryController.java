package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.CategoryRequestDTO;
import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // GET all categories
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName()))
                .toList();
    }

    // GET single category by ID
    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    // POST create a new category
    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody @Valid CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(savedCategory.getId(), savedCategory.getName());
    }

    // PUT update entire category
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(requestDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(updatedCategory.getId(), updatedCategory.getName());
    }

    // PATCH partial update (for future-proofing)
    @PatchMapping("/{id}")
    public CategoryResponseDTO patchCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (requestDTO.getName() != null) {
            category.setName(requestDTO.getName());
        }

        Category updatedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(updatedCategory.getId(), updatedCategory.getName());
    }

    // DELETE category
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }
}

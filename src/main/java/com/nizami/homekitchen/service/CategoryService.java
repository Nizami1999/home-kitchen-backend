package com.nizami.homekitchen.service;

import com.nizami.homekitchen.dto.CategoryRequestDTO;
import com.nizami.homekitchen.dto.CategoryResponseDTO;
import com.nizami.homekitchen.mapper.CategoryMapper;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.repository.CategoryRepository;
import com.nizami.homekitchen.repository.DishRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            DishRepository dishRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
        this.categoryMapper = categoryMapper;
    }

    // =========================
    // GET
    // =========================

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return mapToResponseDTO(category);
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = categoryMapper.toEntity(requestDTO);
        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    @Transactional
    public List<CategoryResponseDTO> createMultipleCategories(List<CategoryRequestDTO> requests) {
        List<Category> categories = requests.stream()
                .map(categoryMapper::toEntity)
                .toList();
        List<Category> saved = categoryRepository.saveAll(categories);
        return saved.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryMapper.updateEntity(category, requestDTO);
        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public CategoryResponseDTO patchCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryMapper.patchEntity(category, requestDTO);
        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    // =========================
    // HELPER / MAPPER
    // =========================

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        int dishCount = getDishCount(category.getId());
        return categoryMapper.toDTO(category, dishCount);
    }

    // Optional caching
    @Cacheable(value = "dishCountByCategory", key = "#categoryId")
    public int getDishCount(Long categoryId) {
        return (int) dishRepository.countByCategoryId(categoryId);
    }
}

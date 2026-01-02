package com.nizami.homekitchen.service;

import com.nizami.homekitchen.dto.DishRequestDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.mapper.DishMapper;
import com.nizami.homekitchen.model.Category;
import com.nizami.homekitchen.model.Dish;
import com.nizami.homekitchen.repository.CategoryRepository;
import com.nizami.homekitchen.repository.DishRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final DishMapper dishMapper;

    public DishService(
            DishRepository dishRepository,
            CategoryRepository categoryRepository,
            DishMapper dishMapper
    ) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.dishMapper = dishMapper;
    }

    // =========================
    // READ
    // =========================

    @Transactional
    public Page<DishResponseDTO> getDishes(Long categoryId, Pageable pageable) {

        Page<Dish> dishes;

        if (categoryId != null) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
            }
            dishes = dishRepository.findByCategoryId(categoryId, pageable);
        } else {
            dishes = dishRepository.findAll(pageable);
        }

        return dishes.map(dishMapper::toResponseDTO);
    }

    @Transactional
    public DishResponseDTO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
        return dishMapper.toResponseDTO(dish);
    }

    // =========================
    // CREATE
    // =========================

    @Transactional
    public DishResponseDTO createDish(DishRequestDTO dto) {
        Category category = getCategory(dto.getCategoryId());

        Dish dish = dishMapper.toEntity(dto, category);
        return dishMapper.toResponseDTO(dishRepository.save(dish));
    }

    @Transactional
    public List<DishResponseDTO> createMultiple(List<DishRequestDTO> requests) {
        List<Dish> dishes = requests.stream()
                .map(dto -> dishMapper.toEntity(dto, getCategory(dto.getCategoryId())))
                .toList();

        return dishRepository.saveAll(dishes).stream()
                .map(dishMapper::toResponseDTO)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================

    @Transactional
    public DishResponseDTO updateDish(Long id, DishRequestDTO dto) {
        Dish dish = getDish(id);
        Category category = getCategory(dto.getCategoryId());

        dishMapper.updateDish(dish, dto, category);
        return dishMapper.toResponseDTO(dishRepository.save(dish));
    }

    @Transactional
    public DishResponseDTO patchDish(Long id, DishRequestDTO dto) {
        Dish dish = getDish(id);

        if (dto.getCategoryId() != null) {
            dish.setCategory(getCategory(dto.getCategoryId()));
        }

        dishMapper.patchDish(dish, dto);
        return dishMapper.toResponseDTO(dishRepository.save(dish));
    }

    // =========================
    // DELETE
    // =========================

    @Transactional
    public void deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        }
        dishRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        dishRepository.deleteAll();
    }

    // =========================
    // HELPERS
    // =========================

    private Dish getDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));
    }
}

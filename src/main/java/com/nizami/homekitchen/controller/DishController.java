package com.nizami.homekitchen.controller;

import com.nizami.homekitchen.dto.DishRequestDTO;
import com.nizami.homekitchen.dto.DishResponseDTO;
import com.nizami.homekitchen.service.DishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    // =========================
    // READ (pagination added)
    // =========================

    @GetMapping
    public Page<DishResponseDTO> getDishes(
            @RequestParam(required = false) Long categoryId,
            Pageable pageable
    ) {
        return dishService.getDishes(categoryId, pageable);
    }

    @GetMapping("/{id}")
    public DishResponseDTO getDish(@PathVariable Long id) {
        return dishService.getDishById(id);
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishResponseDTO createDish(@RequestBody @Valid DishRequestDTO dto) {
        return dishService.createDish(dto);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DishResponseDTO> createMultiple(@RequestBody @Valid List<DishRequestDTO> dtos) {
        return dishService.createMultiple(dtos);
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public DishResponseDTO updateDish(
            @PathVariable Long id,
            @RequestBody @Valid DishRequestDTO dto
    ) {
        return dishService.updateDish(id, dto);
    }

    @PatchMapping("/{id}")
    public DishResponseDTO patchDish(
            @PathVariable Long id,
            @RequestBody DishRequestDTO dto
    ) {
        return dishService.patchDish(id, dto);
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        dishService.deleteAll();
    }
}

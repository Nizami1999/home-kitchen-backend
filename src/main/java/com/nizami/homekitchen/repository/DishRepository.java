package com.nizami.homekitchen.repository;

import com.nizami.homekitchen.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findByCategoryId(Long categoryId, Pageable pageable);
    long countByCategoryId(Long categoryId);

}

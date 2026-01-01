package com.nizami.homekitchen.repository;

import com.nizami.homekitchen.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCategoryId(Long categoryId);

}

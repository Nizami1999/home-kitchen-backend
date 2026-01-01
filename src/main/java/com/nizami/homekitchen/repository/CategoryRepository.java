package com.nizami.homekitchen.repository;

import com.nizami.homekitchen.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

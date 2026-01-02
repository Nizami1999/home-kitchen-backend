package com.nizami.homekitchen.dto;

import jakarta.validation.constraints.*;

public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Image URL must be a valid URL starting with http or https"
    )
    private String imageUrl;

    private String description;

    private Boolean isActive;

    @Min(value = 0, message = "Sort order cannot be negative")
    private Integer sortOrder;

    public CategoryRequestDTO() {
    }

    public CategoryRequestDTO(String name, String imageUrl, String description, Boolean isActive, Integer sortOrder) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}

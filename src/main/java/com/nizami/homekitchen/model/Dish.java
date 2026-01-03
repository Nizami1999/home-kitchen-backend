package com.nizami.homekitchen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "ingredients", length = 2000)
    private String ingredients;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian = false;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "currency", length = 10)
    private String currency = "HUF";

    @Column(name = "discount_price")
    private Double discountPrice;

    @ElementCollection
    @CollectionTable(name = "dish_gallery_images", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "image_url")
    private List<String> galleryImages = new ArrayList<>();

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Dish() {}

    public Dish(Long id, String name, String description, Double price, String imageUrl, Category category,
                Boolean isActive, Boolean isFeatured, Integer sortOrder, String ingredients, Boolean isVegetarian,
                Integer calories, String currency, Double discountPrice, List<String> galleryImages,
                String videoUrl, Double averageRating, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isActive = isActive;
        this.isFeatured = isFeatured;
        this.sortOrder = sortOrder;
        this.ingredients = ingredients;
        this.isVegetarian = isVegetarian;
        this.calories = calories;
        this.currency = currency;
        this.discountPrice = discountPrice;
        this.galleryImages = galleryImages;
        this.videoUrl = videoUrl;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public Boolean getIsVegetarian() { return isVegetarian; }
    public void setIsVegetarian(Boolean isVegetarian) { this.isVegetarian = isVegetarian; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Double discountPrice) { this.discountPrice = discountPrice; }

    public List<String> getGalleryImages() { return galleryImages; }
    public void setGalleryImages(List<String> galleryImages) { this.galleryImages = galleryImages; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                ", isActive=" + isActive +
                ", isFeatured=" + isFeatured +
                ", sortOrder=" + sortOrder +
                ", ingredients='" + ingredients + '\'' +
                ", isVegetarian=" + isVegetarian +
                ", calories=" + calories +
                ", currency='" + currency + '\'' +
                ", discountPrice=" + discountPrice +
                ", galleryImages=" + galleryImages +
                ", videoUrl='" + videoUrl + '\'' +
                ", averageRating=" + averageRating +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

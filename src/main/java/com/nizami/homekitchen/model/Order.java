package com.nizami.homekitchen.model;

import com.nizami.homekitchen.model.enums.OrderStatus;
import com.nizami.homekitchen.model.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_address", nullable = false)
    private String customerAddress;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "discount_applied")
    private Double discountApplied = 0.0;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @Column(name = "special_instructions", length = 500)
    private String specialInstructions;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "dish_snapshot", columnDefinition = "TEXT")
    private String dishSnapshot;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // No-args constructor
    public Order() { }

    // Getters and setters
    public Long getId() { return id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Double getDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(Double discountApplied) {
        double itemsTotal = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        // Guard: discount cannot be null, negative, or greater than items total
        double discount = discountApplied != null && discountApplied > 0
                ? Math.min(discountApplied, itemsTotal)
                : 0.0;

        this.discountApplied = discount;
        recalculateTotalPrice();
    }

    public Double getTotalPrice() { return totalPrice; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getDishSnapshot() { return dishSnapshot; }
    public void setDishSnapshot(String dishSnapshot) { this.dishSnapshot = dishSnapshot; }

    public List<OrderItem> getItems() { return items; }

    public void setItems(List<OrderItem> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(this::addItem);
        }
        recalculateTotalPrice();
        updateDishSnapshot();
    }

    // Helper to add item and maintain bidirectional relationship
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("OrderItem cannot be null");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("OrderItem quantity must be greater than 0");
        }

        item.setOrder(this);
        this.items.add(item);
        recalculateTotalPrice();
        updateDishSnapshot();
    }


    public void removeItem(OrderItem item) {
        this.items.remove(item);
        item.setOrder(null);
        recalculateTotalPrice();
        updateDishSnapshot();
    }

    // Calculate totalPrice from items and discount
    public void recalculateTotalPrice() {
        double itemsTotal = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        double discount = discountApplied != null ? Math.min(discountApplied, itemsTotal) : 0.0;
        this.totalPrice = itemsTotal - discount;
    }


    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = createdAt;
        recalculateTotalPrice();
        updateDishSnapshot();
        validateOrderNotEmpty();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        recalculateTotalPrice();
        updateDishSnapshot();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", discountApplied=" + discountApplied +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", paymentStatus=" + paymentStatus +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", specialInstructions='" + specialInstructions + '\'' +
                ", isActive=" + isActive +
                ", items=" + items +
                ", dishSnapshot='" + dishSnapshot + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Helpers
    public void updateDishSnapshot() {
        StringJoiner joiner = new StringJoiner(", ");
        items.forEach(item -> joiner.add(item.getDish().getName() + " x" + item.getQuantity()));
        this.dishSnapshot = joiner.toString();
    }

    public void validateOrderNotEmpty() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
    }
}

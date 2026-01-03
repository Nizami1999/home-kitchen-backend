package com.nizami.homekitchen.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OrderRequestDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer address is required")
    private String customerAddress;

    private String customerPhone;

    private String customerEmail;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Dish ID is required")
    private Long dishId;

    private String paymentMethod;               // CASH, CARD, ONLINE, etc.
    private LocalDateTime deliveryTime;         // optional, can be null
    private String specialInstructions;         // optional
    private Double discountApplied;             // optional, if frontend can provide promo/discount

    public OrderRequestDTO() {}

    public OrderRequestDTO(String customerName, String customerAddress,
                           String customerPhone, String customerEmail,
                           Integer quantity, Long dishId, String paymentMethod,
                           LocalDateTime deliveryTime, String specialInstructions,
                           Double discountApplied) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.quantity = quantity;
        this.dishId = dishId;
        this.paymentMethod = paymentMethod;
        this.deliveryTime = deliveryTime;
        this.specialInstructions = specialInstructions;
        this.discountApplied = discountApplied;
    }

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public Double getDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(Double discountApplied) { this.discountApplied = discountApplied; }
}

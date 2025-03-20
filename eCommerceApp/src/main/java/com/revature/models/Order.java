package com.revature.models;

import java.time.LocalDateTime;

public class Order {
    private final int orderId;
    private int userId;
    private double totalPrice;
    private Status status;
    private final LocalDateTime createdAt;

    private int orderCounter = 1;

    public Order(int userId, double totalPrice) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = Status.PENDING;
        this.orderId = orderCounter;
        this.createdAt = null;
        orderCounter++;
    }

    public Order(int orderId, int userId, double totalPrice, Status status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

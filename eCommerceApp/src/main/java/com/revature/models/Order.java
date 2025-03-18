package com.revature.models;

import java.time.LocalDateTime;

public class Order {
    private long orderId;
    private long userId;
    private double totalPrice;
    private Status status;
    private LocalDateTime createdAt;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

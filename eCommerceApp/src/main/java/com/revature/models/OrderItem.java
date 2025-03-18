package com.revature.models;

public class OrderItem{
    private long orderItemId;
    private int quantity;
    private double price;

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }
}

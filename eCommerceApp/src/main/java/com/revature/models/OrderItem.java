package com.revature.models;

public class OrderItem{
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;

    private static int orderItemCounter = 1;
    public OrderItem(int productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.orderId = 0;
        this.orderItemId =+ orderItemCounter;
    }

    public OrderItem(int orderItemId, int orderId, int productId, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
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

package com.revature.models;

public class CartItem{
    private int cartItemId;
    private int productId;
    private int userId;
    private int quantity;

    private static int cartItemsCounter = 1;

    public CartItem(){}

    public CartItem(int cartItemId, int productId, int userId, int quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
    }

    public CartItem(int productId, int userId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.cartItemId =+ cartItemsCounter;
    }

    public int getProductId() {
        return productId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

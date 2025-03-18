package com.revature.models;

public class CartItem{
    private long cartItemId;
    private int quantity;

    public CartItem(long cartItemId, int quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

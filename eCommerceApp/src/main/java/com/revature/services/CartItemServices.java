package com.revature.services;

import com.revature.models.CartItem;
import com.revature.repos.CartItemDAO;

import java.util.List;

public class CartItemServices {
    private CartItemDAO repository;

    public CartItemServices(CartItemDAO repository){
        this.repository = repository;
    }

    public List<CartItem> getOrderCartItems(int orderId){
        return repository.getByOrderId(orderId);
    }
}

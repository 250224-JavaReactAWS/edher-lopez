package com.revature.repos;

import com.revature.models.CartItem;

import java.util.List;

public interface CartItemDAO {
    List<CartItem> getByOrderId(int orderId);
}

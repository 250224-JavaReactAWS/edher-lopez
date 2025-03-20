package com.revature.repos;

import com.revature.models.CartItem;

import java.util.List;

public interface CartDAO extends GeneralDAO<CartItem> {
    boolean deleteByUserId(int userId);
    List<CartItem> getByUserId(int userId);
}

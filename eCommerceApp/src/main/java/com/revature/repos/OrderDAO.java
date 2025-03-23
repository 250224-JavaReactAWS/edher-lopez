package com.revature.repos;

import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.models.Status;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends GeneralDAO<Order>{
    Order createWithOrderItems(Order order, List<OrderItem> orderItems) throws SQLException;
    List<Order> getByStatus(Status status);
    List<Order> getByUserId(int userId);
}

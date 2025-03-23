package com.revature.repos;

import com.revature.models.Order;
import com.revature.models.OrderItem;
import com.revature.models.Status;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImp implements  OrderDAO{
    @Override
    public Order createWithOrderItems(Order order, List<OrderItem> orderItems) throws SQLException{
        Connection conn = ConnectionUtil.getConnection();
        try{

            conn.setAutoCommit(false);
            String sqlOrder = "INSERT INTO orders (user_id, total_price) VALUES (?, ?) RETURNING *";
            String sqlItems = "INSERT INTO order_items (product_id, order_id, price, quantity) VALUES " + buildSQLStringForOrderItems(orderItems);
            PreparedStatement orderStatement = conn.prepareStatement(sqlOrder);
            orderStatement.setInt(1, order.getUserId());
            orderStatement.setDouble(2, order.getTotalPrice());
            ResultSet orderResult = orderStatement.executeQuery();
            if (orderResult.next()){
                Order savedOrder = createOrderFromResultSet(orderResult);
                PreparedStatement itemsStatement = conn.prepareStatement(sqlItems);
                int valueCounter = 1;
                for(OrderItem item : orderItems){
                    itemsStatement.setInt(valueCounter, item.getProductId());
                    valueCounter++;
                    itemsStatement.setInt(valueCounter, savedOrder.getOrderId());
                    valueCounter++;
                    itemsStatement.setDouble(valueCounter, item.getPrice());
                    valueCounter++;
                    itemsStatement.setInt(valueCounter, item.getQuantity());
                    valueCounter++;
                }
                int affectedRows = itemsStatement.executeUpdate();
                if(affectedRows != orderItems.size()){
                    throw new SQLException("Could not place order");
                }
                conn.commit();
                return savedOrder;
            }
        }catch (SQLException e) {
            System.out.println("Could not place order");
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
        return null;
    }

    @Override
    public List<Order> getByStatus(Status status) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM orders WHERE status = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, status.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(createOrderFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e) {
            System.out.println("Could not get orders");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<Order> getByUserId(int userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM orders WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(createOrderFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e) {
            System.out.println("Could not get orders");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Order create(Order obj) {
        return null;
    }

    @Override
    public List<Order> getAll() {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM orders";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(createOrderFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e) {
            System.out.println("Could not get orders");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Order getById(int id) {
        return null;
    }

    @Override
    public Order update(Order obj) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                return true;
            }
        }catch (SQLException e) {
            System.out.println("Could not get orders");
            e.printStackTrace();
        }
        return false;
    }

    private String buildSQLStringForOrderItems(List<OrderItem> orderItems){
        StringBuilder sb = new StringBuilder();
        for(OrderItem item : orderItems){
            sb.append("(? , ? , ? , ?),");
        }
        sb.setCharAt(sb.lastIndexOf(","), ';');
        return sb.toString();
    }

    private Order createOrderFromResultSet(ResultSet resultSet) throws SQLException
    {
        return new Order(
                resultSet.getInt("order_id"),
                resultSet.getInt("user_id"),
                resultSet.getDouble("total_price"),
                Status.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

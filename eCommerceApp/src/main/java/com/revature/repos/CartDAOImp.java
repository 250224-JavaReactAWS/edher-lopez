package com.revature.repos;

import com.revature.models.CartItem;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImp implements CartDAO{
    @Override
    public boolean deleteByUserId(int userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "DELETE FROM cart_items WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                return true;
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CartItem> getByUserId(int userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT *  FROM cart_items WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CartItem> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(createCartItemFromResultSet(resultSet));
            }
            return result;
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public CartItem create(CartItem obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?) RETURNING *";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, obj.getUserId());
            preparedStatement.setInt(2, obj.getProductId());
            preparedStatement.setInt(3, obj.getQuantity());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return createCartItemFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CartItem> getAll() {
        return List.of();
    }

    @Override
    public CartItem getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return createCartItemFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartItem update(CartItem obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ? RETURNIN *";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, obj.getCartItemId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return createCartItemFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                return true;
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return false;
    }

    private CartItem createCartItemFromResultSet(ResultSet resultSet) throws SQLException
    {
        return new CartItem(
                resultSet.getInt("cart_item_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("quantity")
        );
    }
}

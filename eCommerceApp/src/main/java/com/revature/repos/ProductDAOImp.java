package com.revature.repos;

import com.revature.models.Product;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductDAOImp implements ProductDAO{

    @Override
    public List<Product> getByIdList(List<Integer> idList) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = buildSQLStringForListOfIds(idList.size());
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int i = 1;
            for(int id : idList){
                preparedStatement.setInt(i, id);
                i++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()){
                products.add(buildProductFromResultSet(resultSet));
            }
            return products;
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Product create(Product obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?) RETURNING *";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getDescription());
            preparedStatement.setDouble(3, obj.getPrice());
            preparedStatement.setInt(4, obj.getStock());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return buildProductFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not save product");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getAll() {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM products";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> products = new LinkedList<>();
            while (resultSet.next()){
                products.add(buildProductFromResultSet(resultSet));
            }
            return products;
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Product getById(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return buildProductFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not get requested products");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product update(Product obj) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE product_id = ? RETURNING *";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getDescription());
            preparedStatement.setDouble(3, obj.getPrice());
            preparedStatement.setInt(4, obj.getStock());
            preparedStatement.setInt(5, obj.getProductId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return buildProductFromResultSet(resultSet);
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
            String sql = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
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


    private String buildSQLStringForListOfIds(int listSize){
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE product_id in (");
        for(int i = 0; i < listSize; i++){
            sql.append(i == listSize - 1 ? "?)" : "?, ");
        }
        return sql.toString();
    }

    private Product buildProductFromResultSet(ResultSet resultSet)throws SQLException{
        return new Product(
                resultSet.getInt("product_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getInt("stock")
        );
    }
}

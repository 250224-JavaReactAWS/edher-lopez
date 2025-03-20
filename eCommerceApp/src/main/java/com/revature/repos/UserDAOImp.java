package com.revature.repos;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImp implements UserDAO{
    @Override
    public User getUserByEmail(String userName) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not retrieve user by email");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User create(User obj) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO users (first_name, last_name, email, password)" +
                    "VALUES (?, ?, ?, ?) RETURNING *;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, obj.getFirstName());
            preparedStatement.setString(2, obj.getLastName());
            preparedStatement.setString(3, obj.getEmail());
            preparedStatement.setString(4, obj.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not save user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not retrieve user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User obj) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ? RETURNING *";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, obj.getFirstName());
            preparedStatement.setString(2, obj.getLastName());
            preparedStatement.setString(3, obj.getEmail());
            preparedStatement.setString(4, obj.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
        }catch (SQLException e) {
            System.out.println("Could not save user");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                return true;
            }
        }catch (SQLException e) {
            System.out.println("Could not delete user");
            e.printStackTrace();
        }
        return false;
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException{
        User user = new User(
                resultSet.getInt("user_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
        return user;
    }
}

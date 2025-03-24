package com.revature.services;

import com.revature.models.User;
import com.revature.repos.UserDAO;

public class UserServices {
    private final UserDAO repository;

    public UserServices(UserDAO repository){
        this.repository = repository;
    }

    public User createUser(
            String firstName,
            String lastName,
            String email,
            String password
    ){
        validateEmail(email);
        validateThatEmailHasNotBeenRegistered(email);
        validatePassword(password);
        User unregisteredUser = new User(firstName, lastName, email, password);
        return repository.create(unregisteredUser);
    }

    public User loginUser(String email, String password){
        User storedUser = repository.getUserByEmail(email);
        if(storedUser != null){
            if(storedUser.getPassword().equals(password)){
                return storedUser;
            }
        }
        return null;
    }

    public User updateName(int userId, String newFirstName, String newLastName){
        User storedUser = repository.getById(userId);
        if(storedUser != null){
            if(newFirstName!=null){
                storedUser.setFirstName(newFirstName);
            }
            if(newLastName!=null){
                storedUser.setLastName(newLastName);
            }
            return repository.update(storedUser);
        }
        return null;
    }

    public User updateEmail(int userId, String newEmail, String password){
        User user = repository.getById(userId);
        validateEmail(newEmail);
        validateThatEmailHasNotBeenRegistered(newEmail);
        if(user != null){
            if(user.getPassword().equals(password)){
                user.setEmail(newEmail);
                return repository.update(user);
            }
        }
        return null;
    }

    public User updatePassword(int userId, String oldPassword, String newPassword){
        User user = repository.getById(userId);
        validatePassword(newPassword);
        if(user != null){
            if(user.getPassword().equals(oldPassword)){
                user.setPassword(newPassword);
                return repository.update(user);
            }
        }
        return null;
    }
    private void validateEmail(String email){
        boolean hasAt = false;
        boolean hasDotAfterAt = false;

        for (int i = 0; i < email.length(); i++) {
            char currentChar = email.charAt(i);

            if (currentChar == '@') {
                hasAt = true;
            } else if (currentChar == '.' && hasAt) {
                hasDotAfterAt = true;
            }
        }

        if(!hasAt || !hasDotAfterAt){
            throw new IllegalArgumentException("You must provide a valid email");
        }

    }

    private void validateThatEmailHasNotBeenRegistered(String email){
        if(repository.getUserByEmail(email) != null){
            throw new IllegalArgumentException("Your email is already registered.");
        }
    }

    private void validatePassword(String password){
        boolean correctLength = password.length() >= 8;
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        if(password.length() < 8){
            throw new IllegalArgumentException("Password must have at least 8 characters.");
        }

        char[] characters = password.toCharArray();
        int i = 0;
        while((!hasLowercase && !hasUppercase) || i<characters.length){
            if (Character.isUpperCase(characters[i])){
                hasUppercase = true;
            } else if (Character.isLowerCase(characters[i])){
                hasLowercase = true;
            }
            i++;
        }
        if(!hasLowercase || !hasUppercase){
            throw new IllegalArgumentException("Password must contain a capital and lowercase letter");
        }
    }
}

package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Order;
import com.revature.models.User;
import com.revature.services.UserServices;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices){
        this.userServices = userServices;
    }

    //Create an account
    public void createUserAccount(Context ctx){
        User requestedUser = ctx.bodyAsClass(User.class);
        try{
            User createdUser = userServices.createUser(
                    requestedUser.getFirstName(),
                    requestedUser.getLastName(),
                    requestedUser.getEmail(),
                    requestedUser.getPassword()
            );
            if(createdUser == null){
                ctx.status(500);
                ctx.json(new ErrorMessage("Something went wrong!"));
                return;
            }

            ctx.json(createdUser).status(201);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void loginUser(Context ctx){
        User requestedUser = ctx.bodyAsClass(User.class);
        User loggedUser = userServices.loginUser(requestedUser.getEmail(), requestedUser.getPassword());
        if (loggedUser == null){
            ctx.json(new ErrorMessage("Email or password incorrect")).status(400);
            return;
        }
        ctx.json(loggedUser).status(200);
        ctx.sessionAttribute("userId", loggedUser.getUserId());
        ctx.sessionAttribute("role", loggedUser.getRole());
    }

    public void updateUserName(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            return;
        }
        try{
            User updatedUser = userServices.updateName(userId, requestUser.getFirstName(), requestUser.getLastName());
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Something went wrong!")).status(500);
                return;
            }
            ctx.json(updatedUser).status(200);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void updateEmail(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            return;
        }
        try{
            User updatedUser = userServices.updateEmail(userId, requestUser.getEmail(), requestUser.getPassword());
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Password incorrect!")).status(400);
                return;
            }
            ctx.json(updatedUser).status(200);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void updatePassword(Context ctx){
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            return;
        }
        try{
            User updatedUser = userServices.updatePassword(userId, (String) body.get("oldPassword"), (String) body.get("newPassword"));
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Old password is incorrect!")).status(400);
                return;
            }
            ctx.json(updatedUser).status(200);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

}

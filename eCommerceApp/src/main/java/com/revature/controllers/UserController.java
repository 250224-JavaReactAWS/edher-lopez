package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Order;
import com.revature.models.User;
import com.revature.services.UserServices;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
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
                logger.warn("Something went wrong while trying to create a new User");
                return;
            }
            ctx.json(createdUser).status(201);
            logger.info("A new user was created successfully");
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
            logger.info("There were issues where the given values to create a new user");
        }
    }

    public void loginUser(Context ctx){
        User requestedUser = ctx.bodyAsClass(User.class);
        User loggedUser = userServices.loginUser(requestedUser.getEmail(), requestedUser.getPassword());
        if (loggedUser == null){
            ctx.json(new ErrorMessage("Email or password incorrect")).status(400);
            logger.warn("Attempt to log in with bad credentials");
            return;
        }
        ctx.json(loggedUser).status(200);
        ctx.sessionAttribute("userId", loggedUser.getUserId());
        ctx.sessionAttribute("role", loggedUser.getRole());
        logger.info("A user was logged in successfully");
    }

    public void updateUserName(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            logger.info("An not logged user tried to update some info");
            return;
        }
        try{
            User updatedUser = userServices.updateName(userId, requestUser.getFirstName(), requestUser.getLastName());
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Something went wrong!")).status(500);
                logger.warn("Something went wrong while trying to update an user");
                return;
            }
            ctx.json(updatedUser).status(200);
            logger.info("An user was updated");
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);

        }
    }

    public void updateEmail(Context ctx){
        User requestUser = ctx.bodyAsClass(User.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            logger.info("something went wrong while trying to update an user email");
            return;
        }
        try{
            User updatedUser = userServices.updateEmail(userId, requestUser.getEmail(), requestUser.getPassword());
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Password incorrect!")).status(400);
                logger.info("An attempt to change an email with bad password was made");
                return;
            }
            ctx.json(updatedUser).status(200);
            logger.info("User email was updated");
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
            logger.info("Attempt to update an email with illegal arguments");
        }
    }

    public void updatePassword(Context ctx){
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        Integer userId = ctx.sessionAttribute("userId");
        if(userId == null){
            ctx.json(new ErrorMessage("You must be logged in use this method")).status(401);
            logger.info("Someone tried to update a password without be logged in");
            return;
        }
        try{
            User updatedUser = userServices.updatePassword(userId, (String) body.get("oldPassword"), (String) body.get("newPassword"));
            if(updatedUser == null){
                ctx.json(new ErrorMessage("Old password is incorrect!")).status(400);
                logger.warn("something went wrong while trying to update a user password");
                return;
            }
            ctx.json(updatedUser).status(200);
            logger.info("User password was updated");
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
            logger.info("Attemp to update password with illegal arguments");
        }
    }

}

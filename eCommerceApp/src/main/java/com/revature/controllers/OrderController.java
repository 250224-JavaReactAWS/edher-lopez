package com.revature.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.services.OrderServices;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderController {
    private final OrderServices orderServices;

    public OrderController(OrderServices orderServices){
        this.orderServices = orderServices;
    }

    public void viewOrderHistory(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this method"));
            ctx.status(401);
            return;
        }
        List<Order> orders = orderServices.viewOrderHistory(userId);
        ctx.json(orders).status(200);
    }

    public void placeOrder(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this method"));
            ctx.status(401);
            return;
        }
        try {
            Order placedOrder = orderServices.placeOrder(userId);
            if(placedOrder == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                return;
            }
            ctx.json(placedOrder).status(201);
        }catch (SQLException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(500);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }
    public void viewListOfAllOrders(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this method"));
            ctx.status(401);
            return;
        }
        if(Role.valueOf(ctx.sessionAttribute("role")) != Role.ADMIN){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            return;
        }
        List<String> statusQueried = ctx.queryParams("status");
        if(!statusQueried.isEmpty()){
            try{
                List<Order> orders = new ArrayList<>();
                for(String s: statusQueried){
                    orders.addAll(orderServices.viewAllOrders(Status.valueOf(s)));
                }
                ctx.json(orders).status(200);
            }catch (IllegalArgumentException e){
                ctx.json(new ErrorMessage("Requested status does not exist")).status(400);
            }
            return;
        }
        List<Order> orders = orderServices.viewAllOrders();
        ctx.json(orders).status(200);
    }

    public void updateOrderStatus(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this method"));
            ctx.status(401);
            return;
        }
        if(Role.valueOf(ctx.sessionAttribute("role")) != Role.ADMIN){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            return;
        }

        try{
            Order requestOrder = ctx.bodyAsClass(Order.class);
            if(requestOrder==null){
                ctx.json(new ErrorMessage("Could not get needed information from request")).status(400);
                return;
            }
            int orderId = Integer.parseInt(ctx.pathParam("orderId"));
            Order savedProduct = orderServices.updateOrderStatus(orderId, requestOrder.getStatus());
            if(savedProduct == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                return;
            }
            ctx.json(savedProduct).status(201);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }
}

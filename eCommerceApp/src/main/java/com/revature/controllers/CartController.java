package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.CartItem;
import com.revature.services.CartServices;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartController {
    private final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartServices cartServices;

    public  CartController(CartServices cartServices){
        this.cartServices = cartServices;
    }

    public void addProductToCar(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("Someone tried to add a product to his car");
            return;
        }
        CartItem itemRequest = ctx.bodyAsClass(CartItem.class);
        try{
            CartItem savedItem = cartServices.addProductToCart(itemRequest.getProductId(), userId, itemRequest.getQuantity());
            if(savedItem == null){
                ctx.json(new ErrorMessage("Something went wrong!")).status(500);
                logger.warn("Something went wrong while trying to insert a CartItem");
                return;
            }
            ctx.json(savedItem).status(201);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void removeProductFromCart(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("Someone tried to remove a product from his car");
            return;
        }
        try {
            int cartItemId = Integer.parseInt(ctx.pathParam("cartItemId"));
            boolean result = cartServices.deleteCartItem(cartItemId, userId);
            if(!result){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                logger.warn("Something went wrong while trying to remove a CartItem");
                return;
            }
            ctx.status(204);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(404);
        }catch (RuntimeException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(403);
        }
    }

    public void updateCartItemQuantity(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");
        if(userId==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("Someone tried to update a product from his car");
            return;
        }
        try {
            CartItem requestCartItem = ctx.bodyAsClass(CartItem.class);
            if(requestCartItem==null){
                ctx.json(new ErrorMessage("Could not get needed information from request")).status(400);
                return;
            }
            int cartItemId = Integer.parseInt(ctx.pathParam("cartItemId"));
            CartItem updatedCartItem = cartServices.updateCartItemQuantity(userId ,cartItemId, requestCartItem.getQuantity());
            if(updatedCartItem == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                logger.warn("Something went wrong while trying to update a CartItem");
                return;
            }
            ctx.json(updatedCartItem).status(200);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }catch (RuntimeException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(403);
        }
    }
}

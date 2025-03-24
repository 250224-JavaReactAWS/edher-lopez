package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.services.ProductServices;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductServices productServices;

    public ProductController(ProductServices productServices){
        this.productServices = productServices;
    }

    public void viewProducts(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("An not logged in user tried to view products");
        }
        List<Product> products = productServices.listProducts();
        ctx.json(products).status(200);
    }

    public void viewProductDetails(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("An not logged in user tried to view product details");
            return;
        }
        int productId = Integer.parseInt(ctx.pathParam("productId"));
        Product product = productServices.viewProductDetails(productId);
        if(product == null){
            ctx.json(new ErrorMessage("Product not Found")).status(404);
            return;
        }
        ctx.json(product).status(200);
    }

    public void addProductToCatalog(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("An not logged in user tried to add a product");
            return;
        }
        if(!Role.ADMIN.equals(ctx.sessionAttribute("role"))){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            logger.info("A not admin user tried to add a product");
            return;
        }
        Product requestProduct = ctx.bodyAsClass(Product.class);
        if(requestProduct==null){
            ctx.json(new ErrorMessage("Could not get needed information from request")).status(400);
            logger.info("Could not get needed information from request");
            return;
        }
        try{
            Product savedProduct = productServices.addProduct(requestProduct);
            if(savedProduct == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                logger.warn("Something went wrong while trying to save a product");
                return;
            }
            ctx.json(savedProduct).status(201);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void updateProduct(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("An not logged in user tried to update a product");
            return;
        }
        if(!Role.ADMIN.equals(ctx.sessionAttribute("role"))){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            logger.info("A not admin user tried to update a product");
            return;
        }
        Product requestProduct = ctx.bodyAsClass(Product.class);
        if(requestProduct==null){
            ctx.json(new ErrorMessage("Could not get needed information from request")).status(400);
            return;
        }
        requestProduct.setProductId(Integer.parseInt(ctx.pathParam("productId")));
        try{
            Product savedProduct = productServices.updateProduct(requestProduct);
            if(savedProduct == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                logger.warn("Something went wrong while trying to update a product");
                return;
            }
            ctx.json(savedProduct).status(201);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(400);
        }
    }

    public void deleteProduct(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
            logger.info("An not logged in user tried to delete a product");
            return;
        }
        if(!Role.ADMIN.equals(ctx.sessionAttribute("role"))){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            logger.info("A not admin user tried to delete a product");
            return;
        }

        int productId = Integer.parseInt(ctx.pathParam("productId"));
        try{
            boolean result = productServices.deleteProduct(productId);
            if(!result){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                logger.warn("Something went wrong while trying to delete a product");
                return;
            }
            ctx.status(204);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(404);
        }
    }
}

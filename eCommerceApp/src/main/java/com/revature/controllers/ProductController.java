package com.revature.controllers;

import com.revature.dtos.response.ErrorMessage;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.services.ProductServices;
import io.javalin.http.Context;

import java.util.List;

public class ProductController {
    private final ProductServices productServices;

    public ProductController(ProductServices productServices){
        this.productServices = productServices;
    }

    public void viewProducts(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
        }
        List<Product> products = productServices.listProducts();
        ctx.json(products).status(200);
    }

    public void viewProductDetails(Context ctx){
        if(ctx.sessionAttribute("userId")==null){
            ctx.json(new ErrorMessage("You must be logged in to access this feature")).status(401);
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
            return;
        }
        if(Role.valueOf(ctx.sessionAttribute("role")) != Role.ADMIN){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            return;
        }
        Product requestProduct = ctx.bodyAsClass(Product.class);
        if(requestProduct==null){
            ctx.json(new ErrorMessage("Could not get needed information from request")).status(400);
            return;
        }
        try{
            Product savedProduct = productServices.addProduct(requestProduct);
            if(savedProduct == null){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
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
            return;
        }
        if(Role.valueOf(ctx.sessionAttribute("role")) != Role.ADMIN){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
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
            return;
        }
        if(Role.valueOf(ctx.sessionAttribute("role")) != Role.ADMIN){
            ctx.json(new ErrorMessage("You are not allowed access this feature!")).status(403);
            return;
        }

        int productId = Integer.parseInt(ctx.pathParam("productId"));
        try{
            boolean result = productServices.deleteProduct(productId);
            if(!result){
                ctx.json(new ErrorMessage("Something went wrong")).status(500);
                return;
            }
            ctx.status(204);
        }catch (IllegalArgumentException e){
            ctx.json(new ErrorMessage(e.getMessage())).status(404);
        }
    }
}

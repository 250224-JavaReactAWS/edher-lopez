package com.revature.util;

import com.revature.controllers.CartController;
import com.revature.controllers.OrderController;
import com.revature.controllers.ProductController;
import com.revature.controllers.UserController;
import com.revature.repos.*;
import com.revature.services.CartServices;
import com.revature.services.OrderServices;
import com.revature.services.ProductServices;
import com.revature.services.UserServices;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinUtil {
    public static Javalin create(int port){
        UserDAO userDAO = new UserDAOImp();
        UserServices userServices = new UserServices(userDAO);
        UserController userController = new UserController(userServices);

        OrderDAO orderDAO = new OrderDAOImp();
        CartDAO cartDAO = new CartDAOImp();
        ProductDAO productDAO = new ProductDAOImp();
        OrderServices orderServices = new OrderServices(cartDAO, orderDAO, productDAO);
        OrderController orderController = new OrderController(orderServices);

        ProductServices productServices = new ProductServices(productDAO);
        ProductController productController = new ProductController(productServices);

        CartServices cartServices = new CartServices(cartDAO, productDAO);
        CartController cartController = new CartController(cartServices);

        return Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                path("/users", () -> {
                    post("/register", userController::createUserAccount);
                    post("/login", userController::loginUser);
                    patch("/name", userController::updateUserName);
                    patch("/email", userController::updateEmail);
                    patch("/password", userController::updatePassword);
                });
                path("/orders", () ->{
                    get("/history", orderController::viewOrderHistory);
                    post("", orderController::placeOrder);
                    get("", orderController::viewListOfAllOrders);
                    patch("/{orderId}", orderController::updateOrderStatus);
                });
                path("/products", () ->{
                    get("", productController::viewProducts);
                    post("", productController::addProductToCatalog);
                    get("/{productId}", productController::viewProductDetails);
                    put("/{productId}", productController::updateProduct);
                    delete("/{productId}", productController::deleteProduct);
                });
                path("/cart", () -> {
                    post("", cartController::addProductToCar);
                    delete("/{cartItemId}", cartController::removeProductFromCart);
                    patch("/{cartItemId}", cartController::updateCartItemQuantity);
                });
            });
        }).start(port);
    }
}

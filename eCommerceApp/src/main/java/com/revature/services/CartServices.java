package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.CartDAO;
import com.revature.repos.ProductDAO;

public class CartServices {
    private final CartDAO cartRepository;
    private final ProductDAO productRepository;

    public CartServices(CartDAO cartRepository, ProductDAO productRepository){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartItem addProductToCart(int productId, int userId, int quantity) {
        validateQuantity(productId, quantity);
        return cartRepository.create(new CartItem(productId,userId, quantity));
    }

    public boolean deleteCartItem(int cartItemId, int userId){
        CartItem item = cartRepository.getById(cartItemId);
        if(item == null){
            throw new IllegalArgumentException("Cart item was not found");
        }
        if(item.getUserId()!=userId){
            throw new RuntimeException("You are not allowed to perform this operation");
        }
        return cartRepository.deleteById(cartItemId);
    }

    public CartItem updateCartItemQuantity(int userId, int cartItemId, int newQuantity) {
        CartItem storedCartItem = cartRepository.getById(cartItemId);
        if(storedCartItem == null){
            throw new IllegalArgumentException("Cart item was not found");
        }
        if(storedCartItem.getUserId()!=userId){
            throw new RuntimeException("You are not allowed to perform this operation");
        }
        validateQuantity(storedCartItem.getProductId(), newQuantity);
        storedCartItem.setQuantity(newQuantity);
        return cartRepository.update(storedCartItem);
    }

    private void validateQuantity(int productId, int quantity){
        Product product = productRepository.getById(productId);
        if (product == null){
            throw new IllegalArgumentException("Product was not Found");
        }
        if(product.getStock() < quantity){
            throw new IllegalArgumentException("Quantity can not be higher than stocked product");
        }
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }


}

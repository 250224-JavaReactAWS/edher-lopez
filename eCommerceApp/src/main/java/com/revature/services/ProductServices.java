package com.revature.services;

import com.revature.models.Product;
import com.revature.repos.ProductDAO;

import java.util.List;

public class ProductServices {
    private final ProductDAO repository;

    public ProductServices(ProductDAO repository){
        this.repository = repository;
    }

    public List<Product> listProducts() {
        return repository.getAll();
    }

    public Product viewProductDetails(int productId) {
        return repository.getById(productId);
    }

    public Product addProduct(Product product) {
        validateRequiredStringField(product.getName(), "Product name");
        validateRequiredStringField(product.getDescription(), "Product description");
        validatePrice(product.getPrice());
        validateStock(product.getStock());
        return repository.create(product);
    }

    public Product updateProduct(Product product) {
        Product storedProduct = repository.getById(product.getProductId());
        if(product.getName()!=null){
            validateRequiredStringField(product.getName(), "Product name");
            storedProduct.setName(product.getName());
        }
        if(product.getDescription() != null){
            validateRequiredStringField(product.getDescription(), "Product description");
            storedProduct.setDescription(product.getDescription());
        }
        if(product.getPrice() != 0){
            validatePrice(product.getPrice());
            storedProduct.setPrice(product.getPrice());
        }
        if(product.getStock()!=0){
            validateStock(product.getStock());
            storedProduct.setStock(product.getStock());
        }
        return repository.update(storedProduct);
    }

    public boolean deleteProduct(int productId){
        Product product = repository.getById(productId);
        if(product==null){
            throw new IllegalArgumentException("Product was not found");
        }
        return repository.deleteById(productId);
    }


    private void validateRequiredStringField(String s, String fieldName){
        if(s == null || s.isEmpty()){
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private void validatePrice(double price){
        if(price<=0){
            throw new IllegalArgumentException("Price should be greater than 0");
        }
    }

    private void validateStock(int stock){
        if(stock < 0) throw new IllegalArgumentException("Stock should be a positive integer");
    }

}

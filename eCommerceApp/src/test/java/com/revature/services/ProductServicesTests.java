package com.revature.services;

import com.revature.repos.ProductDAO;
import com.revature.models.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ProductServicesTests {
    private ProductServices productServices;
    private ProductDAO mockedDAO;
    private List<Product> mockedList;

    @Before
    public void setUp(){
        mockedDAO = Mockito.mock(ProductDAO.class);
        productServices = new ProductServices(mockedDAO);
        mockedList = new ArrayList<>();
        mockedList.add(new Product("test", "test description", 10.5, 10));
        mockedList.add(new Product("test2", "test description2", 10.5, 10));
        mockedList.add(new Product("test3", "test description3", 10.5, 10));
        when(mockedDAO.getById(anyInt())).thenAnswer(invocation -> {
            int argument = invocation.getArgument(0);
            for(Product p : mockedList){
                if(p.getProductId() == argument) return p;
            }
            return null;
        });
    }

    @Test
    public void listProductsShouldReturnAvailableProducts(){
        when(mockedDAO.getAll()).thenReturn(mockedList);
        List<Product> products = productServices.listProducts();
        Assert.assertEquals(mockedList, products);
    }

    @Test
    public void viewProductDetailsShouldReturnRequestedProduct(){
        int testedId = 1;
        Product product = productServices.viewProductDetails(testedId);
        Assert.assertEquals(testedId, product.getProductId());
    }

    @Test
    public void addProductShouldReturnAProductWithTheAttributesOfGivenProduct(){
        Product testedProduct = new Product("tested", "test description", 10.0, 10);
        when(mockedDAO.create(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return new Product(p.getName(), p.getDescription(), p.getPrice(), p.getStock());
        });
        Product returnedProduct = productServices.addProduct(testedProduct);
        Assert.assertEquals(testedProduct.getName(), returnedProduct.getName());
        Assert.assertEquals(testedProduct.getDescription(), returnedProduct.getDescription());
        Assert.assertEquals(testedProduct.getPrice(), returnedProduct.getPrice(), 0.0001);
        Assert.assertEquals(testedProduct.getStock(), returnedProduct.getStock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductShouldThrowExceptionWhenPassedNullName(){
        Product testProduct = new Product(null, "tested", 10.0, 10);
        Product returnedProduct = productServices.addProduct(testProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductShouldThrowExceptionWhenPassedNullDescription(){
        Product testProduct = new Product("Tested", null, 10.0, 10);
        Product returnedProduct = productServices.addProduct(testProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductShouldThrowExceptionWhenPassedInvalidPrice(){
        Product testProduct = new Product("Tested", "Description", 0, 10);
        Product returnedProduct = productServices.addProduct(testProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductShouldThrowExceptionWhenPassedInvalidStock(){
        Product testProduct = new Product("Tested", "Description", 14, -2);
        Product returnedProduct = productServices.addProduct(testProduct);
    }

    @Test
    public void updateProductShouldReturnProductWithNewValues(){
        int existingId = 1;
        Product productWithNewValues = new Product(existingId, "New name", "New description", 14.5, 20);
        when(mockedDAO.update(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return new Product(p.getProductId(), p.getName(), p.getDescription(), p.getPrice(), p.getStock());
        });
        Product returnedProduct = productServices.updateProduct(productWithNewValues);
        Assert.assertEquals(productWithNewValues.getProductId(), returnedProduct.getProductId());
        Assert.assertEquals(productWithNewValues.getName(), returnedProduct.getName());
        Assert.assertEquals(productWithNewValues.getDescription(), returnedProduct.getDescription());
        Assert.assertEquals(productWithNewValues.getPrice(), returnedProduct.getPrice(), 0.0001);
        Assert.assertEquals(productWithNewValues.getStock(), returnedProduct.getStock());
    }

}

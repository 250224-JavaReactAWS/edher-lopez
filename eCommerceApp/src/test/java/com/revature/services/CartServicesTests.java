package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.CartDAO;
import com.revature.repos.ProductDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class CartServicesTests {
    private CartServices cartServices;
    private CartDAO mockedCartDAO;
    private ProductDAO mockedProductDao;

    private final int testProductId = 1;
    private final int testUserId = 1;
    private final int testQuantity = 10;
    private final int testCartItemId = 1;

    @Before
    public void setUp(){
        mockedCartDAO = Mockito.mock(CartDAO.class);
        mockedProductDao = Mockito.mock(ProductDAO.class);
        cartServices = new CartServices(mockedCartDAO, mockedProductDao);
        when(mockedCartDAO.create(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem given = invocation.getArgument(0);
            return new CartItem(given.getProductId(), given.getUserId(), given.getQuantity());
        });
        when(mockedProductDao.getById(anyInt())).thenAnswer(invocation -> {
            int given = invocation.getArgument(0);
            return new Product(given, "Test", "Test", 10.5, 10);
        });
        CartItem mockedCartItem = new CartItem(testCartItemId, testProductId, testUserId, testQuantity);
        when(mockedCartDAO.update(any(CartItem.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        when(mockedCartDAO.getById(testCartItemId)).thenReturn(mockedCartItem);
    }

    @Test
    public void addProductToCartShouldReturnTheAddedCartItem(){

        CartItem returnedCartItem = cartServices.addProductToCart(testProductId, testUserId, testQuantity);
        Assert.assertEquals(testProductId, returnedCartItem.getProductId());
        Assert.assertEquals(testQuantity, returnedCartItem.getQuantity());
        Assert.assertEquals(testUserId, returnedCartItem.getUserId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProductToCartShouldThrowAnExceptionWhenThereAreNotEnoughProducts(){
        int higherQuantity = 11;
        CartItem returnedCartItem = cartServices.addProductToCart(testProductId, testUserId, higherQuantity);
    }

    @Test
    public void updateCartItemQuantityShouldReturnCartItemWithNewQuantity(){
        int newQuantity = 8;
        CartItem returnedCartItem = cartServices.updateCartItemQuantity(testUserId, testCartItemId, newQuantity);
        Assert.assertEquals(testCartItemId, returnedCartItem.getCartItemId());
        Assert.assertEquals(newQuantity, returnedCartItem.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCartItemQuantityShouldThrowAnExceptionWhenQuantityIsZero(){
        int illegalQuantity = 0;
        CartItem returnedCartItem = cartServices.updateCartItemQuantity(testUserId ,testCartItemId, illegalQuantity);
    }

}
